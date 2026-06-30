package com.cta4j.common.internal.http;

import com.cta4j.exception.Cta4jException;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.Fault;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.*;

class HttpClientTest {
    private WireMockServer server;

    @BeforeEach
    void setUp() {
        this.server = new WireMockServer(wireMockConfig().dynamicPort());
        this.server.start();
    }

    @AfterEach
    void tearDown() {
        this.server.stop();
    }

    @Test
    void get_returnsBody_whenResponseSucceeds() {
        this.server.stubFor(get(urlPathEqualTo("/test"))
            .willReturn(aResponse()
                .withStatus(200)
                .withBody("hello")));

        String result = HttpClient.get("http://localhost:" + this.server.port() + "/test");

        assertThat(result).isEqualTo("hello");
    }

    @Test
    void get_throwsCta4jException_whenUrlIsInvalid() {
        assertThatThrownBy(() -> HttpClient.get("http://foo bar/test"))
            .isInstanceOf(Cta4jException.class);
    }

    @Test
    void get_throwsCta4jException_whenResponseReturnsHttpError() {
        this.server.stubFor(get(urlPathEqualTo("/test"))
            .willReturn(aResponse()
                .withStatus(500)));

        assertThatThrownBy(() -> HttpClient.get("http://localhost:" + this.server.port() + "/test"))
            .isInstanceOf(Cta4jException.class);
    }

    @Test
    void get_throwsCta4jException_whenIoErrorOccurs() {
        this.server.stubFor(get(urlPathEqualTo("/test"))
            .willReturn(aResponse()
                .withFault(Fault.MALFORMED_RESPONSE_CHUNK)));

        assertThatThrownBy(() -> HttpClient.get("http://localhost:" + this.server.port() + "/test"))
            .isInstanceOf(Cta4jException.class);
    }
}