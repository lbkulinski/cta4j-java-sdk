package com.cta4j.bus.api.stop.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;

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
    public CtaStop(
        @Nullable String stpid,
        @Nullable String stpnm,
        double lat,
        double lon,
        @Nullable List<@Nullable Integer> dtradd,
        @Nullable List<@Nullable Integer> dtrrem,
        @Nullable Integer gtfsseq,
        @Nullable Boolean ada
    ) {
        if (stpid == null) {
            throw new IllegalArgumentException("stpid must not be null");
        }

        if (stpnm == null) {
            throw new IllegalArgumentException("stpnm must not be null");
        }

        List<Integer> dtraddCopy = null;

        if (dtradd != null) {
            for (Integer detourId : dtradd) {
                if (detourId == null) {
                    throw new IllegalArgumentException("dtradd must not contain null values");
                }
            }

            dtraddCopy = List.copyOf(dtradd);
        }

        List<Integer> dtrremCopy = null;

        if (dtrrem != null) {
            for (Integer detourId : dtrrem) {
                if (detourId == null) {
                    throw new IllegalArgumentException("dtrrem must not contain null values");
                }
            }

            dtrremCopy = List.copyOf(dtrrem);
        }

        this.stpid = stpid;
        this.stpnm = stpnm;
        this.lat = lat;
        this.lon = lon;
        this.dtradd = dtraddCopy;
        this.dtrrem = dtrremCopy;
        this.gtfsseq = gtfsseq;
        this.ada = ada;
    }
}
