package com.cta4j.bus.api.stop.external;

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
        Objects.requireNonNull(stpid);
        Objects.requireNonNull(stpnm);

        if (dtradd != null) {
            dtradd.forEach(Objects::requireNonNull);

            dtradd = List.copyOf(dtradd);
        }

        if (dtrrem != null) {
            dtrrem.forEach(Objects::requireNonNull);

            dtrrem = List.copyOf(dtrrem);
        }
    }
}
