package org.opentrafficsim.unity.message;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.reflect.TypeToken;

/**
 * Utility class that defines {@code Type}'s for use in {@code Gson.fromJson(String, Type)}.
 * @author wjschakel
 */
public final class MessageTypes
{

    /** Vehicle states type. */
    public static final Type VEHICLE_STATES = new TypeToken<List<VehicleState>>()
    {
    }.getType();

    /** New vehicle request type. */
    public static final Type NEW_VEHICLE_REQUEST = new TypeToken<NewVehicleRequest>()
    {
    }.getType();

    /** Private constructor for a utility class. */
    private MessageTypes()
    {
        //
    }

}
