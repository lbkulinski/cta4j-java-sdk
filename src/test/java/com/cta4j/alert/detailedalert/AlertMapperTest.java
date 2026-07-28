package com.cta4j.alert.detailedalert;

import com.cta4j.alert.common.internal.wire.CtaCdata;
import com.cta4j.alert.common.model.ServiceType;
import com.cta4j.alert.detailedalert.internal.mapper.AlertMapper;
import com.cta4j.alert.detailedalert.internal.wire.CtaAlert;
import com.cta4j.alert.detailedalert.internal.wire.CtaImpactedService;
import com.cta4j.alert.detailedalert.internal.wire.CtaImpactedServices;
import com.cta4j.alert.detailedalert.model.Alert;
import com.cta4j.alert.detailedalert.model.ImpactedService;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class AlertMapperTest {
    @Test
    void toDomain_mapsAllFields_whenOptionalFieldsArePresent() {
        CtaImpactedService wireService = new CtaImpactedService(
            "B", "Bus Route", "Clark", "22", "565a5c", "ffffff",
            new CtaCdata("http://www.transitchicago.com/bus/22/")
        );

        CtaAlert wire = new CtaAlert(
            "115070",
            "Route 22 Rerouted",
            "Route 22 is rerouted due to construction",
            new CtaCdata("Route 22 buses are being rerouted due to construction on Clark St."),
            "37",
            "06c",
            "planned",
            "Planned Reroute",
            "2026-07-01T05:00:00",
            "2026-08-01T05:00:00",
            "0",
            "0",
            new CtaCdata("http://www.transitchicago.com/alerts/115070"),
            new CtaImpactedServices(List.of(wireService)),
            "0",
            "664b81c1-197b-450b-a00c-090483b90bb9"
        );

        Alert alert = AlertMapper.INSTANCE.toDomain(wire);

        assertThat(alert.id()).isEqualTo("115070");
        assertThat(alert.headline()).isEqualTo("Route 22 Rerouted");
        assertThat(alert.shortDescription()).isEqualTo("Route 22 is rerouted due to construction");
        assertThat(alert.fullDescription())
            .isEqualTo("Route 22 buses are being rerouted due to construction on Clark St.");
        assertThat(alert.severity().score()).isEqualTo(37);
        assertThat(alert.severity().color()).isEqualTo("06c");
        assertThat(alert.severity().css()).isEqualTo("planned");
        assertThat(alert.impact()).isEqualTo("Planned Reroute");
        assertThat(alert.startTime()).isEqualTo(Instant.parse("2026-07-01T10:00:00Z"));
        assertThat(alert.endTime()).isEqualTo(Instant.parse("2026-08-01T10:00:00Z"));
        assertThat(alert.openEnded()).isFalse();
        assertThat(alert.major()).isFalse();
        assertThat(alert.url()).hasToString("http://www.transitchicago.com/alerts/115070");
        assertThat(alert.impactedServices()).hasSize(1);

        ImpactedService impactedService = alert.impactedServices().getFirst();
        assertThat(impactedService.type()).isEqualTo(ServiceType.BUS);
        assertThat(impactedService.typeDescription()).isEqualTo("Bus Route");
        assertThat(impactedService.name()).isEqualTo("Clark");
        assertThat(impactedService.serviceId()).isEqualTo("22");
        assertThat(impactedService.color()).isEqualTo("565a5c");
        assertThat(impactedService.textColor()).isEqualTo("ffffff");
        assertThat(impactedService.url()).hasToString("http://www.transitchicago.com/bus/22/");

        assertThat(alert.ttim()).isEqualTo("0");
        assertThat(alert.guid()).isEqualTo("664b81c1-197b-450b-a00c-090483b90bb9");
    }

    @Test
    void toDomain_mapsNullEndTimeTtimAndGuid_whenAbsent() {
        CtaImpactedService wireService = new CtaImpactedService(
            "T", "Train Station", "Austin", "41260", "009b3a", "FFFFFF",
            new CtaCdata("http://www.transitchicago.com/travel_information/station.aspx?StopId=24")
        );

        CtaAlert wire = new CtaAlert(
            "115080",
            "Austin Main Stationhouse Temporarily Closed",
            "Elevator out of service",
            new CtaCdata("The elevator at Austin is out of service."),
            "9",
            "000000",
            "minor",
            "Elevator Status",
            "2026-07-15T00:00:00",
            null,
            "1",
            "1",
            new CtaCdata("http://www.transitchicago.com/alerts/115080"),
            new CtaImpactedServices(List.of(wireService)),
            null,
            null
        );

        Alert alert = AlertMapper.INSTANCE.toDomain(wire);

        assertThat(alert.endTime()).isNull();
        assertThat(alert.openEnded()).isTrue();
        assertThat(alert.major()).isTrue();
        assertThat(alert.ttim()).isNull();
        assertThat(alert.guid()).isNull();
    }

    @Test
    void toDomain_mapsDateOnlyEventStartAndEventEnd_toMidnightChicagoTime() {
        // Confirmed against the live Detailed Alerts API: EventStart/EventEnd are sometimes a bare
        // "yyyy-MM-dd" date with no time-of-day component (e.g., long-running planned service changes).
        CtaImpactedService wireService = new CtaImpactedService(
            "B", "Bus Route", "South Pulaski", "53A", "059", "ffffff",
            new CtaCdata("http://www.transitchicago.com/riding_cta/bus_route.aspx?RouteId=207")
        );

        CtaAlert wire = new CtaAlert(
            "115090",
            "Later, More Frequent Weekend Service",
            "Service is being increased on the South Pulaski corridor.",
            new CtaCdata("Later evening and more frequent weekend service."),
            "11",
            "000000",
            "normal",
            "Added Service",
            "2025-11-07",
            "2027-09-30",
            "0",
            "0",
            new CtaCdata("http://www.transitchicago.com/alerts/115090"),
            new CtaImpactedServices(List.of(wireService)),
            "0",
            "9979cd0c-a29d-4b52-805d-4baa0b32322b"
        );

        Alert alert = AlertMapper.INSTANCE.toDomain(wire);

        assertThat(alert.startTime()).isEqualTo(Instant.parse("2025-11-07T06:00:00Z"));
        assertThat(alert.endTime()).isEqualTo(Instant.parse("2027-09-30T05:00:00Z"));
    }

    @Test
    void toDomain_mapsMultipleImpactedServices() {
        CtaImpactedService station = new CtaImpactedService(
            "T", "Train Station", "Austin", "41260", "009b3a", "FFFFFF",
            new CtaCdata("http://www.transitchicago.com/travel_information/station.aspx?StopId=24")
        );
        CtaImpactedService route = new CtaImpactedService(
            "R", "Train Route", "Green Line", "G", "009b3a", "FFFFFF",
            new CtaCdata("http://www.transitchicago.com/greenline/")
        );

        CtaAlert wire = new CtaAlert(
            "115080",
            "Austin Main Stationhouse Temporarily Closed",
            "Elevator out of service",
            new CtaCdata("The elevator at Austin is out of service."),
            "9",
            "000000",
            "minor",
            "Elevator Status",
            "2026-07-15T00:00:00",
            null,
            "1",
            "0",
            new CtaCdata("http://www.transitchicago.com/alerts/115080"),
            new CtaImpactedServices(List.of(station, route)),
            "1",
            "d41b2532-09ca-4827-b9c6-f4299cc86fb6"
        );

        Alert alert = AlertMapper.INSTANCE.toDomain(wire);

        assertThat(alert.impactedServices()).hasSize(2);
        assertThat(alert.impactedServices().get(0).type()).isEqualTo(ServiceType.STATION);
        assertThat(alert.impactedServices().get(1).type()).isEqualTo(ServiceType.RAIL);
    }
}