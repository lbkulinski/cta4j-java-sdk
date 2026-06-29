package com.cta4j.bus.locale;

import com.cta4j.TestFixtures;
import com.cta4j.bus.common.internal.config.BusApiConfig;
import com.cta4j.bus.locale.internal.impl.LocalesApiImpl;
import com.cta4j.bus.locale.model.SupportedLocale;
import com.cta4j.exception.Cta4jException;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Locale;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.*;

class LocalesApiImplTest {
    private WireMockServer server;
    private LocalesApiImpl api;

    @BeforeEach
    void setUp() {
        this.server = new WireMockServer(wireMockConfig().dynamicPort());
        this.server.start();
        BusApiConfig config = new BusApiConfig("http", "localhost", this.server.port(), "testkey");
        this.api = new LocalesApiImpl(config);
    }

    @AfterEach
    void tearDown() {
        this.server.stop();
    }

    @Test
    void list_returnsLocales_whenResponseContainsLocales() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getlocalelist"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/locale/success.json"))));

        List<SupportedLocale> locales = this.api.list();

        assertThat(locales).hasSize(1);
        SupportedLocale locale = locales.getFirst();
        assertThat(locale.locale()).isEqualTo(Locale.of("en"));
        assertThat(locale.displayName()).isEqualTo("English");
    }

    @Test
    void list_returnsEmpty_whenResponseHasNoDataAndNoErrors() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getlocalelist"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/locale/empty.json"))));

        List<SupportedLocale> locales = this.api.list();

        assertThat(locales).isEmpty();
    }

    @Test
    void list_throwsCta4jException_whenResponseContainsFatalErrors() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getlocalelist"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/locale/error.json"))));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jException.class);
    }

    @Test
    void list_throwsCta4jException_whenResponseIsNotJson() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getlocalelist"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody("not-json")));

        assertThatThrownBy(() -> this.api.list())
            .isInstanceOf(Cta4jException.class);
    }

    @Test
    void list_withDisplayLocale_sendsLocaleParameter() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getlocalelist"))
            .withQueryParam("locale", equalTo("en"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/locale/success.json"))));

        List<SupportedLocale> locales = this.api.list(Locale.ENGLISH);

        assertThat(locales).hasSize(1);
    }

    @Test
    void listInNativeLanguage_returnsLocales_whenResponseContainsLocales() {
        this.server.stubFor(get(urlPathEqualTo("/bustime/api/v3/getlocalelist"))
            .withQueryParam("inLocaleLanguage", equalTo("true"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(TestFixtures.read("bus/locale/success.json"))));

        List<SupportedLocale> locales = this.api.listInNativeLanguage();

        assertThat(locales).hasSize(1);
    }
}
