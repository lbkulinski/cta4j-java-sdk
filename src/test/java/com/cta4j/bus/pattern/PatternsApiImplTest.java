package com.cta4j.bus.pattern;

import com.cta4j.TestFixtures;
import com.cta4j.bus.common.exception.Cta4jBusException;
import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.common.internal.util.BusApiConstants;
import com.cta4j.bus.pattern.internal.impl.PatternsApiImpl;
import com.cta4j.bus.pattern.model.RoutePattern;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tools.jackson.core.JacksonException;

import java.util.List;
import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.*;

class PatternsApiImplTest {
    private WireMockServer server;
    private PatternsApiImpl api;

    @BeforeEach
    void setUp() {
        this.server = new WireMockServer(wireMockConfig().dynamicPort());
        this.server.start();
        BusApiConfig config = new BusApiConfig("http", "localhost", this.server.port(), "testkey");
        this.api = new PatternsApiImpl(config);
    }

    @AfterEach
    void tearDown() {
        this.server.stop();
    }

    @Test
    void findByIds_returnsPatterns_whenResponseContainsPatterns() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpatterns"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/pattern/success.json"))));

        List<RoutePattern> patterns = this.api.findByIds(List.of("3630"));

        assertThat(patterns).hasSize(1);
        RoutePattern pattern = patterns.getFirst();
        assertThat(pattern.id()).isEqualTo("3630");
        assertThat(pattern.direction()).isEqualTo("Northbound");
        assertThat(pattern.points()).hasSize(1);
    }

    @Test
    void findByIds_returnsEmpty_whenInputIsEmpty() {
        List<RoutePattern> patterns = this.api.findByIds(List.of());

        assertThat(patterns).isEmpty();
        this.server.verify(0, anyRequestedFor(anyUrl()));
    }

    @Test
    void findByIds_returnsEmpty_whenResponseHasNoDataAndNoErrors() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpatterns"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/pattern/empty.json"))));

        List<RoutePattern> patterns = this.api.findByIds(List.of("3630"));

        assertThat(patterns).isEmpty();
    }

    @Test
    void findByIds_returnsEmpty_whenAllErrorsAreResourceSpecific() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpatterns"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/pattern/not_found.json"))));

        List<RoutePattern> patterns = this.api.findByIds(List.of("9999"));

        assertThat(patterns).isEmpty();
    }

    @Test
    void findByIds_throwsCta4jBusException_whenResponseContainsFatalErrors() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpatterns"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/pattern/error.json"))));

        assertThatThrownBy(() -> this.api.findByIds(List.of("3630")))
            .isInstanceOf(Cta4jBusException.class)
            .hasMessage("Internal server error")
            .satisfies(e -> assertThat(((Cta4jBusException) e).getEndpoint())
                .isEqualTo(BusApiConstants.PATTERNS_ENDPOINT));
    }

    @Test
    void findByIds_throwsCta4jBusException_whenResponseIsNotJson() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpatterns"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("not-json")));

        assertThatThrownBy(() -> this.api.findByIds(List.of("3630")))
            .isInstanceOf(Cta4jBusException.class)
            .hasMessage("Failed to parse response")
            .satisfies(e -> assertThat(((Cta4jBusException) e).getEndpoint())
                .isEqualTo(BusApiConstants.PATTERNS_ENDPOINT))
            .satisfies(e -> assertThat(e.getCause()).isInstanceOf(JacksonException.class));
    }

    @Test
    void findByIds_throwsCta4jBusException_whenServerReturnsErrorStatus() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpatterns"))
            .willReturn(aResponse()
                .withStatus(500)));

        assertThatThrownBy(() -> this.api.findByIds(List.of("3630")))
            .isInstanceOf(Cta4jBusException.class)
            .hasMessageContaining("status code: 500")
            .satisfies(e -> assertThat(((Cta4jBusException) e).getEndpoint())
                .isEqualTo(BusApiConstants.PATTERNS_ENDPOINT))
            .satisfies(e -> assertThat(e.getCause()).isNotNull());
    }

    @Test
    void findByRouteId_sendsRtParameter() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpatterns"))
            .withQueryParam("rt", equalTo("8"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/pattern/success.json"))));

        List<RoutePattern> patterns = this.api.findByRouteId("8");

        assertThat(patterns).hasSize(1);
    }

    @Test
    void findByIds_throwsIllegalArgumentException_whenTooManyPatternIds() {
        List<String> tooMany = List.of("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11");

        assertThatIllegalArgumentException().isThrownBy(() -> this.api.findByIds(tooMany));
    }

    @Test
    void findById_returnsEmpty_whenNoPatternFound() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpatterns"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/pattern/not_found.json"))));

        Optional<RoutePattern> pattern = this.api.findById("9999");

        assertThat(pattern).isEmpty();
    }

    @Test
    void findById_returnsPattern_whenOnePatternFound() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpatterns"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/pattern/success.json"))));

        Optional<RoutePattern> pattern = this.api.findById("3630");

        assertThat(pattern).isPresent();
        assertThat(pattern.get().id()).isEqualTo("3630");
    }

    @Test
    void findById_throwsCta4jBusException_whenMultiplePatternsFound() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getpatterns"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/pattern/multiple.json"))));

        assertThatThrownBy(() -> this.api.findById("3630"))
            .isInstanceOf(Cta4jBusException.class)
            .hasMessage("Multiple route patterns found for ID: 3630")
            .satisfies(e -> assertThat(((Cta4jBusException) e).getEndpoint())
                .isEqualTo(BusApiConstants.PATTERNS_ENDPOINT));
    }
}
