package com.cta4j.mapper.bus;

import com.cta4j.external.bus.prediction.CtaPredictionsPrd;
import com.cta4j.model.bus.BusPredictionType;
import com.cta4j.model.bus.StopArrival;
import com.cta4j.util.DateTimeUtils;

import java.math.BigInteger;
import java.util.Objects;

public final class StopArrivalMapper {
    private StopArrivalMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static StopArrival fromExternal(CtaPredictionsPrd prd) {
        Objects.requireNonNull(prd);

        return new StopArrival(
            (prd.typ() == null) ? null :BusPredictionTypeMapper.fromExternal(prd.typ()),
            prd.stpnm(),
            prd.stpid(),
            prd.vid(),
            (prd.dstp() == null) ? null : new BigInteger(prd.dstp()),
            prd.rt(),
            prd.rtdd(),
            prd.rtdir(),
            prd.des(),
            (prd.prdtm() == null) ? null: DateTimeUtils.parseBusTimestamp(prd.prdtm()),
            (prd.dly() == null) ? null : Boolean.parseBoolean(prd.dly())
        );
    }
}
