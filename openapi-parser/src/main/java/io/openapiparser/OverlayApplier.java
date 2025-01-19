/*
 * Copyright 2025 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import com.jayway.jsonpath.*;
import com.jayway.jsonpath.spi.json.AbstractJsonProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import io.openapiparser.model.ov10.Action;
import io.openapiparser.model.ov10.Overlay;
import io.openapiparser.support.Experimental;
import io.openapiprocessor.jsonschema.schema.NotImplementedException;
import io.openapiprocessor.jsonschema.support.Types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;

@Experimental
public class OverlayApplier {
    private static final Logger log = LoggerFactory.getLogger (OverlayApplier.class);

    private final Map<String, Object> document;

    public OverlayApplier(Map<String, Object> document) {
        this.document = deepCopy(document);
    }

    public Map<String, Object> apply(OverlayResult overlayResult) {
        OverlayResult.Version version = overlayResult.getVersion();

        if (version == OverlayResult.Version.V10) {
            return apply10(overlayResult);

        } else {
            throw new UnknownVersionException(version.toString());
        }
    }

    private Map<String, Object> apply10(OverlayResult overlayResult) {
        Overlay overlay = overlayResult.getModel(Overlay.class);
        DocumentContext context = createContext();

        overlay.getActions().forEach(action -> {
            String location = action.getTarget();

            if (Boolean.TRUE.equals(action.getRemove())) {
                context.delete(location);

            } else {
                Collection<Object> targets = context.read(location);
                if (targets == null || targets.isEmpty()) {
                    log.warn("target json path {} result is empty!", location);
                    return;
                }

                targets.forEach(target -> {
                    if(Types.isObject(target) && Types.isObject(action.getUpdate())) {
                        Map<String, Object> targetObject = Types.asObject(target);
                        mergeObject(targetObject, action);

                    } else if (Types.isArray(target)) {
                        Collection<Object> targetArray = Types.asArray(target);
                        targetArray.add(action.getUpdate());

                    } else {
                        log.warn("target json path {} is not an object or array!", location);
                    }
                });
            }
        });

        return document;
    }

    private DocumentContext createContext() {
        Configuration conf = Configuration.builder()
                .options(Option.ALWAYS_RETURN_LIST)
                .jsonProvider(new OverlayJsonProvider())
                .mappingProvider(new OverlayMappingProvider())
                .build();

        return JsonPath.using(conf).parse(document);
    }

    private void mergeObject(Map<String, Object> targetRoot, Action action) {
        Map<String, Object> actionRoot = Types.asObject(action.getUpdate());
        if (actionRoot == null) {
            log.warn("target json path {} update is empty!", action.getTarget());
            return;
        }

        actionRoot.forEach((key, value) -> {
            Object target = targetRoot.get(key);

            if (Types.isObject(target) && Types.isObject(value)) {
                mergeObject(Types.asObject(target), Types.asObject(value));

            } else if (Types.isArray(target)) {
                Collection<Object> targetArray = Types.asArray(value);
                targetArray.add(deepCopy(value));
            } else {
                Object oldValue = targetRoot.get(key);
                if (oldValue != null && oldValue.getClass() != value.getClass()) {
                    log.warn("target json path {} does not have same type ({} vs {}). ",
                            action.getTarget(),
                            oldValue.getClass().getName(),
                            value.getClass().getName());
                    return;
                }

                targetRoot.put(key, deepCopy(value));
            }
        });
    }

    private void mergeObject(Map<String, Object> targetRoot, Map<String, Object> actionRoot) {
        actionRoot.forEach((key, value) -> {
            Object target = targetRoot.get(key);

            if (Types.isObject(target) && Types.isObject(value)) {
                mergeObject(Types.asObject(target), Types.asObject(value));

            } else if (Types.isArray(target)) {
                Collection<Object> targetArray = Types.asArray(value);
                targetArray.add(deepCopy(value));
            } else {
                Object oldValue = targetRoot.get(key);
                if (oldValue != null && oldValue.getClass() != value.getClass()) {
                    log.warn("ignoring action property {}. It has not the same ", key);
                }

                targetRoot.put(key, deepCopy(value));
            }
        });
    }

    private Map<String, Object> deepCopy(Map<String, Object> object) {
        return (Map<String, Object>) deepCopy((Object)object);
    }

    private Object deepCopy (Object object) {
        if (Types.isObject(object)) {
            return deepCopyMap(Types.asObject(object));

        } else if (Types.isArray(object)) {
            return deepCopyArray(Types.asArray(object));

        } else {
            return object;
        }
    }

    private Map<String, Object> deepCopyMap(Map<String, Object> source) {
        Map<String, Object> copy = new LinkedHashMap<>(source.size());
        source.forEach((key, value) -> {
            copy.put(key, deepCopy(value));
        });
        return copy;
    }

    private Collection<Object> deepCopyArray(Collection<Object> source) {
        Collection<Object> copy = new ArrayList<>(source.size());
        source.forEach(item -> {
           copy.add(deepCopy(item));
        });
        return copy;
    }

    private static class OverlayJsonProvider extends AbstractJsonProvider {

        @Override
        public Object parse(String json) throws InvalidJsonException {
            throw new NotImplementedException();
        }

        @Override
        public Object parse(InputStream jsonStream, String charset) throws InvalidJsonException {
            throw new NotImplementedException();
        }

        @Override
        public String toJson(Object obj) {
            throw new NotImplementedException();
        }

        @Override
        public List<Object> createArray() {
            return new LinkedList<>();
        }

        @Override
        public Object createMap() {
            return new LinkedHashMap<String, Object>();
        }
    }

    private static class OverlayMappingProvider implements MappingProvider {

        @Override
        public <T> T map(Object source, Class<T> targetType, Configuration configuration) {
            throw new NotImplementedException();
        }

        @Override
        public <T> T map(Object source, TypeRef<T> targetType, Configuration configuration) {
            throw new NotImplementedException();
        }
    }
}
