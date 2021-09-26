package io.openapiparser;

import java.util.Collection;

public interface OpenApiResult {

    enum Version { V30, V31 }

    Version getVersion();

    <T> T getModel(Class<T> api);

    Collection<ValidationMessage> getValidationMessages();

}
