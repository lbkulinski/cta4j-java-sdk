package com.cta4j.bus.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings("ConstantConditions")
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaError(
    @Nullable
    String rtpidatafeed,

    @Nullable
    String rt,

    @Nullable
    String dir,

    String msg
) {
    public CtaError {
        if (msg == null) {
            throw new IllegalArgumentException("msg must not be null");
        }
    }
}
