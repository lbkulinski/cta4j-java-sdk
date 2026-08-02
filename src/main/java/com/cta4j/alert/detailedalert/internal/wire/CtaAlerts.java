package com.cta4j.alert.detailedalert.internal.wire;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaAlerts(
    @JsonProperty("TimeStamp")
    String timestamp,

    @JsonProperty("ErrorCode")
    String errorCode,

    @JsonProperty("ErrorMessage")
    @Nullable
    String errorMessage,

    @JsonProperty("Alert")
    @Nullable
    List<CtaAlert> alert
) {
    public CtaAlerts {
        Objects.requireNonNull(timestamp);
        Objects.requireNonNull(errorCode);

        if (alert != null) {
            alert = List.copyOf(alert);
        }
    }
}
