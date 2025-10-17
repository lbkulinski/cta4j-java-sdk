package com.cta4j.mapper.bus;

import com.cta4j.external.bus.detour.CtaDetour;
import com.cta4j.model.bus.Detour;
import com.cta4j.model.bus.DetourRouteDirection;
import com.cta4j.util.DateTimeUtils;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

@ApiStatus.Internal
public final class DetourMapper {
    private static final Logger logger;

    static {
        logger = LoggerFactory.getLogger(DetourMapper.class);
    }

    private DetourMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Detour fromExternal(CtaDetour detour) {
        Objects.requireNonNull(detour);

        Boolean active = null;

        if (detour.st() != null) {
            active = "1".equals(detour.st());
        }

        List<DetourRouteDirection> routeDirections = null;

        if (detour.rtdirs() != null) {
            routeDirections = detour.rtdirs()
                                    .stream()
                                    .map(rd -> new DetourRouteDirection(rd.rt(), rd.dir()))
                                    .toList();
        }

        Instant startTime = null;

        if (detour.startdt() != null) {
            try {
                startTime = DateTimeUtils.parseBusTimestamp(detour.startdt());
            } catch (DateTimeParseException e) {
                logger.warn("Invalid start time value {}", detour.startdt());
            }
        }

        Instant endTime = null;

        if (detour.enddt() != null) {
            try {
                endTime = DateTimeUtils.parseBusTimestamp(detour.enddt());
            } catch (DateTimeParseException e) {
                logger.warn("Invalid end time value {}", detour.enddt());
            }
        }

        return new Detour(
            detour.id(),
            detour.ver(),
            active,
            detour.desc(),
            routeDirections,
            startTime,
            endTime
        );
    }
}
