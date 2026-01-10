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
public record CtaDetour(
    String id,

    int ver,

    int st,

    String desc,

    List<CtaDetoursRouteDirection> rtdirs,

    String startdt,

    String enddt,

    @Nullable
    String rtpidatafeed
) {
    public CtaDetour {
        if (id == null) {
            throw new IllegalArgumentException("id must not be null");
        }

        if (desc == null) {
            throw new IllegalArgumentException("desc must not be null");
        }

        if (rtdirs == null) {
            throw new IllegalArgumentException("rtdirs must not be null");
        }

        if (startdt == null) {
            throw new IllegalArgumentException("startdt must not be null");
        }

        if (enddt == null) {
            throw new IllegalArgumentException("enddt must not be null");
        }

        for (CtaDetoursRouteDirection rtdir : rtdirs) {
            if (rtdir == null) {
                throw new IllegalArgumentException("rtdirs must not contain null elements");
            }
        }
    }
}
