package com.cta4j.bus.mapper;

import com.cta4j.bus.external.vehicle.CtaVehicle;
import com.cta4j.bus.model.Bus;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Objects;

@ApiStatus.Internal
public final class BusMapper {
    private static final Logger logger;

    static {
        logger = LoggerFactory.getLogger(BusMapper.class);
    }

    private BusMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static Bus fromExternal(CtaVehicle vehicle) {
        Objects.requireNonNull(vehicle);

        BigDecimal latitude = null;

        if (vehicle.lat() != null) {
            try {
                latitude = new BigDecimal(vehicle.lat());
            } catch (NumberFormatException e) {
                logger.warn("Invalid latitude value {}", vehicle.lat());
            }
        }

        BigDecimal longitude = null;

        if (vehicle.lon() != null) {
            try {
                longitude = new BigDecimal(vehicle.lon());
            } catch (NumberFormatException e) {
                logger.warn("Invalid longitude value {}", vehicle.lon());
            }
        }

        Integer heading = null;

        if (vehicle.hdg() != null) {
            try {
                heading = Integer.parseInt(vehicle.hdg());
            } catch (NumberFormatException e) {
                logger.warn("Invalid heading value {}", vehicle.hdg());
            }
        }

        return new Bus(
            vehicle.vid(),
            latitude,
            longitude,
            heading,
            vehicle.rt(),
            vehicle.des(),
            vehicle.dly()
        );
    }
}
