package io.openapiparser;

public interface OpenApiResult {

    enum Version { V30, V31 }

    Version getVersion();

    <T> T getModel(Class<T> api);

    ValidationResult getValidationResult();

}
