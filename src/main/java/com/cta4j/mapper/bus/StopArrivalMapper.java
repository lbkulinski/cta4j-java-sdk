package com.cta4j.mapper.bus;

import com.cta4j.external.bus.prediction.CtaPredictionsPrd;
import com.cta4j.model.bus.BusPredictionType;
import com.cta4j.model.bus.StopArrival;
import com.cta4j.util.DateTimeUtils;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@ApiStatus.Internal
public final class StopArrivalMapper {
    private static final Logger logger;

    static {
        logger = LoggerFactory.getLogger(StopArrivalMapper.class);
    }

    private StopArrivalMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static StopArrival fromExternal(CtaPredictionsPrd prd) {
        Objects.requireNonNull(prd);

        BusPredictionType type = null;

        if (prd.typ() != null) {
            try {
                type = BusPredictionTypeMapper.fromExternal(prd.typ());
            } catch (IllegalArgumentException e) {
                logger.warn("Invalid bus prediction type {}", prd.typ());
            }
        }

        BigInteger distanceToStop = null;

        if (prd.dstp() != null) {
            try {
                distanceToStop = new BigInteger(prd.dstp());
            } catch (NumberFormatException e) {
                logger.warn("Invalid distance to stop value {}", prd.dstp());
            }
        }

        Instant arrivalTime = null;

        if (prd.prdtm() != null) {
            try {
                arrivalTime = DateTimeUtils.parseBusTimestamp(prd.prdtm());
            } catch (DateTimeParseException e) {
                logger.warn("Invalid arrival time value {}", prd.prdtm());
            }
        }

        Boolean delayed = null;

        if (prd.dly() != null) {
            delayed = Boolean.parseBoolean(prd.dly());
        }

        return new StopArrival(
            type,
            prd.stpnm(),
            prd.stpid(),
            prd.vid(),
            distanceToStop,
            prd.rt(),
            prd.rtdd(),
            prd.rtdir(),
            prd.des(),
            arrivalTime,
            delayed
        );
    }
}
