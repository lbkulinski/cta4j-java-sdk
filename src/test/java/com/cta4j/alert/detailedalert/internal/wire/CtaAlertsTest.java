package com.cta4j.alert.detailedalert.internal.wire;

import com.cta4j.alert.common.internal.wire.CtaCdata;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CtaAlertsTest {
    @Test
    void constructor_copiesAlert_whenNonNull() {
        List<CtaAlert> alert = new ArrayList<>(List.of(newAlert("115070")));

        CtaAlerts alerts = new CtaAlerts("2026-07-28T12:00:00", "0", null, alert);
        alert.add(newAlert("115080"));

        assertThat(alerts.alert()).hasSize(1);
    }

    @Test
    void constructor_allowsNullAlert() {
        CtaAlerts alerts = new CtaAlerts("2026-07-28T12:00:00", "25", "There are no active alerts", null);

        assertThat(alerts.alert()).isNull();
    }

    @Test
    void constructor_throwsNullPointerException_whenTimestampIsNull() {
        assertThatNullPointerException().isThrownBy(() -> new CtaAlerts(null, "0", null, null));
    }

    @Test
    void constructor_throwsNullPointerException_whenErrorCodeIsNull() {
        assertThatNullPointerException().isThrownBy(() -> new CtaAlerts("2026-07-28T12:00:00", null, null, null));
    }

    private static CtaAlert newAlert(String alertId) {
        return new CtaAlert(
            alertId,
            "Headline",
            "Short description",
            new CtaCdata("Full description"),
            "9",
            "000000",
            "minor",
            "Impact",
            "2026-07-01T05:00:00",
            null,
            "0",
            "0",
            new CtaCdata("http://www.transitchicago.com/alerts/" + alertId),
            new CtaImpactedServices(List.of()),
            null,
            null
        );
    }
}