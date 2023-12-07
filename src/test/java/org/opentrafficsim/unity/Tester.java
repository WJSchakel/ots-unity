package org.opentrafficsim.unity;

import java.util.ArrayList;
import java.util.List;

import org.opentrafficsim.unity.message.Position;
import org.opentrafficsim.unity.message.VehicleState;
import org.opentrafficsim.unity.message.MessageTypes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public final class Tester
{

    /** Private. */
    private Tester()
    {
        //
    }
    
    public static void main(final String[] args)
    {
        
        // Create a dummy list of vehicle states
        List<VehicleState> vehicleStates = new ArrayList<VehicleState>();
        vehicleStates.add(new VehicleState("1", "car", new Position(1.0, 2.0, 3.0), new Position(4.0, 5.0, 6.0)));
        vehicleStates.add(new VehicleState("2", "truck", new Position(7.0, 8.0, 9.0), new Position(10.0, 11.0, 12.0)));

        // Create a Gson
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting(); // for human readability, otherwise without blanks/new lines
        Gson gson = gsonBuilder.create();

        // Convert list to Json
        String jsonString = gson.toJson(vehicleStates);
        System.out.println(jsonString);
        
        // Convert back to list
        List<VehicleState> vehicleStates2 = gson.fromJson(jsonString, MessageTypes.VEHICLE_STATES);
        System.out.println(vehicleStates2);
        
        // Check equals
        System.out.println("Are the lists equal? " + vehicleStates.equals(vehicleStates2));

    }

}
