package com.cta4j.model.bus;

import com.cta4j.external.bus.detour.CtaDetour;
import com.cta4j.util.DateTimeUtils;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public record Detour(
    String id,

    int version,

    boolean active,

    String description,

    List<DetourRouteDirection> routeDirections,

    Instant startTime,

    Instant endTime
) {
    public static Detour fromExternal(CtaDetour detour) {
        Objects.requireNonNull(detour);

        return new Detour(
            detour.id(),
            detour.ver(),
            detour.st() == 1,
            detour.desc(),
            detour.rtdirs()
                  .stream()
                  .map(rd -> new DetourRouteDirection(rd.rt(), rd.dir()))
                  .toList(),
            DateTimeUtils.parseBusTimestamp(detour.startdt()),
            DateTimeUtils.parseBusTimestamp(detour.enddt())
        );
    }
}
