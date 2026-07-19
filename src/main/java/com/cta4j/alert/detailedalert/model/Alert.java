package com.cta4j.alert.detailedalert.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

@NullMarked
public record Alert(
    String id,
    String headline,
    String shortDescription,
    String fullDescription,
    Severity severity,
    String impact,
    Instant startTime,
    @Nullable Instant endTime,
    boolean openEnded,
    boolean major,
    URI url,
    List<ImpactedService> impactedServices
) {
    public Alert {
        Objects.requireNonNull(id);
        Objects.requireNonNull(headline);
        Objects.requireNonNull(shortDescription);
        Objects.requireNonNull(fullDescription);
        Objects.requireNonNull(severity);
        Objects.requireNonNull(impact);
        Objects.requireNonNull(startTime);
        Objects.requireNonNull(url);
        Objects.requireNonNull(impactedServices);

        impactedServices = List.copyOf(impactedServices);
    }
}
