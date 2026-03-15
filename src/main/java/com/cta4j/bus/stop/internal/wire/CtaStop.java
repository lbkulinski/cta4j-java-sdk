package com.cta4j.bus.stop.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@NullMarked
@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaStop(
    String stpid,

    String stpnm,

    @Nullable
    Double lat,

    @Nullable
    Double lon,

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
        Objects.requireNonNull(stpid);
        Objects.requireNonNull(stpnm);
        Objects.requireNonNull(lat);
        Objects.requireNonNull(lon);

        if (dtradd != null) {
            dtradd = List.copyOf(dtradd);
        }

        if (dtrrem != null) {
            dtrrem = List.copyOf(dtrrem);
        }
    }
}
