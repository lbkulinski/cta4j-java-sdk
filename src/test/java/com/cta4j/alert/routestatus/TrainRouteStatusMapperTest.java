package com.cta4j.alert.routestatus;

import com.cta4j.alert.routestatus.internal.mapper.TrainRouteStatusMapper;
import com.cta4j.alert.routestatus.internal.wire.CtaRouteInfo;
import com.cta4j.alert.routestatus.internal.wire.CtaRouteInfoUrl;
import com.cta4j.alert.routestatus.model.TrainRouteStatus;
import com.cta4j.common.train.TrainLine;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class TrainRouteStatusMapperTest {
    @Test
    void toDomain_mapsAllFields() {
        CtaRouteInfo wire = new CtaRouteInfo(
            "Red Line", "c60c30", "ffffff", "Red",
            new CtaRouteInfoUrl("http://www.transitchicago.com/redline/"),
            "Normal Service", "404040"
        );

        TrainRouteStatus status = TrainRouteStatusMapper.INSTANCE.toDomain(wire);

        assertThat(status.route()).isEqualTo("Red Line");
        assertThat(status.color()).isEqualTo("c60c30");
        assertThat(status.textColor()).isEqualTo("ffffff");
        assertThat(status.line()).isEqualTo(TrainLine.RED);
        assertThat(status.url()).hasToString("http://www.transitchicago.com/redline/");
        assertThat(status.status()).isEqualTo("Normal Service");
        assertThat(status.statusColor()).isEqualTo("404040");
    }

    @Test
    void toDomain_throwsIllegalArgumentException_whenServiceIdIsNotATrainLine() {
        CtaRouteInfo wire = new CtaRouteInfo(
            "Clark", "565a5c", "ffffff", "22",
            new CtaRouteInfoUrl("http://www.transitchicago.com/bus/22/"),
            "Bus Stop Note", "000000"
        );

        assertThatIllegalArgumentException().isThrownBy(() -> TrainRouteStatusMapper.INSTANCE.toDomain(wire));
    }
}
