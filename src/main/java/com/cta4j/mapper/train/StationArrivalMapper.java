package com.cta4j.mapper.train;

import com.cta4j.external.train.arrival.CtaArrivalsEta;
import com.cta4j.model.train.StationArrival;
import com.cta4j.util.DateTimeUtils;

import java.math.BigDecimal;
import java.util.Objects;

public final class StationArrivalMapper {
    private StationArrivalMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static StationArrival fromExternal(CtaArrivalsEta eta) {
        Objects.requireNonNull(eta);

        return new StationArrival(
            eta.staId(),
            eta.stpId(),
            eta.staNm(),
            eta.stpDe(),
            eta.rn(),
            RouteMapper.fromExternal(eta.rt()),
            eta.destSt(),
            eta.destNm(),
            Integer.parseInt(eta.trDr()),
            DateTimeUtils.parseTrainTimestamp(eta.prdt()),
            DateTimeUtils.parseTrainTimestamp(eta.arrT()),
            "1".equals(eta.isApp()),
            "1".equals(eta.isSch()),
            "1".equals(eta.isDly()),
            "1".equals(eta.isFlt()),
            eta.flags(),
            new BigDecimal(eta.lat()),
            new BigDecimal(eta.lon()),
            Integer.parseInt(eta.heading())
        );
    }
}
