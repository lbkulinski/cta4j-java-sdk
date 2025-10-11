package com.cta4j.model.bus;

import com.cta4j.external.bus.stop.CtaStop;

import java.math.BigDecimal;
import java.util.Objects;

public record Stop(
    String id,

    String name,

    BigDecimal latitude,

    BigDecimal longitude
) {
    public static Stop fromExternal(CtaStop stop) {
        Objects.requireNonNull(stop);

        return new Stop(
            stop.stpid(),
            stop.stpnm(),
            new BigDecimal(stop.lat()),
            new BigDecimal(stop.lon())
        );
    }
}
