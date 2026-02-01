package com.cta4j.internal.http;

import com.cta4j.exception.Cta4jException;
import org.apache.hc.client5.http.HttpResponseException;
import org.apache.hc.client5.http.fluent.Request;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;

import java.io.IOException;
import java.net.URI;

@NullMarked
@ApiStatus.Internal
public final class HttpClient {
    private HttpClient() {
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

        String response;

        try {
            response = Request.get(url)
                              .execute()
                              .returnContent()
                              .asString();
        } catch (HttpResponseException e) {
            String path = uri.getPath();
            int statusCode = e.getStatusCode();

            String message = String.format("Request to %s failed with status code %d", path, statusCode);

            throw new Cta4jException(message, e);
        } catch (IOException e) {
            String path = uri.getPath();

            String message = String.format("Request to %s failed due to an I/O error", path);

            throw new Cta4jException(message, e);
        }

        return response;
    }
}
