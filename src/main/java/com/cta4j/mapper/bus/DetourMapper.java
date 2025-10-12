package com.cta4j.mapper.bus;

import com.cta4j.external.bus.detour.CtaDetour;
import com.cta4j.model.bus.Detour;
import com.cta4j.model.bus.DetourRouteDirection;
import com.cta4j.util.DateTimeUtils;

import java.util.Objects;

public final class DetourMapper {
    private DetourMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

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
