/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Schema</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.1.0.html#schema-object">4.8.24 Schema Object</a>
 * <a href="https://tools.ietf.org/html/draft-bhutton-json-schema-00">JSON Schema</a>
 * <a href="https://tools.ietf.org/html/draft-bhutton-json-schema-validation-00">JSON Schema
 * Validation</a>
 */
public class Schema implements Reference, Extensions {
    private final Context context;
    private final Node node;
    private final @Nullable Node refNode;

    public Schema (Context context, Node node) {
        this.context = context;
        this.node = node;
        refNode = null;
    }

    @Override
    public boolean isRef () {
        return false;
    }

    @Override
    @Required
    public String getRef () {
        return null;
    }

    @Override
    public @Nullable String getSummary () {
        return null;
    }

    @Override
    public @Nullable String getDescription () {
        return null;
    }

    public @Nullable Number getMultipleOf() {
        return getSource ().getNumberValue (MULTIPLE_OF);
    }

    public @Nullable Number getMaximum() {
        return getSource ().getNumberValue (MAXIMUM);
    }

    public Boolean getExclusiveMaximum() {
        return getSource ().getBooleanValue (EXCLUSIVE_MAXIMUM, false);
    }

    public @Nullable Number getMinimum() {
        return getSource ().getNumberValue (MINIMUM);
    }

    public Boolean getExclusiveMinimum() {
        return getSource ().getBooleanValue (EXCLUSIVE_MINIMUM, false);
    }

    public @Nullable Number getMaxLength() {
        return getSource ().getNumberValue (MAX_LENGTH);
    }

    public @Nullable Number getMinLength() {
        return getSource ().getNumberValue (MIN_LENGTH);
    }

    public @Nullable String getPattern() {
        return getSource ().getStringValue (PATTERN);
    }

    public Integer getMinItems() {
        return getSource ().getIntegerValue (MIN_ITEMS, 0);
    }

    public @Nullable Integer getMaxItems() {
        return getSource ().getIntegerValue (MAX_ITEMS);
    }

    public Boolean getUniqueItems() {
        return getSource ().getBooleanValue (UNIQUE_ITEMS, false);
    }

    public Integer getMinContains() {
        return getSource ().getIntegerValue (MIN_CONTAINS, 1);
    }

    public @Nullable Integer getMaxContains() {
        return getSource ().getIntegerValue (MAX_CONTAINS);
    }

    public Integer getMinProperties() {
        return getSource ().getIntegerValue (MIN_PROPERTIES, 0);
    }

    public @Nullable Integer getMaxProperties() {
        return getSource ().getIntegerValue (MAX_PROPERTIES);
    }

    public Collection<String> getRequired() {
        return getSource ().getStringValuesOrEmpty (REQUIRED);
    }

    public Map<String, Set<String>> getDependentRequired() {
        return getSource ().getObjectSetValuesOrEmpty (DEPENDENT_REQUIRED);
    }

    public Collection<String> getType() {
        final Object value = getSource ().getRawValue (TYPE);
        if (value instanceof String) {
            return Collections.singletonList (getSource ().getStringValue (TYPE));
        } else if (value instanceof Collection) {
            return getSource ().getStringValues (TYPE);
        } else {
            throw new NoValueException (getSource ().getPath (TYPE));
        }
    }

    public @Nullable String getFormat() {
        return getSource ().getStringValue (FORMAT);
    }

    public @Nullable Collection<?> getEnum() {
        return getSource ().getStringValues (ENUM);
    }

    @Override
    public Map<String, Object> getExtensions () {
        return node.getExtensions ();
    }

    private Node getSource () {
        return (refNode != null) ? refNode : node;
    }
}
