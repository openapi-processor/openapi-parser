package io.openapiparser.schema;

import io.openapiparser.converter.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.Map;

public class JsonSchemaObject implements JsonSchema {
    @Deprecated
    private final JsonPointer location;
    private final PropertyBucket content; // properties
    private final PropertyBucket properties; // propertiesProperty

    public JsonSchemaObject (Map<String, Object> document) {
        location = JsonPointer.EMPTY;
        content = new PropertyBucket (location, document);
        properties = getProperties ();
    }

    public JsonSchemaObject (JsonPointer location, Map<String, Object> document) {
        this.location = location;
        this.content = new PropertyBucket (location, document);
        properties = getProperties ();
    }

    private JsonSchemaObject (JsonPointer location, PropertyBucket content) {
        this.location = location;
        this.content = content;
        properties = getProperties ();
    }

    public JsonPointer getLocation (String property) {
        return location.append (property);
    }

    @Override
    public @Nullable URI getMetaSchema () {
        return content.convert ("$schema", new UriConverter ());

//        String uri = (String) content.getRawValue ("$schema");
//        if (uri == null)
//            return null;
//
//        return URI.create (uri);
    }

    @Override
    public @Nullable URI getId () {
        return content.convert ("id", new UriConverter ());

//        String uri = (String) content.getRawValue ("id");
//        if (uri == null)
//            return null;
//
//        return URI.create (uri);
    }

    @Override
    public boolean isUniqueItems () {
        Boolean unique = content.convert ("uniqueItems", new BooleanConverter (location));

//        Boolean unique = (Boolean) content.getRawValue ("uniqueItems");
        if (unique == null)
            return false;

        return unique;
    }

    @Override
    public @Nullable JsonSchema getPropertySchema (String name) {
        return properties.convert (name, new JsonSchemaConverter (location.append ("properties")));

//        JsonPointer propsPointer = pointer.append ("properties");

//        Content value = this.properties.getContentValue (propsPointer, name);
//        if (value == null)
//            return null;
//
//        return new JsonSchemaObject (propsPointer, value);
    }

    private PropertyBucket getProperties () {
        return content.convert ("properties", new ContentConverter (location));
//        return content.getContentValue (pointer, "properties");
    }
}
