package io.openapiparser;

import io.openapiparser.model.v30.OpenApi;
import io.openapiparser.model.v30.validations.OpenapiValidator;

import java.util.Collection;

public class OpenApiResult30 implements OpenApiResult {
    private final Context context;
    private Collection<ValidationMessage> validationMessages;

    public OpenApiResult30 (Context context) {
        this.context = context;
    }

    @Override
    public Version getVersion () {
        return Version.V30;
    }

    @SuppressWarnings ("unchecked")
    @Override
    public <T> T getModel (Class<T> api) {
        if (!OpenApi.class.equals (api)) {
            throw new IllegalArgumentException ();
        }

        return (T)new OpenApi (context, context.getBaseNode ());
    }

    @Override
    public Collection<ValidationMessage> getValidationMessages () {
        return validationMessages;
    }

    void validate() {
        validationMessages = new OpenapiValidator ().validate (
            new ValidationContext (context.getBaseUri ()), context.getBaseNode ());
    }

}
