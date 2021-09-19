package io.openapiparser;

import java.net.URI;

public class ValidationContext {
    private final URI source;
    private final String path;

    public ValidationContext (URI source) {
        this.source = source;
        this.path = "$";
    }

    public ValidationContext (URI source, String path) {
        this.source = source;
        this.path = path;
    }

    public String getPropertyPath(String property) {
        return path + "." + property;
    }

}
