package io.openapiparser;


/**
 * thrown by {@link Converter} if {@link Converter#convert(String)} fails.
 */
public class ConverterException extends Exception {
    public ConverterException (String message, Exception e) {
        super(message, e);
    }
}
