package io.openapiparser;

public class ContextException extends Exception {

    public ContextException (String message, Exception e) {
        super(message, e);
    }
}
