package com.cta4j.bus.stop.internal.wire;

import com.cta4j.bus.common.internal.wire.CtaError;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaStopError(
    String msg,
    @Nullable String rt,
    @Nullable String dir,
    @Nullable String stpid,
    @Nullable String rtpidatafeed
) implements CtaError {
    public CtaStopError {
        Objects.requireNonNull(msg);
    }

    @Override
    public boolean notFound() {
        return (this.rt != null && this.dir != null) || this.stpid != null;
    }
}
