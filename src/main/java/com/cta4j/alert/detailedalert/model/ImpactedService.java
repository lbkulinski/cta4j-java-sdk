package com.cta4j.alert.detailedalert.model;

import com.cta4j.alert.common.model.ServiceType;
import org.jspecify.annotations.NullMarked;

import java.net.URI;
import java.util.Objects;

/**
 * Represents a single service - a bus route, train route, train station, or systemwide grouping - impacted by
 * an alert.
 *
 * @param type the type of service this represents
 * @param name the name of this service (e.g., "Clark", "Red Line", "Jackson", "All Bus Routes")
 * @param serviceId the identifier of this service; matches GTFS route or station IDs, except for systemwide groupings,
 *                  which use a fixed identifier instead (e.g., "22", "Red", "Systemwide")
 * @param color the color of this service used in maps; length and casing vary (e.g., "059", "565a5c")
 * @param textColor the suggested color of text displayed against {@code color}; casing varies (e.g., "ffffff",
 *                  "FFFFFF")
 * @param url the URL of this service's page on transitchicago.com
 */
@NullMarked
public record ImpactedService(
    ServiceType type,
    String name,
    String serviceId,
    String color,
    String textColor,
    URI url
) {
    /**
     * Constructs an {@code ImpactedService}.
     *
     * @param type the type of service the impacted service represents
     * @param name the name of the service (e.g., "Clark", "Red Line", "Jackson", "All Bus Routes")
     * @param serviceId the identifier of the service; matches GTFS route or station IDs, except for systemwide
     *                  groupings, which use a fixed identifier instead (e.g., "22", "Red", "Systemwide")
     * @param color the color of the service used in maps; length and casing vary (e.g., "059", "565a5c")
     * @param textColor the suggested color of text displayed against {@code color}; casing varies (e.g., "ffffff",
     *                  "FFFFFF")
     * @param url the URL of the service's page on transitchicago.com
     * @throws NullPointerException if {@code type}, {@code name}, {@code serviceId}, {@code color},
     * {@code textColor}, or {@code url} is {@code null}
     */
    public ImpactedService {
        Objects.requireNonNull(type);
        Objects.requireNonNull(name);
        Objects.requireNonNull(serviceId);
        Objects.requireNonNull(color);
        Objects.requireNonNull(textColor);
        Objects.requireNonNull(url);
    }
}
