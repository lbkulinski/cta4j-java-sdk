package com.cta4j.bus.vehicle.model;

import com.cta4j.bus.prediction.model.PassengerLoad;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents metadata associated with a vehicle.
 *
 * <p>
 *     <b>NOTE:</b> {@code stopStatus}, {@code timepointId}, {@code stopId}, {@code sequence}, {@code gtfsSequence},
 *     {@code serverTimestamp}, {@code speed}, and {@code block} are not well-documented by the CTA. As such, their
 *     presence here is primarily for completeness and may not be populated or described correctly.
 * </p>
 *
 * @param dataFeed the data feed from which this vehicle information was obtained, if applicable
 * @param lastUpdated the date and time (UTC) this vehicle information was last updated, if applicable
 * @param patternId the pattern identifier for the trip this vehicle is servicing
 * @param distanceToPatternPoint the number of feet this vehicle has traveled into the pattern currently being serviced
 * @param stopStatus the stop status of this vehicle, if applicable
 * @param timepointId the timepoint identifier associated with this vehicle, if applicable
 * @param stopId the stop identifier associated with this vehicle, if applicable
 * @param sequence the sequence number associated with this vehicle, if applicable
 * @param gtfsSequence the GTFS sequence number associated with this vehicle, if applicable
 * @param serverTimestamp the date and time (UTC) this vehicle information was received by the server, if applicable
 * @param speed the current speed of this vehicle in miles per hour, if applicable
 * @param block the block number for this vehicle, if applicable
 * @param blockId the scheduled block identifier for this vehicle
 * @param tripId the scheduled trip identifier for this vehicle
 * @param originalTripNumber the trip identifier for this vehicle
 * @param zone the zone name for this vehicle, otherwise blank
 * @param mode the {@link TransitMode} of this vehicle
 * @param passengerLoad the {@link PassengerLoad} of this vehicle
 * @param scheduledStartSeconds the scheduled start time in seconds past midnight associated with this vehicle, if
 *                              applicable
 * @param scheduledStartDate the scheduled start date associated with this vehicle, if applicable
 */
@NullMarked
public record VehicleMetadata(
    @Nullable
    String dataFeed,

    @Nullable
    Instant lastUpdated,

    int patternId,

    int distanceToPatternPoint,

    @Nullable
    Integer stopStatus,

    @Nullable
    Integer timepointId,

    @Nullable
    String stopId,

    @Nullable
    Integer sequence,

    @Nullable
    Integer gtfsSequence,

    @Nullable
    Instant serverTimestamp,

    @Nullable
    Integer speed,

    @Nullable
    Integer block,

    String blockId,

    String tripId,

    String originalTripNumber,

    String zone,

    TransitMode mode,

    PassengerLoad passengerLoad,

    @Nullable
    Integer scheduledStartSeconds,

    @Nullable
    LocalDate scheduledStartDate
) {
    /**
     * Constructs a {@code VehicleMetadata}.
     *
     * @param dataFeed the data feed from which the vehicle information was obtained, if applicable
     * @param lastUpdated the date and time (UTC) the vehicle information was last updated, if applicable
     * @param patternId the pattern identifier for the trip the vehicle is servicing
     * @param distanceToPatternPoint the number of feet the vehicle has traveled into the pattern currently being
     *                               serviced
     * @param stopStatus the stop status of the vehicle, if applicable
     * @param timepointId the timepoint identifier associated with the vehicle, if applicable
     * @param stopId the stop identifier associated with the vehicle, if applicable
     * @param sequence the sequence number associated with the vehicle, if applicable
     * @param gtfsSequence the GTFS sequence number associated with the vehicle, if applicable
     * @param serverTimestamp the date and time (UTC) the vehicle information was received by the server, if applicable
     * @param speed the current speed of the vehicle in miles per hour, if applicable
     * @param block the block number for the vehicle, if applicable
     * @param blockId the scheduled block identifier for the vehicle
     * @param tripId the scheduled trip identifier for the vehicle
     * @param originalTripNumber the trip identifier for the vehicle
     * @param zone the zone name for the vehicle, otherwise blank
     * @param mode the {@link TransitMode} of the vehicle
     * @param passengerLoad the {@link PassengerLoad} of the vehicle
     * @param scheduledStartSeconds the scheduled start time in seconds past midnight associated with the vehicle, if
     *                              applicable
     * @param scheduledStartDate the scheduled start date associated with the vehicle, if applicable
     * @throws NullPointerException if {@code blockId}, {@code tripId}, {@code originalTripNumber}, {@code zone},
     * {@code mode}, or {@code passengerLoad} is {@code null}
     */
    public VehicleMetadata {
        Objects.requireNonNull(blockId);
        Objects.requireNonNull(tripId);
        Objects.requireNonNull(originalTripNumber);
        Objects.requireNonNull(zone);
        Objects.requireNonNull(mode);
        Objects.requireNonNull(passengerLoad);
    }
}
