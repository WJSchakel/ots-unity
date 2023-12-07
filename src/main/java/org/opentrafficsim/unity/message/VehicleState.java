package org.opentrafficsim.unity.message;

/**
 * Class to store a vehicle state.
 * @author wjschakel
 */
public class VehicleState
{

    /** Vehicle id. */
    private String id;

    /** Vehicle type. */
    private String type;

    /** Vehicle position. */
    private Position position;

    /** Vehicle rotation. */
    private Position rotation;

    /**
     * Constructor.
     * @param id String; id.
     * @param type String; type, e.g. for car, color, etc.
     * @param position Position; position [m]
     * @param rotation Position; rotation [degrees]
     */
    public VehicleState(final String id, final String type, final Position position, final Position rotation)
    {
        this.id = id;
        this.type = type;
        this.position = position;
        this.rotation = rotation;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((position == null) ? 0 : position.hashCode());
        result = prime * result + ((rotation == null) ? 0 : rotation.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
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
        VehicleState other = (VehicleState) obj;
        if (id == null)
        {
            if (other.id != null)
            {
                return false;
            }
        }
        else if (!id.equals(other.id))
        {
            return false;
        }
        if (position == null)
        {
            if (other.position != null)
            {
                return false;
            }
        }
        else if (!position.equals(other.position))
        {
            return false;
        }
        if (rotation == null)
        {
            if (other.rotation != null)
            {
                return false;
            }
        }
        else if (!rotation.equals(other.rotation))
        {
            return false;
        }
        if (type == null)
        {
            if (other.type != null)
            {
                return false;
            }
        }
        else if (!type.equals(other.type))
        {
            return false;
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public String toString()
    {
        return "VehicleState [id=" + id + ", type=" + type + ", position=" + position + ", rotation=" + rotation + "]";
    }

}
