package io.openapiparser;

import java.util.Collection;
import java.util.Map;

public interface Validation {

    Collection<ValidationMessage> validate(ValidationContext context, Map<String, Object> node);

}
