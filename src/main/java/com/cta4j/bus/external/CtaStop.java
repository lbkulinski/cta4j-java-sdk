package com.cta4j.bus.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

@NullMarked
@ApiStatus.Internal
@SuppressWarnings("ConstantConditions")
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaStop(
    String stpid,

    String stpnm,

    double lat,

    double lon,

    @Nullable
    List<Integer> dtradd,

    @Nullable
    List<Integer> dtrrem,

    @Nullable
    Integer gtfsseq,

    @Nullable
    Boolean ada
) {
    public CtaStop {
        if (stpid == null) {
            throw new IllegalArgumentException("stpid must not be null");
        }

        if (stpnm == null) {
            throw new IllegalArgumentException("stpnm must not be null");
        }
    }
}
