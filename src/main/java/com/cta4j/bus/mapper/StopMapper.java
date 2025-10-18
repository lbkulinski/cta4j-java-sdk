package com.cta4j.bus.mapper;

import com.cta4j.bus.external.stop.CtaStop;
import com.cta4j.bus.model.Stop;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Objects;

@ApiStatus.Internal
public final class StopMapper {
    private static final Logger logger;

    static {
        logger = LoggerFactory.getLogger(StopMapper.class);
    }

    private StopMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Stop fromExternal(CtaStop stop) {
        Objects.requireNonNull(stop);

        BigDecimal latitude = null;

        if (stop.lat() != null) {
            try {
                latitude = new BigDecimal(stop.lat());
            } catch (NumberFormatException e) {
                logger.warn("Invalid latitude value {}", stop.lat());
            }
        }

        BigDecimal longitude = null;

        if (stop.lon() != null) {
            try {
                longitude = new BigDecimal(stop.lon());
            } catch (NumberFormatException e) {
                logger.warn("Invalid longitude value {}", stop.lon());
            }
        }

        return new Stop(
            stop.stpid(),
            stop.stpnm(),
            latitude,
            longitude
        );
    }
}
