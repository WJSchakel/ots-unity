package org.opentrafficsim.unity.demo;

import java.awt.Dimension;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Locale;

import javax.naming.NamingException;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Time;
import org.djutils.event.Event;
import org.djutils.event.EventListener;
import org.opentrafficsim.core.dsol.OtsAnimator;
import org.opentrafficsim.core.gtu.Gtu;
import org.opentrafficsim.draw.core.OtsDrawingException;
import org.opentrafficsim.road.gtu.lane.LaneBasedGtu;
import org.opentrafficsim.road.network.RoadNetwork;
import org.opentrafficsim.swing.gui.OtsAnimationPanel;
import org.opentrafficsim.swing.gui.OtsSwingApplication;
import org.opentrafficsim.unity.demo.TJunctionDemoUnity.TJunctionModelUnity;
import org.opentrafficsim.unity.message.DeleteVehicleRequest;
import org.opentrafficsim.unity.message.UpdateVehicleRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.Replication;
import nl.tudelft.simulation.language.DSOLException;

/**
 * T-junction demo to connect OTS with Unity.
 * @author wjschakel
 */
public class UnityDemo
{

    /**
     * Runs the T-junction demo to connect with Unity.
     * @param args String[] arguments (none used).
     */
    public static void main(String[] args)
    {
        Locale.setDefault(Locale.US);
        try
        {
            OtsAnimator simulator = new OtsAnimator("TJunctionDemo");
            final TJunctionModelUnity junctionModel = new TJunctionModelUnity(simulator);
            simulator.initialize(Time.ZERO, Duration.ZERO, Duration.instantiateSI(3600.0), junctionModel);
            OtsAnimationPanel animationPanel =
                    new OtsAnimationPanel(junctionModel.getNetwork().getExtent(), new Dimension(800, 600), simulator,
                            junctionModel, OtsSwingApplication.DEFAULT_COLORER, junctionModel.getNetwork());
            TJunctionDemoUnity app = new TJunctionDemoUnity("T-Junction demo", animationPanel, junctionModel);
            app.setExitOnClose(true);
            animationPanel.enableSimulationControlButtons();

            setupConnection(junctionModel.getNetwork());
        }
        catch (SimRuntimeException | NamingException | IOException | OtsDrawingException | DSOLException exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * Setup the connection.
     * @param network RoadNetwork; road network.
     * @throws IOException when the connection fails
     */
    private static void setupConnection(final RoadNetwork network) throws IOException
    {
        UnityEventHandler eventHandler = new UnityEventHandler(network);
        network.getSimulator().addListener(eventHandler, Replication.START_REPLICATION_EVENT);
        network.addListener(eventHandler, RoadNetwork.GTU_ADD_EVENT);
        network.addListener(eventHandler, RoadNetwork.GTU_REMOVE_EVENT);
    }

    /**
     * Handler of events from OTS to Unity. This class listens to internal OTS events, and sends appropriate messages.
     * @author wjschakel
     */
    private static class UnityEventHandler implements EventListener
    {
        /** */
        private static final long serialVersionUID = 20231207L;

        /** Port number. */
        private final static int MAIN_PORT = 5100;
        
        /** Time out [ms] */
        private final static int TIMEOUT = 5000;

        /** Road network. */
        private final RoadNetwork network;

        /** Printer for message over the communication network. */
        private final PrintWriter mainWriter;

        /** Gson string builder. */
        private Gson gson = new GsonBuilder().create();

        /**
         * Constructor.
         * @param network RoadNetwork; road network.
         * @throws IOException when the connection fails
         */
        public UnityEventHandler(final RoadNetwork network) throws IOException
        {
            this.network = network;
            try (ServerSocket serverSocket = new ServerSocket(MAIN_PORT))
            {
                serverSocket.setSoTimeout(TIMEOUT);
                Socket socket = serverSocket.accept();
                System.out.println("Connection with Unity achieved.");
                OutputStream output = socket.getOutputStream();
                this.mainWriter = new PrintWriter(output, true);
            }
        }

        /** {@inheritDoc} */
        @Override
        public void notify(final Event event) throws RemoteException
        {
            if (event.getType().equals(Gtu.MOVE_EVENT))
            {
                String gtuId = (String) ((Object[]) event.getContent())[0];
                LaneBasedGtu gtu = (LaneBasedGtu) network.getGTU(gtuId);
                this.mainWriter.print(this.gson.toJson(UpdateVehicleRequest.from(gtu)));
                this.mainWriter.flush();
            }
            else if (event.getType().equals(RoadNetwork.GTU_ADD_EVENT))
            {
                String gtuId = (String) event.getContent();
                Gtu gtu = network.getGTU(gtuId);
                gtu.addListener(this, Gtu.MOVE_EVENT);
                System.out.println("Added GTU " + gtuId);
            }
            else if (event.getType().equals(RoadNetwork.GTU_REMOVE_EVENT))
            {
                String gtuId = (String) event.getContent();
                String str = "DEL" + this.gson.toJson(new DeleteVehicleRequest(gtuId));
                this.mainWriter.print(str);
                this.mainWriter.flush();
                System.out.println("Removed GTU " + gtuId + " with string: " + str);
            }
            else if (event.getType().equals(Replication.START_REPLICATION_EVENT))
            {
                this.mainWriter.print("START");
                this.mainWriter.flush();
                System.out.println("Starting replication");
            }
        }

    }

}
