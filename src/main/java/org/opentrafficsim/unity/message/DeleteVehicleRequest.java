package org.opentrafficsim.unity.message;

import java.io.Serializable;

/**
 * Message to request a deleted vehicle in Unity.
 * @author wjschakel
 */
public class DeleteVehicleRequest implements Serializable
{
    /** */
    private static final long serialVersionUID = 20230419L;

    /** Id. */
    private String id;
    
    /**
     * Constructor.
     * @param id String; id.
     */
    public DeleteVehicleRequest(final String id) 
    {
        this.id = id;
    }
}
