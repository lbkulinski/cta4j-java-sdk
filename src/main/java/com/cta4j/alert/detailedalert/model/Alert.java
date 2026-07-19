package com.cta4j.alert.detailedalert.model;

import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

/**
 * Represents a detailed alert describing an event that affects one or more CTA services.
 *
 * @param id the unique ID of this alert (e.g., "115070")
 * @param headline the headline of this alert
 * @param shortDescription the short description of this alert
 * @param fullDescription the full description of this alert
 * @param severity the severity of this alert
 * @param impact the descriptive text of the impact this alert has on service (e.g., "Elevator Status",
 *               "Bus Stop Relocation", "Planned Reroute")
 * @param startTime the start time of this alert
 * @param endTime the end time of this alert, or {@code null} if not known
 * @param openEnded whether this alert is open-ended (has no known end time)
 * @param major whether this alert is of major significance
 * @param url the URL of this alert's detail page on transitchicago.com
 * @param impactedServices the services impacted by this alert
 */
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
    /**
     * Constructs an {@code Alert}.
     *
     * @param id the unique ID of the alert (e.g., "115070")
     * @param headline the headline of the alert
     * @param shortDescription the short description of the alert
     * @param fullDescription the full description of the alert
     * @param severity the severity of the alert
     * @param impact the descriptive text of the impact the alert has on service (e.g., "Elevator Status",
     *               "Bus Stop Relocation", "Planned Reroute")
     * @param startTime the start time of the alert
     * @param endTime the end time of the alert, or {@code null} if not known
     * @param openEnded whether the alert is open-ended (has no known end time)
     * @param major whether the alert is of major significance
     * @param url the URL of the alert's detail page on transitchicago.com
     * @param impactedServices the services impacted by the alert
     * @throws NullPointerException if {@code id}, {@code headline}, {@code shortDescription},
     * {@code fullDescription}, {@code severity}, {@code impact}, {@code startTime}, {@code url}, or
     * {@code impactedServices} is {@code null}, or if any element of {@code impactedServices} is {@code null}
     */
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
