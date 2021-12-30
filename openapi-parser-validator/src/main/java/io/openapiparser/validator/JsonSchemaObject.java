package io.openapiparser.validator;

import io.openapiparser.validator.converter.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Map;

public class JsonSchemaObject implements JsonSchema {
    private final JsonPointer pointer;
    private final Content content;
    private final Content properties;

    public JsonSchemaObject (Map<String, Object> document) {
        pointer = JsonPointer.EMPTY;
        content = new Content (document);
        properties = getProperties ();
    }

    public JsonSchemaObject (JsonPointer pointer, Map<String, Object> document) {
        this.pointer = pointer;
        this.content = new Content (document);
        properties = getProperties ();
    }

    private JsonSchemaObject (JsonPointer pointer, Content content) {
        this.pointer = pointer;
        this.content = content;
        properties = getProperties ();
    }

    public JsonPointer getPointer (String property) {
        return pointer.append (property);
    }

    @Override
    public @Nullable URI getMetaSchema () {
        return content.convert ("$schema", new UriConverter (pointer));

//        String uri = (String) content.getRawValue ("$schema");
//        if (uri == null)
//            return null;
//
//        return URI.create (uri);
    }

    @Override
    public @Nullable URI getId () {
        return content.convert ("id", new UriConverter (pointer));

//        String uri = (String) content.getRawValue ("id");
//        if (uri == null)
//            return null;
//
//        return URI.create (uri);
    }

    @Override
    public boolean isUniqueItems () {
        Boolean unique = content.convert ("uniqueItems", new BooleanConverter (pointer));

//        Boolean unique = (Boolean) content.getRawValue ("uniqueItems");
        if (unique == null)
            return false;

        return unique;
    }

    @Override
    public @Nullable JsonSchema getPropertySchema (String name) {
        return properties.convert (name, new JsonSchemaConverter (pointer.append ("properties")));

//        JsonPointer propsPointer = pointer.append ("properties");

//        Content value = this.properties.getContentValue (propsPointer, name);
//        if (value == null)
//            return null;
//
//        return new JsonSchemaObject (propsPointer, value);
    }

    private Content getProperties () {
        return content.convert ("properties", new ContentConverter (pointer));
//        return content.getContentValue (pointer, "properties");
    }
}
