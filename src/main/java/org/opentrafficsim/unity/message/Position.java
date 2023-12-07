package org.opentrafficsim.unity.message;

import java.io.Serializable;

/**
 * Class to store 3 values, for either a position or a rotation.
 * @author wjschakel
 */
public class Position implements Serializable
{
    /** */
    private static final long serialVersionUID = 20230419L;

    /** X-coordinate [m]. */
    private final double x;

    /** Y-coordinate [m]. */
    private final double y;

    /** Z-coordinate [m]. */
    private final double z;

    /**
     * Constructor.
     * @param x double; x-coodintate.
     * @param y double; y-coodintate.
     * @param z double; z-coodintate.
     */
    public Position(final double x, final double y, final double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(z);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        Position other = (Position) obj;
        if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
        {
            return false;
        }
        if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
        {
            return false;
        }
        if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
        {
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "Position [x=" + x + ", y=" + y + ", z=" + z + "]";
    }

}
