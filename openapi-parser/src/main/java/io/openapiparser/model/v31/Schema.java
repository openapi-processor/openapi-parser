/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v31;

import io.openapiparser.*;
import io.openapiparser.Properties;
import io.openapiparser.converter.NoValueException;
import io.openapiparser.schema.Bucket;
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
public class Schema extends Properties implements Reference, Extensions {

    public Schema (Context context, Bucket bucket) {
        super (context, bucket);
    }

    @Override
    public boolean isRef () {
        return hasProperty (REF);
    }

    @Override
    public String getRef () {
        return getStringOrThrow (REF);
    }

    @Override
    public @Nullable String getSummary () {
        return getStringOrNull (SUMMARY);
    }

    /**
     * JSON Schema Validation: metadata
     */
    @Override
    public @Nullable String getDescription () {
        return getStringOrNull (DESCRIPTION);
    }

    // JSON Schema: metadata keyword $schema
    // JSON Schema: metadata keyword $vocabulary
    // JSON Schema: metadata keyword $id
    // JSON Schema: core keyword $dynamicRef
    // JSON Schema: core keyword $defs
    // JSON Schema: core keyword $comment

    /**
     * JSON Schema: subschemas logic keyword
     */
    public Collection<Schema> getAllOf () {
        return getObjectsOrEmpty (ALL_OF, Schema.class);
    }

    /**
     * JSON Schema: subschemas logic keyword
     */
    public Collection<Schema> getAnyOf () {
        return getObjectsOrEmpty (ANY_OF, Schema.class);
    }

    /**
     * JSON Schema: subschemas logic keyword
     */
    public Collection<Schema> getOneOf () {
        return getObjectsOrEmpty (ONE_OF, Schema.class);
    }

    /**
     * JSON Schema: subschemas logic keyword
     */
    public @Nullable Schema getNot () {
        return getObjectOrNull (NOT, Schema.class);
    }

    // JSON Schema: subschemas conditional keyword if
    // JSON Schema: subschemas conditional keyword then
    // JSON Schema: subschemas conditional keyword else
    // JSON Schema: subschemas conditional keyword dependentSchemas

    /**
     * JSON Schema: subschemas array keyword
     */
    public Collection<Schema> getPrefixItems () {
        return getObjectsOrEmpty (PREFIX_ITEMS, Schema.class);
    }

    /**
     * JSON Schema: subschemas array keyword
     */
    public @Nullable Schema getItems () {
        return getObjectOrNull (ITEMS, Schema.class);
    }

    /**
     * JSON Schema: subschemas array keyword
     */
    public @Nullable Schema getContains () {
        return getObjectOrNull (CONTAINS, Schema.class);
    }

    /**
     * JSON Schema: subschemas object keyword
     */
    public Map<String, Schema> getProperties () {
        return getMapObjectsOrEmpty (PROPERTIES, Schema.class);
    }

    /**
     * JSON Schema: subschemas object keyword
     */
    public Map<String, Schema> getPatternProperties () {
        return getMapObjectsOrEmpty (PATTERN_PROPERTIES, Schema.class);
    }

    /**
     * JSON Schema: subschemas object keyword
     */
    public @Nullable Schema getAdditionalProperties () {
        return getObjectOrNull (ADDITIONAL_PROPERTIES, Schema.class);
    }

    /**
     * JSON Schema: subschemas object keyword
     */
    public @Nullable Schema getPropertyNames () {
        return getObjectOrNull (PROPERTY_NAMES, Schema.class);
    }

    // JSON Schema: subschemas unevaluated keyword unevaluatedItems
    // JSON Schema: subschemas unevaluated keyword unevaluatedProperties

    /**
     * JSON Schema Validation: validation keyword for any instance type
     */
    public Collection<String> getType () {
        final Object value = getRawValue (TYPE);
        if (value instanceof String) {
            return Collections.singletonList (getStringOrThrow (TYPE));
        } else if (value instanceof Collection) {
            return getStringsOrEmpty (TYPE);
        } else {
            throw new NoValueException ("todo"/*getSource ().getPath (TYPE)*/);
        }
    }

    /**
     * JSON Schema Validation: validation keyword for any instance type
     */
    // todo
    public @Nullable Collection<?> getEnum () {
        return getStringsOrEmpty (ENUM);
    }

    /**
     * JSON Schema Validation: validation keyword for any instance type
     */
    public @Nullable String getConst () {
        return getStringOrNull (CONST);
    }

    /**
     * JSON Schema Validation: validation Keywords for numeric instances (number and integer)
     */
    public @Nullable Number getMultipleOf () {
        return getNumberOrNull (MULTIPLE_OF);
    }

    /**
     * JSON Schema Validation: validation Keywords for numeric instances (number and integer)
     */
    public @Nullable Number getMaximum () {
        return getNumberOrNull (MAXIMUM);
    }

