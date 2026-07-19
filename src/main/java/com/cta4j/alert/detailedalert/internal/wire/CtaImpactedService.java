package com.cta4j.alert.detailedalert.internal.wire;

import com.cta4j.alert.common.internal.wire.CtaCdata;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaImpactedService(
    @JsonProperty("ServiceType") String serviceType,
    @JsonProperty("ServiceTypeDescription") String serviceTypeDescription,
    @JsonProperty("ServiceName") String serviceName,
    @JsonProperty("ServiceId") String serviceId,
    @JsonProperty("ServiceBackColor") String serviceBackColor,
    @JsonProperty("ServiceTextColor") String serviceTextColor,
    @JsonProperty("ServiceURL") CtaCdata serviceUrl
) {
    public CtaImpactedService {
        Objects.requireNonNull(serviceType);
        Objects.requireNonNull(serviceTypeDescription);
        Objects.requireNonNull(serviceName);
        Objects.requireNonNull(serviceId);
        Objects.requireNonNull(serviceBackColor);
        Objects.requireNonNull(serviceTextColor);
        Objects.requireNonNull(serviceUrl);
    }
}
