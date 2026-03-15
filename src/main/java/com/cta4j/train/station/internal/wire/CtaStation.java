package com.cta4j.train.station.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaStation(
    @JsonProperty("stop_id")
    String stopId,

    @JsonProperty("direction_id")
    String directionId,

    @JsonProperty("stop_name")
    String stopName,

    @JsonProperty("station_name")
    String stationName,

    @JsonProperty("station_descriptive_name")
    String stationDescriptiveName,

    @JsonProperty("map_id")
    String mapId,

    boolean ada,

    boolean red,

    boolean blue,

    boolean g,

    boolean brn,

    boolean p,

    boolean y,

    boolean pnk,

    boolean o,

    CtaLocation location
) {
    public CtaStation {
        Objects.requireNonNull(stopId);
        Objects.requireNonNull(directionId);
        Objects.requireNonNull(stopName);
        Objects.requireNonNull(stationName);
        Objects.requireNonNull(stationDescriptiveName);
        Objects.requireNonNull(mapId);
        Objects.requireNonNull(location);
    }
}
