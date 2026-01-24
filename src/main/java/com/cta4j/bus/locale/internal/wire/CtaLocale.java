package com.cta4j.bus.locale.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@NullMarked
@ApiStatus.Internal
@JsonIgnoreProperties(ignoreUnknown = true)
public record CtaLocale(
    String localestring,

    String displayname
) {
    public CtaLocale {
        Objects.requireNonNull(localestring);
        Objects.requireNonNull(displayname);
    }
}
