package com.cta4j.alert.routestatus.model;

import org.jspecify.annotations.NullMarked;

import java.net.URI;
import java.util.Objects;

/// Represents the service status of a single route.
///
/// @param route the name of this route (e.g., "Clark")
/// @param color the color of this route used in maps; casing varies (e.g., "565a5c", "0065BD")
/// @param textColor the suggested color of text displayed against `color`; casing varies (e.g., "ffffff", "FFFFFF")
/// @param serviceId the unique GTFS route or station identifier of this route (e.g., "22")
/// @param url the URL of this route's or station's page on transitchicago.com
/// @param status the ultimate, human-readable status of this route
///               (e.g., "Normal Service", "Service Change", "Bus Stop Note")
/// @param statusColor the suggested color associated with `status`; length and casing vary
///                    (e.g., "000000", "06c", "B45F04")
@NullMarked
public record RouteStatus(
    String route,
    String color,
    String textColor,
    String serviceId,
    URI url,
    String status,
    String statusColor
) {
    /// Constructs a `RouteStatus`.
    ///
    /// @param route the name of the route (e.g., "Clark")
    /// @param color the color of the route used in maps; casing varies (e.g., "565a5c", "0065BD")
    /// @param textColor the suggested color of text displayed against `color`; casing varies
    ///                  (e.g., "ffffff", "FFFFFF")
    /// @param serviceId the unique GTFS route or station identifier of the route (e.g., "22")
    /// @param url the URL of the route's or station's page on transitchicago.com
    /// @param status the ultimate, human-readable status of the route
    ///               (e.g., "Normal Service", "Service Change", "Bus Stop Note")
    /// @param statusColor the suggested color associated with `status`; length and casing vary
    ///                    (e.g., "000000", "06c", "B45F04")
    /// @throws NullPointerException if `route`, `color`, `textColor`, `serviceId`, `url`, `status`, or `statusColor`
    /// is `null`
    public RouteStatus {
        Objects.requireNonNull(route);
        Objects.requireNonNull(color);
        Objects.requireNonNull(textColor);
        Objects.requireNonNull(serviceId);
        Objects.requireNonNull(url);
        Objects.requireNonNull(status);
        Objects.requireNonNull(statusColor);
    }
}
