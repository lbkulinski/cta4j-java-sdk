package com.cta4j.common.internal.http;

import com.cta4j.common.exception.Cta4jException;
import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.fluent.Request;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.net.URI;

@ApiStatus.Internal
@NullMarked
public final class HttpClient {
    private HttpClient() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String get(String url) {
        URI uri;

        try {
            uri = URI.create(url);
        } catch (IllegalArgumentException e) {
            throw new Cta4jException("Invalid URL", url, e);
        }

        String response;

        try {
            response = Request.get(url)
                              .execute()
                              .returnContent()
                              .asString();
        } catch (HttpResponseException e) {
            int statusCode = e.getStatusCode();
            String message = "Request failed with status code %d".formatted(statusCode);

            String path = uri.getPath();

            throw new Cta4jException(message, path, e);
        } catch (IOException e) {
            String path = uri.getPath();

            throw new Cta4jException("Request failed due to an I/O error", path, e);
        }

        return response;
    }
}
