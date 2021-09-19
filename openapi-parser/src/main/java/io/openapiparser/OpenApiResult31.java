package io.openapiparser;

import io.openapiparser.model.v31.OpenApi;

public class OpenApiResult31 implements OpenApiResult {
    private final Context context;

    public OpenApiResult31 (Context context) {
        this.context = context;
    }

    @Override
    public Version getVersion () {
        return Version.V31;
    }

    @SuppressWarnings ("unchecked")
    @Override
    public <T> T getModel (Class<T> api) {
        if (!OpenApi.class.equals (api)) {
            throw new IllegalArgumentException ();
        }

        return (T) new OpenApi (context, new Node (context.getBaseNode ()));
    }

    @Override
    public ValidationResult getValidationResult () {
        return new WritableValidationResult ();
    }

    void validate() {

    }

}
