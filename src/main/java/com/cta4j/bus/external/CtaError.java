package com.cta4j.bus.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings("ConstantConditions")
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaError(
    String msg
) {
    public CtaError {
        if (msg == null) {
            throw new IllegalArgumentException("msg must not be null");
        }
    }
}
