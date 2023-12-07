package org.opentrafficsim.unity.demo;

import java.awt.Dimension;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Locale;

import javax.naming.NamingException;

import org.djunits.unit.DurationUnit;
import org.djunits.unit.LengthUnit;
import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Length;
import org.djunits.value.vdouble.scalar.Time;
import org.djutils.io.URLResource;
import org.opentrafficsim.core.dsol.AbstractOtsModel;
import org.opentrafficsim.core.dsol.OtsAnimator;
import org.opentrafficsim.core.dsol.OtsSimulatorInterface;
import org.opentrafficsim.draw.core.OtsDrawingException;
import org.opentrafficsim.road.network.RoadNetwork;
import org.opentrafficsim.road.network.factory.xml.parser.XmlNetworkLaneParser;
import org.opentrafficsim.road.network.lane.CrossSectionLink;
import org.opentrafficsim.road.network.lane.Lane;
import org.opentrafficsim.road.network.lane.conflict.ConflictBuilder;
import org.opentrafficsim.road.network.lane.object.trafficlight.TrafficLight;
import org.opentrafficsim.road.network.lane.object.trafficlight.TrafficLightColor;
import org.opentrafficsim.swing.gui.OtsAnimationPanel;
import org.opentrafficsim.swing.gui.OtsSimulationApplication;
import org.opentrafficsim.unity.demo.TJunctionDemoUnity.TJunctionModelUnity;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.language.DSOLException;

/**
 * <p>
 * Copyright (c) 2013-2023 Delft University of Technology, PO Box 5, 2600 AA, Delft, the Netherlands. All rights reserved. <br>
 * BSD-style license. See <a href="https://opentrafficsim.org/docs/license.html">OpenTrafficSim License</a>.
 * </p>
 * @author <a href="https://github.com/averbraeck">Alexander Verbraeck</a>
 * @author <a href="https://tudelft.nl/staff/p.knoppers-1">Peter Knoppers</a>
 * @author <a href="https://dittlab.tudelft.nl">Wouter Schakel</a>
 */
public class TJunctionDemoUnity extends OtsSimulationApplication<TJunctionModelUnity>
{
    /** */
    private static final long serialVersionUID = 20161211L;

    /**
     * Create a T-Junction demo.
     * @param title String; the title of the Frame
     * @param panel OtsAnimationPanel; the tabbed panel to display
     * @param model TJunctionModel; the model
     * @throws OtsDrawingException on animation error
     */
    public TJunctionDemoUnity(final String title, final OtsAnimationPanel panel, final TJunctionModelUnity model)
            throws OtsDrawingException
    {
        super(model, panel);
    }

    /**
     * Main program.
     * @param args String[]; the command line arguments (not used)
     */
    public static void main(final String[] args)
    {
        Locale.setDefault(Locale.FRANCE);
        demo(true);
    }

    /**
     * Start the demo.
     * @param exitOnClose boolean; when running stand-alone: true; when running as part of a demo: false
     */
    public static void demo(final boolean exitOnClose)
    {
        try
        {
            OtsAnimator simulator = new OtsAnimator("TJunctionDemo");
            final TJunctionModelUnity junctionModel = new TJunctionModelUnity(simulator);
            simulator.initialize(Time.ZERO, Duration.ZERO, Duration.instantiateSI(3600.0), junctionModel);
            OtsAnimationPanel animationPanel = new OtsAnimationPanel(junctionModel.getNetwork().getExtent(),
                    new Dimension(800, 600), simulator, junctionModel, DEFAULT_COLORER, junctionModel.getNetwork());
            TJunctionDemoUnity app = new TJunctionDemoUnity("T-Junction demo", animationPanel, junctionModel);
            app.setExitOnClose(exitOnClose);
            animationPanel.enableSimulationControlButtons();
        }
        catch (SimRuntimeException | NamingException | RemoteException | OtsDrawingException | DSOLException exception)
        {
            exception.printStackTrace();
        }
    }

    /**
     * The simulation model.
     */
    public static class TJunctionModelUnity extends AbstractOtsModel
    {
        /** */
        private static final long serialVersionUID = 20161211L;

        /** The network. */
        private RoadNetwork network;

        /**
         * @param simulator OtsSimulatorInterface; the simulator for this model
         */
        public TJunctionModelUnity(final OtsSimulatorInterface simulator)
        {
            super(simulator);
        }

        /** {@inheritDoc} */
        @Override
        public void constructModel() throws SimRuntimeException
        {
            try
            {
                URL xmlURL = URLResource.getResource("/resources/TJunctionUnity.xml");
                if (xmlURL == null)
                {
                    xmlURL = URLResource.getResource("/TJunctionUnity.xml");
                }
                this.network = new RoadNetwork("TJunction", getSimulator());
                XmlNetworkLaneParser.build(xmlURL, this.network, false);

                // add conflicts
                // ((CrossSectionLink) this.network.getLink("SCEC")).setPriority(Priority.STOP);
                // ((CrossSectionLink) this.network.getLink("SCWC")).setPriority(Priority.STOP);
                ConflictBuilder.buildConflicts(this.network, this.simulator,
                        new ConflictBuilder.FixedWidthGenerator(new Length(2.0, LengthUnit.SI)));

                // add trafficlight after
                Lane lane = ((CrossSectionLink) this.network.getLink("ECE")).getLanes().get(0);
                TrafficLight trafficLight =
                        new TrafficLight("light", lane, new Length(50.0, LengthUnit.SI), this.simulator);

                trafficLight.setTrafficLightColor(TrafficLightColor.RED);
                changePhase(trafficLight);

            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }

        /**
         * Changes color of traffic light.
         * @param trafficLight SimpleTrafficLight; traffic light
         * @throws SimRuntimeException scheduling error
         */
        private void changePhase(final TrafficLight trafficLight) throws SimRuntimeException
        {
            switch (trafficLight.getTrafficLightColor())
            {
                case RED:
                {
                    trafficLight.setTrafficLightColor(TrafficLightColor.GREEN);
                    this.simulator.scheduleEventRel(new Duration(30.0, DurationUnit.SECOND), this, "changePhase",
                            new Object[] {trafficLight});
                    break;
                }
                case YELLOW:
                {
                    trafficLight.setTrafficLightColor(TrafficLightColor.RED);
                    this.simulator.scheduleEventRel(new Duration(56.0, DurationUnit.SECOND), this, "changePhase",
                            new Object[] {trafficLight});
                    break;
                }
                case GREEN:
                {
                    trafficLight.setTrafficLightColor(TrafficLightColor.YELLOW);
                    this.simulator.scheduleEventRel(new Duration(4.0, DurationUnit.SECOND), this, "changePhase",
                            new Object[] {trafficLight});
                    break;
                }
                default:
                {
                    //
                }
            }
        }

        /** {@inheritDoc} */
        @Override
        public RoadNetwork getNetwork()
        {
            return this.network;
        }

    }
}

