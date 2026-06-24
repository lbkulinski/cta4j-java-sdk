package com.cta4j.bus.prediction.internal.wire;

import com.cta4j.bus.common.internal.wire.CtaError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
@NullMarked
public record CtaPredictionError(
    String msg,
    @Nullable String stpid,
    @Nullable String vid,
    @Nullable String rtpidatafeed
) implements CtaError {
    public CtaPredictionError {
        Objects.requireNonNull(msg);
    }
}
