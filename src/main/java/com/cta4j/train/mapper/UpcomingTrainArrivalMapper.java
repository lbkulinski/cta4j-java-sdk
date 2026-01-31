package com.cta4j.train.mapper;

import com.cta4j.train.external.follow.CtaFollowEta;
import com.cta4j.train.model.Route;
import com.cta4j.train.model.UpcomingTrainArrival;
import com.cta4j.util.DateTimeUtils;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.Objects;

@ApiStatus.Internal
public final class UpcomingTrainArrivalMapper {
    private static final Logger logger;

    static {
        logger = LoggerFactory.getLogger(UpcomingTrainArrivalMapper.class);
    }

    private UpcomingTrainArrivalMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static UpcomingTrainArrival fromExternal(CtaFollowEta eta) {
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

        return new UpcomingTrainArrival(
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
            eta.flags()
        );
    }
}
