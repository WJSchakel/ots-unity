package org.opentrafficsim.unity.message;

import java.io.Serializable;

/**
 * Segment describing the longitudinal dynamics of a vehicle over a period of constant acceleration.
 * @author wjschakel
 */
public class Segment implements Serializable
{
    /** */
    private static final long serialVersionUID = 20230419L;
    
    /** Segment duration [s]. */
    private final double duration;
    
    /** Segment acceleration [m/s2]. */
    private final double a;

    /**
     * Constructor.
     * @param duration double; segment duration [s].
     * @param a double; segment acceleration [m/s2].
     */
    public Segment(final double duration, final double a)
    {
        this.duration = duration;
        this.a = a;
    }
    
}
