package com.cta4j.mapper.train;

import com.cta4j.external.train.follow.CtaFollowEta;
import com.cta4j.model.train.UpcomingTrainArrival;

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
            RouteMapper.fromExternal(eta.rt()),
            eta.destSt(),
            eta.destNm(),
            Integer.parseInt(eta.trDr()),
            Instant.ofEpochMilli(Long.parseLong(eta.prdt())),
            Instant.ofEpochMilli(Long.parseLong(eta.arrT())),
            "1".equals(eta.isApp()),
            "1".equals(eta.isSch()),
            "1".equals(eta.isDly()),
            "1".equals(eta.isFlt()),
            eta.flags()
        );
    }
}
