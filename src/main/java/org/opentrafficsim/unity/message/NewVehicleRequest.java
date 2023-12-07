package org.opentrafficsim.unity.message;

import java.io.Serializable;

/**
 * Message to request a new vehicle in Unity.
 * @author wjschakel
 */
public class NewVehicleRequest implements Serializable
{
    /** */
    private static final long serialVersionUID = 20230419L;

    /** Id. */
    private String id;
    
    /** Vehicle type, "Car" or "Truck". */
    private String type;
    
    /** Port for communication. */
    private int port;
    
    /**
     * Constructor.
     * @param id String; id.
     * @param type String; vehicle type, "Car" or "Truck".
     * @param port int; port for communication.
     */
    public NewVehicleRequest(final String id, final String type, final int port) 
    {
        this.id = id;
        this.type = type;
        this.port = port;
    }
}
