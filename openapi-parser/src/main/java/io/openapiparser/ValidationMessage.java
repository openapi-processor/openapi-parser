package io.openapiparser;

public class ValidationMessage {
    // kind/source ?
    // warn/error ?
    // file ?
    private final String path;
    private final String text;

    public ValidationMessage (String path, String text) {
        this.path = path;
        this.text = text;
    }
}
