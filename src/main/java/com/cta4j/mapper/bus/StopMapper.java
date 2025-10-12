package com.cta4j.mapper.bus;

import com.cta4j.external.bus.stop.CtaStop;
import com.cta4j.model.bus.Stop;

import java.math.BigDecimal;
import java.util.Objects;

public final class StopMapper {
    private StopMapper() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

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
