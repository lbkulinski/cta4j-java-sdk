package com.cta4j.train.mapper;

import com.cta4j.train.external.follow.CtaFollowPosition;
import com.cta4j.train.model.TrainCoordinates;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

@ApiStatus.Internal
public final class TrainCoordinatesMapper {
    private static final Logger logger;

    static {
        logger = LoggerFactory.getLogger(TrainCoordinatesMapper.class);
    }

    private TrainCoordinatesMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static TrainCoordinates fromExternal(CtaFollowPosition position) {
        if (position == null) {
            throw new IllegalArgumentException("position must not be null");
        }

        BigDecimal latitude = null;

        if (position.lat() != null) {
            try {
                latitude = new BigDecimal(position.lat());
            } catch (NumberFormatException e) {
                logger.warn("Invalid latitude value {}", position.lat());
            }
        }

        BigDecimal longitude = null;

        if (position.lon() != null) {
            try {
                longitude = new BigDecimal(position.lon());
            } catch (NumberFormatException e) {
                logger.warn("Invalid longitude value {}", position.lon());
            }
        }

        Integer heading = null;

        if (position.heading() != null) {
            try {
                heading = Integer.parseInt(position.heading());
            } catch (NumberFormatException e) {
                logger.warn("Invalid heading value {}", position.heading());
            }
        }

        return new TrainCoordinates(
            latitude,
            longitude,
            heading
        );
    }
}
