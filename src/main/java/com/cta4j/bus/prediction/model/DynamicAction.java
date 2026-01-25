package com.cta4j.bus.prediction.model;

/**
 * Represents the various dynamic actions that can be applied to a bus trip.
 */
public enum DynamicAction {
    /**
     * Indicates that no dynamic action has been applied.
     */
    NONE(0),

    /**
     * Indicates that the event or trip has been canceled.
     */
    CANCELLED(1),

    /**
     * Indicates that the event or trip will be handled by a different vehicle or operator.
     */
    REASSIGNED(2),

    /**
     * Indicates that the time of the event, or the entire trip, has been moved.
     */
    SHIFTED(3),

    /**
     * Indicates that the event is “drop-off only” and will not stop to pick up passengers.
     */
    EXPRESSED(4),

    /**
     * Indicates that the trip has events that are affected by Disruption Management changes, but the trip itself is
     * not affected.
     */
    STOPS_AFFECTED(6),

    /**
     * Indicates that the trip was created dynamically and does not appear in the TA schedule.
     */
    NEW_TRIP(8),

    /**
     * Indicates one of the following:
     * <ul>
     *     <li>
     *         The trip has been split, and this part of the split is using the original trip identifier(s).
     *     </li>
     *      <li>
     *          The trip has been short-turned leading to the removal of short-turned stops from the trip resulting in
     *          the trip being partial.
     *      </li>
     * </ul>
     */
    PARTIAL_TRIP(9),

    /**
     * Indicates the trip has been split, and this part of the split has been assigned a new trip identifier(s).
     */
    PARTIAL_TRIP_NEW(10),

    /**
     * Indicates that the event or trip has been marked as canceled, but the cancellation should not be shown to the
     * public.
     */
    DELAYED_CANCEL(12),

    /**
     * Indicates that event has been added to the trip. It was not originally scheduled.
     */
    ADDED_STOP(13),

    /**
     * Indicates that the trip has been affected by a delay.
     */
    UNKNOWN_DELAY(14),

    /**
     * Indicates that the trip, which was created dynamically, has been affected by a delay.
     */
    UNKNOWN_DELAY_NEW(15),

    /**
     * Indicates that the trip has been invalidated. Predictions for it should not be shown to the public.
     */
    INVALIDATED_TRIP(16),

    /**
     * Indicates that the trip, which was created dynamically, has been invalidated. Predictions for it should not be
     * shown to the public.
     */
    INVALIDATED_TRIP_NEW(17),

    /**
     * Indicates that the trip, which was created dynamically, has been canceled.
     */
    CANCELLED_TRIP_NEW(18),

    /**
     * Indicates that the trip, which was created dynamically, has events that are affected by Disruption Management
     * changes, but the trip itself is not affected.
     */
    STOPS_AFFECTED_NEW(19);

    private final int code;

    DynamicAction(int code) {
        this.code = code;
    }

    /**
     * Gets the code associated with this dynamic action.
     *
     * @return the dynamic action code
     */
    public int getCode() {
        return this.code;
    }
}
