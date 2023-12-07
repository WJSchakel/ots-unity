package org.opentrafficsim.unity.message;

import java.io.Serializable;

import org.djunits.value.vdouble.scalar.Duration;
import org.djunits.value.vdouble.scalar.Speed;
import org.djunits.value.vdouble.scalar.Time;
import org.djutils.exceptions.Try;
import org.djutils.immutablecollections.ImmutableList;
import org.opentrafficsim.core.geometry.OtsPoint3d;
import org.opentrafficsim.core.gtu.plan.operational.OperationalPlan;
import org.opentrafficsim.road.gtu.lane.LaneBasedGtu;

/**
 * Message to request a vehicle update in Unity.
 * @author wjschakel
 */
public class UpdateVehicleRequest implements Serializable
{
    /** */
    private static final long serialVersionUID = 20230419L;

    /** GTU id. */
    private String id;

    /** Start time. */
    private double startTime;

    /** Start speed. */
    private double startSpeed;

    /** Path. */
    private Position[] path;

    /** Longitudinal dynamics segments. */
    private Segment[] segments;

    /** Indicator status; NONE, LEFT, RIGHT, HAZARD, NOTPRESENT. */
    private String indicator;

    /** Brake lights on or off. */
    private boolean brakeLighs;

    /**
     * Private constructor for static creation method.
     */
    private UpdateVehicleRequest()
    {

    }

    /**
     * Constructor.
     * @param id
     * @param startTime
     * @param startSpeed
     * @param path
     * @param segments
     * @param indicator
     * @param brakeLighs
     */
    public UpdateVehicleRequest(final String id, final Time startTime, final Speed startSpeed, final Position[] path,
            final Segment[] segments, final String indicator, final boolean brakeLighs)
    {
        this.id = id;
        this.startTime = startTime.si;
        this.startSpeed = startSpeed.si;
        this.path = path;
        this.segments = segments;
        this.indicator = indicator;
        this.brakeLighs = brakeLighs;
    }

    /**
     * Returns an update vehicle request based on the current operational plan of the GTU.
     * @param gtu LaneBasedGtu; GTU.
     * @return UpdateVehicleRequest; request based on the current operational plan of the GTU.
     */
    public static UpdateVehicleRequest from(final LaneBasedGtu gtu)
    {
        OperationalPlan plan = gtu.getOperationalPlan();
        Time startTime = plan.getStartTime();
        UpdateVehicleRequest request = new UpdateVehicleRequest();
        request.id = gtu.getId();
        request.startTime = startTime.si;
        request.startSpeed = plan.getStartSpeed().si;

        // path
        OtsPoint3d[] points = plan.getPath().getPoints();
        request.path = new Position[points.length];
        for (int i = 0; i < points.length; i++)
        {
            request.path[i] = new Position(points[i].x, points[i].y, points[i].z);
        }

        // segments
        ImmutableList<org.opentrafficsim.core.gtu.plan.operational.Segment> segments = plan.getOperationalPlanSegmentList();
        Duration since = Duration.ZERO;
        request.segments = new Segment[segments.size()];
        for (int i = 0; i < segments.size(); i++)
        {
            org.opentrafficsim.core.gtu.plan.operational.Segment segment = segments.get(i);
            double a = Try.assign(() -> plan.getAcceleration(since).si, RuntimeException.class,
                    "Operational plan is not consistent.");
            since.plus(segment.getDuration());
            request.segments[i] = new Segment(segment.getDuration().si, a);
        }

        request.indicator = gtu.getTurnIndicatorStatus(startTime).toString();
        request.brakeLighs = gtu.isBrakingLightsOn(startTime);
        return request;
    }

}
