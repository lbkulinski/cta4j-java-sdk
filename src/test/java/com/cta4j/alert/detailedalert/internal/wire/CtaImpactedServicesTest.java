package com.cta4j.alert.detailedalert.internal.wire;

import com.cta4j.alert.common.internal.wire.CtaCdata;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class CtaImpactedServicesTest {
    @Test
    void constructor_copiesService() {
        CtaImpactedService service = new CtaImpactedService(
            "B", "Bus Route", "Clark", "22", "565a5c", "ffffff",
            new CtaCdata("http://www.transitchicago.com/bus/22/")
        );
        List<CtaImpactedService> services = new ArrayList<>(List.of(service));

        CtaImpactedServices impactedServices = new CtaImpactedServices(services);
        services.add(service);

        assertThat(impactedServices.service()).hasSize(1);
    }

    @Test
    void constructor_throwsNullPointerException_whenServiceIsNull() {
        assertThatNullPointerException().isThrownBy(() -> new CtaImpactedServices(null));
    }
}