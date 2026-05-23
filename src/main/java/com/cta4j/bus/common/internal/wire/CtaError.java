package com.cta4j.bus.common.internal.wire;

import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

@NullMarked
@ApiStatus.Internal
public interface CtaError {
    String msg();
}
