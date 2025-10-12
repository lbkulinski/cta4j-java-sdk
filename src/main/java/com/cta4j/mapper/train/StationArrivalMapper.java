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
            (eta.rt() == null) ? null : RouteMapper.fromExternal(eta.rt()),
            eta.destSt(),
            eta.destNm(),
            (eta.trDr() == null) ? null : Integer.parseInt(eta.trDr()),
            (eta.prdt() == null) ? null : DateTimeUtils.parseTrainTimestamp(eta.prdt()),
            (eta.arrT() == null) ? null : DateTimeUtils.parseTrainTimestamp(eta.arrT()),
            (eta.isApp() == null) ? null : "1".equals(eta.isApp()),
            (eta.isSch() == null) ? null : "1".equals(eta.isSch()),
            (eta.isDly() == null) ? null : "1".equals(eta.isDly()),
            (eta.isFlt() == null) ? null : "1".equals(eta.isFlt()),
            eta.flags(),
            (eta.lat() == null) ? null : new BigDecimal(eta.lat()),
            (eta.lon() == null) ? null : new BigDecimal(eta.lon()),
            (eta.heading() == null) ? null : Integer.parseInt(eta.heading())
        );
    }
}
