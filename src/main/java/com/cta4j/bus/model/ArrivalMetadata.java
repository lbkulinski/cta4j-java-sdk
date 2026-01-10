package com.cta4j.bus.model;

public record ArrivalMetadata(
    String timestamp,

    int dynamic,

    String blockId,

    String tripId,

    String originalTripNumber,

    String zone,

    PassengerLoad passengerLoad,

    int gtfsSequence,

    String nextBus,

    Integer stopStatus,

    String stopStatusDescription,

    int flagStop
) {
}
