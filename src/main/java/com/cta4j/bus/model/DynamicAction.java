package com.cta4j.bus.model;

public enum DynamicAction {
    NONE(0),

    CANCELLED(1),

    REASSIGNED(2),

    SHIFTED(3),

    EXPRESSED(4),

    STOPS_AFFECTED(6),

    NEW_TRIP(8),

    PARTIAL_TRIP(9),

    PARTIAL_TRIP_NEW(10),

    DELAYED_CANCEL(12),

    ADDED_STOP(13),

    UNKNOWN_DELAY(14),

    UNKNOWN_DELAY_NEW(15),

    INVALIDATED_TRIP(16),

    INVALIDATED_TRIP_NEW(17),

    CANCELLED_TRIP_NEW(18),

    STOPS_AFFECTED_NEW(19);

    private final int id;

    DynamicAction(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static DynamicAction fromId(int id) {
        for (DynamicAction action : DynamicAction.values()) {
            if (action.id == id) {
                return action;
            }
        }

        throw new IllegalArgumentException("Invalid dynamic action id: " + id);
    }
}
