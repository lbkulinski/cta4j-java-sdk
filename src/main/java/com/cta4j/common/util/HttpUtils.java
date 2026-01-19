package com.cta4j.common.util;

import com.cta4j.common.exception.Cta4jException;
import org.apache.hc.client5.http.fluent.Request;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.net.URI;

@ApiStatus.Internal
public final class HttpUtils {
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

        String response;

        try {
            response = Request.get(url)
                              .execute()
                              .returnContent()
                              .asString();
        } catch (IOException e) {
            String path = uri.getPath();

            String message = String.format("Request to %s failed due to an I/O error", path);

            throw new Cta4jException(message, e);
        }

        return response;
    }
}
