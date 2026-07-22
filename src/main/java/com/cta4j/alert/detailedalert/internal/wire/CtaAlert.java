package com.cta4j.alert.detailedalert.internal.wire;

import com.cta4j.alert.common.internal.wire.CtaCdata;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
@ApiStatus.Internal
@NullMarked
public record CtaAlert(
    @JsonProperty("AlertId")
    String alertId,

    @JsonProperty("Headline")
    String headline,

    @JsonProperty("ShortDescription")
    String shortDescription,

    @JsonProperty("FullDescription")
    CtaCdata fullDescription,

    @JsonProperty("SeverityScore")
    String severityScore,

    @JsonProperty("SeverityColor")
    String severityColor,

    @JsonProperty("SeverityCSS")
    String severityCss,

    @JsonProperty("Impact")
    String impact,

    @JsonProperty("EventStart")
    String eventStart,

    @JsonProperty("EventEnd")
    @Nullable
    String eventEnd,

    @JsonProperty("TBD")
    String tbd,

    @JsonProperty("MajorAlert")
    String majorAlert,

    @JsonProperty("AlertURL")
    CtaCdata alertUrl,

    @JsonProperty("ImpactedService")
    CtaImpactedServices impactedService,

    @JsonProperty("ttim")
    String ttim,

    @JsonProperty("GUID")
    String guid
) {
    public CtaAlert {
        Objects.requireNonNull(alertId);
        Objects.requireNonNull(headline);
        Objects.requireNonNull(shortDescription);
        Objects.requireNonNull(fullDescription);
        Objects.requireNonNull(severityScore);
        Objects.requireNonNull(severityColor);
        Objects.requireNonNull(severityCss);
        Objects.requireNonNull(impact);
        Objects.requireNonNull(eventStart);
        Objects.requireNonNull(tbd);
        Objects.requireNonNull(majorAlert);
        Objects.requireNonNull(alertUrl);
        Objects.requireNonNull(impactedService);
        Objects.requireNonNull(ttim);
        Objects.requireNonNull(guid);
    }
}
