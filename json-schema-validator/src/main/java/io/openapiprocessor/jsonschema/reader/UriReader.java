/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.reader;

import io.openapiprocessor.interfaces.Reader;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Redirect;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Objects;

/**
 * simple {@link Reader} implementation based on {@link URL}.
 */
public class UriReader implements Reader {

    /**
     * reads the document at the given uri.
     *
     * @param uri uri of the OpenAPI document.
     * @return input stream of the uri
     * @throws IOException if it fails to read the uri
     */
    @Override
    public InputStream read (URI uri) throws IOException {
        Objects.requireNonNull (uri);

        if (!isHttp(uri)) {
            return readFromUri(uri);
        }

        return readFromHttp(uri);
    }

    private static InputStream readFromUri(URI uri) throws IOException {
        URL root = uri.toURL ();
        return root.openStream();
    }

    private static InputStream readFromHttp(URI uri) throws IOException {
        HttpClient client = buildHttpClient();
        HttpRequest request = buildHttpRequest(uri);

        try {
            return client.send(request, HttpResponse.BodyHandlers.ofInputStream()).body();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static HttpRequest buildHttpRequest(URI uri) {
        return HttpRequest.newBuilder(uri)
                //.header("Accept", "application/json")
                .GET()
                .timeout(Duration.ofSeconds(30))
                .build();
    }

    private static HttpClient buildHttpClient() {
        return HttpClient.newBuilder()
            .followRedirects(Redirect.NORMAL)
            .build();
    }

    private static boolean isHttp(URI uri) {
        return uri.getScheme().startsWith("http");
    }
}
