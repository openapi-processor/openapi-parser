package io.openapiparser;

import io.openapiparser.model.v30.OpenApi;

public class OpenApiResult30 implements OpenApiResult {
    private final Context context;

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

        return (T)new OpenApi (context);
    }

    @Override
    public ValidationResult getValidationResult () {
        return new WritableValidationResult ();
    }

    void validate() {

    }

}
