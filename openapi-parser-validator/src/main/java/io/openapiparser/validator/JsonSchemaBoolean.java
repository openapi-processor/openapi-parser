package io.openapiparser.validator;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;

public class JsonSchemaBoolean implements JsonSchema {
    private final JsonPointer pointer;
    private final Boolean content;

    public JsonSchemaBoolean (Boolean content) {
        this.pointer = JsonPointer.EMPTY;
        this.content = content;
    }

    public JsonSchemaBoolean (JsonPointer pointer, Boolean content) {
        this.pointer = pointer;
        this.content = content;
    }

    @Override
    public @Nullable URI getMetaSchema () {
        return null;
    }

    @Override
    public @Nullable URI getId () {
        return null;
    }

    @Override
    public boolean isUniqueItems () {
        return false;
    }

    @Override
    public @Nullable JsonSchema getPropertySchema (String propName) {
        return null;
    }

}
