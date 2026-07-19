package com.cta4j.alert.detailedalert.model;

import com.cta4j.alert.common.model.ServiceType;
import org.jspecify.annotations.NullMarked;

import java.net.URI;
import java.util.Objects;

@NullMarked
public record ImpactedService(
    ServiceType type,
    String name,
    String serviceId,
    String color,
    String textColor,
    URI url
) {
    public ImpactedService {
        Objects.requireNonNull(type);
        Objects.requireNonNull(name);
        Objects.requireNonNull(serviceId);
        Objects.requireNonNull(color);
        Objects.requireNonNull(textColor);
        Objects.requireNonNull(url);
    }
}
