package com.cta4j.alert.detailedalert.model;

import com.cta4j.alert.common.model.ServiceType;
import org.jspecify.annotations.NullMarked;

import java.net.URI;
import java.util.Objects;

/// Represents a single service - a bus route, train route, train station, or systemwide grouping - impacted by an
/// alert.
///
/// @param type the type of service this service represents
/// @param typeDescription the plain English description of `type` (e.g., "Bus Route")
/// @param name the name of this service (e.g., "Clark", "Red Line", "Jackson", "All Bus Routes")
/// @param serviceId the identifier of this service; matches GTFS route or station IDs, except for systemwide
///                  groupings, which use a fixed identifier instead (e.g., "22", "Red", "Systemwide")
/// @param color the color of this service used in maps; casing varies (e.g., "565a5c", "0065BD")
/// @param textColor the suggested color of text displayed against `color`; casing varies (e.g., "ffffff", "FFFFFF")
/// @param url the URL of this service's page on transitchicago.com
@NullMarked
public record ImpactedService(
    ServiceType type,
    String typeDescription,
    String name,
    String serviceId,
    String color,
    String textColor,
    URI url
) {
    /// Constructs an `ImpactedService`.
    ///
    /// @param type the type of service the service represents
    /// @param typeDescription the plain English description of `type` (e.g., "Bus Route")
    /// @param name the name of the service (e.g., "Clark", "Red Line", "Jackson", "All Bus Routes")
    /// @param serviceId the identifier of the service; matches GTFS route or station IDs, except for systemwide
    ///                  groupings, which use a fixed identifier instead (e.g., "22", "Red", "Systemwide")
    /// @param color the color of the service used in maps; casing varies (e.g., "565a5c", "0065BD")
    /// @param textColor the suggested color of text displayed against `color`; casing varies
    ///                  (e.g., "ffffff", "FFFFFF")
    /// @param url the URL of the service's page on transitchicago.com
    /// @throws NullPointerException if `type`, `typeDescription`, `name`, `serviceId`, `color`, `textColor`, or `url`
    /// is `null`
    public ImpactedService {
        Objects.requireNonNull(type);
        Objects.requireNonNull(typeDescription);
        Objects.requireNonNull(name);
        Objects.requireNonNull(serviceId);
        Objects.requireNonNull(color);
        Objects.requireNonNull(textColor);
        Objects.requireNonNull(url);
    }
}
