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
        refNode = context.getRefNodeOrNull (node);
    }

    @Override
    public boolean isRef () {
        return node.hasProperty (REF);
    }

    @Override
    @Required
    public String getRef () {
        return node.getRequiredStringValue (REF);
    }

    @Override
    public @Nullable String getSummary () {
        return null;
    }

    /**
     * JSON Schema Validation: metadata
     */
    @Override
    public @Nullable String getDescription () {
        return getSource ().getStringValue (DESCRIPTION);
    }

    // JSON Schema: core keyword $dynamicRef
    // JSON Schema: core keyword $defs
    // JSON Schema: core keyword $comment

    // JSON Schema: subschemas logic keyword allOf
    // JSON Schema: subschemas logic keyword anyOf
    // JSON Schema: subschemas logic keyword oneOf
    // JSON Schema: subschemas logic keyword not

    // JSON Schema: subschemas conditional keyword if
    // JSON Schema: subschemas conditional keyword then
    // JSON Schema: subschemas conditional keyword else
    // JSON Schema: subschemas conditional keyword dependentSchemas

    /**
     * JSON Schema: subschemas array keyword
     */
    public Collection<Schema> getPrefixItems () {
        return getSource ().getObjectValuesOrEmpty (PREFIX_ITEMS, node -> new Schema (context, node));
    }

    /**
     * JSON Schema: subschemas array keyword
     */
    public @Nullable Schema getItems () {
        return getSource ().getObjectValue (ITEMS, node -> new Schema (context, node));
    }

    /**
     * JSON Schema: subschemas array keyword
     */
    public @Nullable Schema getContains () {
        return getSource ().getObjectValue (CONTAINS, node -> new Schema (context, node));
    }

    /**
     * JSON Schema: subschemas object keyword
     */
    public Map<String, Schema> getProperties () {
        return node.getMapObjectValuesOrEmpty (PROPERTIES, node -> new Schema (context, node));
    }

    /**
     * JSON Schema: subschemas object keyword
     */
    public Map<String, Schema> getPatternProperties () {
        return node.getMapObjectValuesOrEmpty (PATTERN_PROPERTIES, node -> new Schema (context, node));
    }

    /**
     * JSON Schema: subschemas object keyword
     */
    public @Nullable Schema getAdditionalProperties () {
        return node.getObjectValue (ADDITIONAL_PROPERTIES, node -> new Schema (context, node));
    }

    /**
     * JSON Schema: subschemas object keyword
     */
    public @Nullable Schema getPropertyNames () {
        return node.getObjectValue (PROPERTY_NAMES, node -> new Schema (context, node));
    }

    // JSON Schema: subschemas unevaluated keyword unevaluatedItems
    // JSON Schema: subschemas unevaluated keyword unevaluatedProperties

    /**
     * JSON Schema Validation: validation keyword for any instance type
     */
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

    /**
     * JSON Schema Validation: validation keyword for any instance type
     */
    public @Nullable Collection<?> getEnum() {
        return getSource ().getStringValues (ENUM);
    }

    /**
     * JSON Schema Validation: validation keyword for any instance type
     */
    public @Nullable String getConst() {
        return getSource ().getStringValue (CONST);
    }

    /**
     * JSON Schema Validation: validation Keywords for numeric instances (number and integer)
     */
    public @Nullable Number getMultipleOf() {
        return getSource ().getNumberValue (MULTIPLE_OF);
    }

    /**
     * JSON Schema Validation: validation Keywords for numeric instances (number and integer)
     */
    public @Nullable Number getMaximum() {
        return getSource ().getNumberValue (MAXIMUM);
    }

    /**
     * JSON Schema Validation: validation Keywords for numeric instances (number and integer)
     */
    public Boolean getExclusiveMaximum() {
        return getSource ().getBooleanValue (EXCLUSIVE_MAXIMUM, false);
    }

    /**
     * JSON Schema Validation: validation Keywords for numeric instances (number and integer)
     */
    public @Nullable Number getMinimum() {
        return getSource ().getNumberValue (MINIMUM);
    }

    /**
     * JSON Schema Validation: validation Keywords for numeric instances (number and integer)
     */
    public Boolean getExclusiveMinimum() {
        return getSource ().getBooleanValue (EXCLUSIVE_MINIMUM, false);
    }

    /**
     * JSON Schema Validation: validation Keywords for strings
     */
    public @Nullable Number getMaxLength() {
        return getSource ().getNumberValue (MAX_LENGTH);
    }

    /**
     * JSON Schema Validation: validation Keywords for strings
     */
    public @Nullable Number getMinLength() {
        return getSource ().getNumberValue (MIN_LENGTH);
    }

    /**
     * JSON Schema Validation: validation Keywords for strings
     */
    public @Nullable String getPattern() {
        return getSource ().getStringValue (PATTERN);
    }

    /**
     * JSON Schema Validation: validation Keywords for arrays
     */
    public @Nullable Integer getMaxItems() {
        return getSource ().getIntegerValue (MAX_ITEMS);
    }

    /**
     * JSON Schema Validation: validation Keywords for arrays
     */
    public Integer getMinItems() {
        return getSource ().getIntegerValue (MIN_ITEMS, 0);
    }

    /**
     * JSON Schema Validation: validation Keywords for arrays
     */
    public Boolean getUniqueItems() {
        return getSource ().getBooleanValue (UNIQUE_ITEMS, false);
    }

    /**
     * JSON Schema Validation: validation Keywords for arrays
     */
    public @Nullable Integer getMaxContains() {
        return getSource ().getIntegerValue (MAX_CONTAINS);
    }

    /**
     * JSON Schema Validation: validation Keywords for arrays
     */
    public Integer getMinContains() {
        return getSource ().getIntegerValue (MIN_CONTAINS, 1);
    }

    /**
     * JSON Schema Validation: validation Keywords for objects
     */
    public @Nullable Integer getMaxProperties() {
        return getSource ().getIntegerValue (MAX_PROPERTIES);
    }

    /**
     * JSON Schema Validation: validation Keywords for objects
     */
    public Integer getMinProperties() {
        return getSource ().getIntegerValue (MIN_PROPERTIES, 0);
    }

    /**
     * JSON Schema Validation: validation Keywords for objects
     */
    public Collection<String> getRequired() {
        return getSource ().getStringValuesOrEmpty (REQUIRED);
    }

    /**
     * JSON Schema Validation: validation Keywords for objects
     */
    public Map<String, Set<String>> getDependentRequired() {
        return getSource ().getObjectSetValuesOrEmpty (DEPENDENT_REQUIRED);
    }

    /**
     * JSON Schema Validation: semantic format
     */
    public @Nullable String getFormat() {
        return getSource ().getStringValue (FORMAT);
    }

    /* todo contentEncoding
     * JSON Schema Validation: string-encoded data
     */

    /* todo contentMediaType
     * JSON Schema Validation: string-encoded data
     */

    /* todo contentSchema
     * JSON Schema Validation: string-encoded data
     */

    /**
     * JSON Schema Validation: metadata
     */
    public @Nullable String getTitle () {
        return getSource ().getStringValue (TITLE);
    }

    /**
     * JSON Schema Validation: metadata
     */
    public @Nullable Object getDefault () {
        return getSource ().getRawValue (DEFAULT);
    }

    /**
     * JSON Schema Validation: metadata
     */
    public Boolean getDeprecated () {
        return getSource ().getBooleanValue (DEPRECATED, false);
    }

    /* todo readOnly
     * JSON Schema Validation: metadata
     */

    /* todo writeOnly
     * JSON Schema Validation: metadata
     */

    /* todo examples
     * JSON Schema Validation: metadata
     */

    /* todo discriminator
     * OpenAPI base vocabulary
     */

    /* todo xml
     * OpenAPI base vocabulary
     */

    /* todo externalDocs
     * OpenAPI base vocabulary
     */

    /* todo example
     * OpenAPI base vocabulary
     * @Deprecated
     */

    /**
     * todo not required to have x- prefix
     */
    @Override
    public Map<String, Object> getExtensions () {
        return node.getExtensions ();
    }

    private Node getSource () {
        return (refNode != null) ? refNode : node;
    }
}
