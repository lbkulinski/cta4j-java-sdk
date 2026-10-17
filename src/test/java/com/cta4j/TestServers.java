package com.cta4j;

import com.github.tomakehurst.wiremock.WireMockServer;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

public final class TestServers {
    private TestServers() {}

    /// Creates an unstarted [WireMockServer] on a dynamic port, bound to the loopback address only.
    ///
    /// A wildcard bind can be handed a port that another local process (e.g. an IDE) already holds on `127.0.0.1`.
    /// WireMock then starts without error, but requests to `localhost` reach the other process instead, causing flaky
    /// 404s and read timeouts.
    public static WireMockServer create() {
        return new WireMockServer(wireMockConfig().dynamicPort().bindAddress("127.0.0.1"));
    }
}
