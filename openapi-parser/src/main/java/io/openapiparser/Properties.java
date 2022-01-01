package io.openapiparser;

import io.openapiparser.converter.MapObjectsOrEmptyConverter;
import io.openapiparser.converter.ObjectOrNullConverter;
import io.openapiparser.schema.Bucket;

import java.util.Map;

public class Properties {
    private final Context context;
    private final Bucket properties;

    public Properties (Context context, Bucket properties) {
        this.context = context;
        this.properties = properties;
    }

    public <T> Map<String, T> getMapObjectsOrEmpty (Class<T> clazz) {
        return properties.convert (new MapObjectsOrEmptyConverter<> (context, clazz));
    }

    public <T> T getObjectOrNull (String property, Class<T> clazz) {
        return properties.convert (property, new ObjectOrNullConverter<> (context, clazz));
    }
}
