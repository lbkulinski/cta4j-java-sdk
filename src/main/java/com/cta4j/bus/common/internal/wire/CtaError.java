package com.cta4j.bus.common.internal.wire;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@ApiStatus.Internal
@NullMarked
public interface CtaError {
    String msg();
}
