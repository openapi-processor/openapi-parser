package io.openapiparser;

import io.openapiparser.converter.MapObjectsOrEmptyConverter;
import io.openapiparser.converter.ObjectOrNullConverter;
import io.openapiparser.schema.PropertyBucket;

import java.util.Map;

public class Properties {
    private final Context context;
    private final PropertyBucket properties;

    public Properties (Context context, PropertyBucket properties) {
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
