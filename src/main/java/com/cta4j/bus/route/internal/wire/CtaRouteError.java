package com.cta4j.bus.route.internal.wire;

import com.cta4j.bus.common.internal.wire.CtaError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
@NullMarked
public record CtaRouteError(
    @Nullable String rtpidatafeed,
    String msg
) implements CtaError {
    public CtaRouteError {
        Objects.requireNonNull(msg);
    }
}
