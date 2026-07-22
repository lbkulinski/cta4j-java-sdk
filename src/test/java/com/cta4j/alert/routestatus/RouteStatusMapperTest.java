package com.cta4j.alert.routestatus;

import com.cta4j.alert.routestatus.internal.mapper.RouteStatusMapper;
import com.cta4j.alert.routestatus.internal.wire.CtaRouteInfo;
import com.cta4j.alert.common.internal.wire.CtaCdata;
import com.cta4j.alert.routestatus.model.RouteStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class RouteStatusMapperTest {
    @Test
    void toDomain_mapsAllFields() {
        CtaRouteInfo wire = new CtaRouteInfo(
            "Clark", "565a5c", "ffffff", "22",
            new CtaCdata("http://www.transitchicago.com/bus/22/"),
            "Bus Stop Note", "000000"
        );

        RouteStatus status = RouteStatusMapper.INSTANCE.toDomain(wire);

        assertThat(status.route()).isEqualTo("Clark");
        assertThat(status.color()).isEqualTo("565a5c");
        assertThat(status.textColor()).isEqualTo("ffffff");
        assertThat(status.serviceId()).isEqualTo("22");
        assertThat(status.url()).hasToString("http://www.transitchicago.com/bus/22/");
        assertThat(status.status()).isEqualTo("Bus Stop Note");
        assertThat(status.statusColor()).isEqualTo("000000");
    }
}
