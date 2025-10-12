package com.cta4j.mapper.train;

import com.cta4j.external.train.arrival.CtaArrivalsEta;
import com.cta4j.model.train.Route;
import com.cta4j.model.train.StationArrival;
import com.cta4j.util.DateTimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

public final class StationArrivalMapper {
    private static final Logger logger;

    static {
        logger = LoggerFactory.getLogger(StationArrivalMapper.class);
    }

    private StationArrivalMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static StationArrival fromExternal(CtaArrivalsEta eta) {
        Objects.requireNonNull(eta);

        Route route = null;

        if (eta.rt() != null) {
            try {
                route = RouteMapper.fromExternal(eta.rt());
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid route {}", eta.rt());
            }
        }

        Integer direction = null;

        if (eta.trDr() != null) {
            try {
                direction = Integer.parseInt(eta.trDr());
            } catch (NumberFormatException e) {
                logger.warn("Invalid direction value {}", eta.trDr());
            }
        }

        Instant predictionTime = null;

        if (eta.prdt() != null) {
            try {
                predictionTime = DateTimeUtils.parseTrainTimestamp(eta.prdt());
            } catch (Exception e) {
                logger.warn("Invalid prediction time value {}", eta.prdt());
            }
        }

        Instant arrivalTime = null;

        if (eta.arrT() != null) {
            try {
                arrivalTime = DateTimeUtils.parseTrainTimestamp(eta.arrT());
            } catch (Exception e) {
                logger.warn("Invalid arrival time value {}", eta.arrT());
            }
        }

        Boolean approaching = null;

        if (eta.isApp() != null) {
            approaching = "1".equals(eta.isApp());
        }

        Boolean scheduled = null;

        if (eta.isSch() != null) {
            scheduled = "1".equals(eta.isSch());
        }

        Boolean delayed = null;

        if (eta.isDly() != null) {
            delayed = "1".equals(eta.isDly());
        }

        Boolean faulted = null;

        if (eta.isFlt() != null) {
            faulted = "1".equals(eta.isFlt());
        }

        BigDecimal latitude = null;

        if (eta.lat() != null) {
            try {
                latitude = new BigDecimal(eta.lat());
            } catch (NumberFormatException e) {
                logger.warn("Invalid latitude value {}", eta.lat());
            }
        }

        BigDecimal longitude = null;

        if (eta.lon() != null) {
            try {
                longitude = new BigDecimal(eta.lon());
            } catch (NumberFormatException e) {
                logger.warn("Invalid longitude value {}", eta.lon());
            }
        }

        Integer heading = null;

        if (eta.heading() != null) {
            try {
                heading = Integer.parseInt(eta.heading());
            } catch (NumberFormatException e) {
                logger.warn("Invalid heading value {}", eta.heading());
            }
        }

        return new StationArrival(
            eta.staId(),
            eta.stpId(),
            eta.staNm(),
            eta.stpDe(),
            eta.rn(),
            route,
            eta.destSt(),
            eta.destNm(),
            direction,
            predictionTime,
            arrivalTime,
            approaching,
            scheduled,
            delayed,
            faulted,
            eta.flags(),
            latitude,
            longitude,
            heading
        );
    }
}