    /**
     * JSON Schema Validation: validation Keywords for numeric instances (number and integer)
     */
    public Boolean getExclusiveMaximum () {
        return getBooleanOrDefault (EXCLUSIVE_MAXIMUM, false);
    }

    /**
     * JSON Schema Validation: validation Keywords for numeric instances (number and integer)
     */
    public @Nullable Number getMinimum () {
        return getNumberOrNull (MINIMUM);
    }

    /**
     * JSON Schema Validation: validation Keywords for numeric instances (number and integer)
     */
    public Boolean getExclusiveMinimum () {
        return getBooleanOrDefault (EXCLUSIVE_MINIMUM, false);
    }

    /**
     * JSON Schema Validation: validation Keywords for strings
     */
    public @Nullable Number getMaxLength () {
        return getNumberOrNull (MAX_LENGTH);
    }

    /**
     * JSON Schema Validation: validation Keywords for strings
     */
    public @Nullable Number getMinLength () {
        return getNumberOrNull (MIN_LENGTH);
    }

    /**
     * JSON Schema Validation: validation Keywords for strings
     */
    public @Nullable String getPattern () {
        return getStringOrNull (PATTERN);
    }

    /**
     * JSON Schema Validation: validation Keywords for arrays
     */
    public @Nullable Integer getMaxItems () {
        return getIntegerOrNull (MAX_ITEMS);
    }

    /**
     * JSON Schema Validation: validation Keywords for arrays
     */
    public Integer getMinItems () {
        return getIntegerOrDefault (MIN_ITEMS, 0);
    }

    /**
     * JSON Schema Validation: validation Keywords for arrays
     */
    public Boolean getUniqueItems () {
        return getBooleanOrDefault (UNIQUE_ITEMS, false);
    }

    /**
     * JSON Schema Validation: validation Keywords for arrays
     */
    public @Nullable Integer getMaxContains () {
        return getIntegerOrNull (MAX_CONTAINS);
    }

    /**
     * JSON Schema Validation: validation Keywords for arrays
     */
    public Integer getMinContains () {
        return getIntegerOrDefault (MIN_CONTAINS, 1);
    }

    /**
     * JSON Schema Validation: validation Keywords for objects
     */
    public @Nullable Integer getMaxProperties () {
        return getIntegerOrNull (MAX_PROPERTIES);
    }

    /**
     * JSON Schema Validation: validation Keywords for objects
     */
    public Integer getMinProperties () {
        return getIntegerOrDefault (MIN_PROPERTIES, 0);
    }

    /**
     * JSON Schema Validation: validation Keywords for objects
     */
    public Collection<String> getRequired () {
        return getStringsOrEmpty (REQUIRED);
    }

    /**
     * JSON Schema Validation: validation Keywords for objects
     */
    public Map<String, Set<String>> getDependentRequired () {
        return getMapSetStringsOrEmpty (DEPENDENT_REQUIRED);
    }

    /**
     * JSON Schema Validation: semantic format
     */
    public @Nullable String getFormat () {
        return getStringOrNull (FORMAT);
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
        return getStringOrNull (TITLE);
    }

    /**
     * JSON Schema Validation: metadata
     */
    public @Nullable Object getDefault () {
        return getRawValue (DEFAULT);
    }

    /**
     * JSON Schema Validation: metadata
     */
    public Boolean getDeprecated () {
        return getBooleanOrDefault (DEPRECATED, false);
    }

    /**
     * JSON Schema Validation: metadata
     */
    public Boolean getReadOnly () {
        return getBooleanOrDefault (READ_ONLY, false);
    }

    /**
     * JSON Schema Validation: metadata
     */
    public Boolean getWriteOnly () {
        return getBooleanOrDefault (WRITE_ONLY, false);
    }

    /* todo examples
     * JSON Schema Validation: metadata
     */

    /**
     * OpenAPI base vocabulary
     */
    public @Nullable Discriminator getDiscriminator () {
        return getObjectOrNull (DISCRIMINATOR, Discriminator.class);
    }

    /**
     * OpenAPI base vocabulary
     */
    public @Nullable Xml getXml () {
        return getObjectOrNull (XML, Xml.class);
    }

    /**
     * OpenAPI base vocabulary
     */
    public @Nullable ExternalDocumentation getExternalDocs () {
        return getObjectOrNull (EXTERNAL_DOCS, ExternalDocumentation.class);
    }

    /**
     * OpenAPI base vocabulary
     */
    @Deprecated
    public @Nullable Object getExample () {
        return getRawValue (EXAMPLE);
    }

    /**
     * todo not required to have x- prefix
     */
    @Override
    public Map<String, Object> getExtensions () {
        return super.getExtensions ();
    }
}
