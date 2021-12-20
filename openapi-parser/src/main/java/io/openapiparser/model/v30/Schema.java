/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v30;

import io.openapiparser.*;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Map;

import static io.openapiparser.Keywords.*;

/**
 * the <em>Schema</em> object.
 *
 * <p>See specification:
 * <a href="https://spec.openapis.org/oas/v3.0.3.html#schema-object">4.7.24 Schema Object</a>
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-00">JSON Schema</a>
 * <a href="https://datatracker.ietf.org/doc/html/draft-wright-json-schema-validation-00">JSON
 * Schema Validation</a>
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

    public Integer getMinProperties() {
        return getSource ().getIntegerValue (MIN_PROPERTIES, 0);
    }

    public @Nullable Integer getMaxProperties() {
        return getSource ().getIntegerValue (MAX_PROPERTIES);
    }

    public Collection<String> getRequired() {
        return getSource ().getStringValuesOrEmpty (REQUIRED);
    }

    public String getType() {
        return getSource ().getRequiredStringValue (TYPE);
    }

    public @Nullable String getFormat() {
        return getSource ().getStringValue (FORMAT);
    }

    public @Nullable Collection<?> getEnum() {
        return getSource ().getStringValues (ENUM);
    }

    public @Nullable String getTitle () {
        return getSource ().getStringValue (TITLE);
    }

    public @Nullable String getDescription () {
        return getSource ().getStringValue (DESCRIPTION);
    }

    public @Nullable Object getDefault () {
        return getSource ().getRawValue (DEFAULT);
    }

    public Boolean getNullable () {
        return getSource ().getBooleanValue (NULLABLE, false);
    }

    public Boolean getDeprecated () {
        return getSource ().getBooleanValue (DEPRECATED, false);
    }

    public Map<String, Schema> getProperties () {
        return node.getMapObjectValuesOrEmpty (PROPERTIES, node -> new Schema (context, node));
    }

    @Override
    public Map<String, Object> getExtensions () {
        return getSource ().getExtensions ();
    }

    private Node getSource () {
        return (refNode != null) ? refNode : node;
    }
}
