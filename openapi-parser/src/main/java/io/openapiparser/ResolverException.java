package io.openapiparser;

public class ResolverException extends Exception {

    public ResolverException (String message, Exception e) {
        super(message, e);
    }
}
