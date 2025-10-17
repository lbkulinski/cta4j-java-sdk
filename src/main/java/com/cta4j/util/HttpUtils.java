package com.cta4j.util;

import com.cta4j.exception.Cta4jException;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.jetbrains.annotations.ApiStatus;

import java.io.IOException;
import java.net.URI;

@ApiStatus.Internal
public final class HttpUtils {
    private static final CloseableHttpClient httpClient;

    static {
        httpClient = HttpClients.createDefault();
    }

    private HttpUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    private static String handleResponse(URI uri, ClassicHttpResponse httpResponse) throws IOException, ParseException {
        int status = httpResponse.getCode();

        if (status >= 200 && status < 300) {
            HttpEntity entity = httpResponse.getEntity();

            return EntityUtils.toString(entity);
        }

        String path = uri.getPath();

        String message = String.format("Request to %s failed with status code %d", path, status);

        throw new Cta4jException(message);
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

        String response;

        try {
            response = httpClient.execute(httpGet, httpResponse -> HttpUtils.handleResponse(uri, httpResponse));
        } catch (IOException e) {
            String path = uri.getPath();

            String message = String.format("Request to %s failed due to an I/O error", path);

            throw new Cta4jException(message, e);
        }

        return response;
    }
}
