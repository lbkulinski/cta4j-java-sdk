package com.cta4j.util;

import com.cta4j.exception.Cta4jException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;

import java.io.IOException;
import java.net.URI;

public final class HttpUtils {
    private static final CloseableHttpClient httpClient;

    static {
        httpClient = HttpClients.createDefault();
    }

    private HttpUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String get(String url) {
        URI uri;

        try {
            uri = URI.create(url);
        } catch (IllegalArgumentException e) {
            String message = "Invalid URL";

            throw new Cta4jException(message, e);
        }

        HttpGet httpGet = new HttpGet(uri);

        HttpClientResponseHandler<String> responseHandler = httpResponse -> {
            int status = httpResponse.getCode();

            if (status >= 200 && status < 300) {
                byte[] bytes = httpResponse.getEntity()
                                           .getContent()
                                           .readAllBytes();

                return new String(bytes);
            }

            String path = uri.getPath();

            String message = String.format("Request to %s failed with status code %d", path, status);

            throw new Cta4jException(message);
        };

        String response;

        try {
            response = httpClient.execute(httpGet, responseHandler);
        } catch (IOException e) {
            String path = uri.getPath();

            String message = String.format("Request to %s failed due to an I/O error", path);

            throw new Cta4jException(message, e);
        }

        return response;
    }
}
