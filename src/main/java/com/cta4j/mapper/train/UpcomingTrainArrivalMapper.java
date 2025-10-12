package com.cta4j.mapper.train;

import com.cta4j.external.train.follow.CtaFollowEta;
import com.cta4j.model.train.UpcomingTrainArrival;
import com.cta4j.util.DateTimeUtils;

import java.time.Instant;
import java.util.Objects;

public final class UpcomingTrainArrivalMapper {
    private UpcomingTrainArrivalMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static UpcomingTrainArrival fromExternal(CtaFollowEta eta) {
        Objects.requireNonNull(eta);

        return new UpcomingTrainArrival(
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
            eta.flags()
        );
    }
}
