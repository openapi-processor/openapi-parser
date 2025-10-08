/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiprocessor.jsonschema.schema.Keyword;
import io.openapiprocessor.jsonschema.schema.Keywords;
import io.openapiprocessor.jsonschema.schema.*;
import io.openapiprocessor.jsonschema.support.Types;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.net.URI;
import java.util.*;

import static io.openapiprocessor.jsonschema.support.Null.nonNull;
import static io.openapiprocessor.jsonschema.support.Null.requiresNonNull;
import static io.openapiprocessor.jsonschema.support.Types.*;
import static io.openapiprocessor.jsonschema.schema.Scope.createScope;

public class OpenApiBundler {
    private final Set<URI> walked = new HashSet<> ();

    private final Context context;
    private final DocumentStore documents;
    private final Bucket root;
    private final URI rootDocumentUri;
    private final Object rootDocument;
    private final OpenApiVersion version;


    private final Map<String, @Nullable Object> schemas = new LinkedHashMap<> ();
    private final Map<String, @Nullable Object> responses = new LinkedHashMap<> ();
    private final Map<String, @Nullable Object> parameters = new LinkedHashMap<> ();
    private final Map<String, @Nullable Object> examples = new LinkedHashMap<> ();
    private final Map<String, @Nullable Object> requestBodies = new LinkedHashMap<> ();
    private final Map<String, @Nullable Object> headers = new LinkedHashMap<> ();
    private final Map<String, @Nullable Object> securitySchemes = new LinkedHashMap<> ();
    private final Map<String, @Nullable Object> links = new LinkedHashMap<> ();
    private final Map<String, @Nullable Object> callbacks = new LinkedHashMap<> ();
    private final Map<String, @Nullable Object> pathItems = new LinkedHashMap<> ();
    private final Map<String, @Nullable Object> mediaTypes = new LinkedHashMap<> ();

    public OpenApiBundler (Context context, DocumentStore documents, Bucket root) {
        this.context = context;
        this.documents = documents.copy ();
        this.root = root;
        this.rootDocumentUri = root.getScope ().getDocumentUri ();
        this.rootDocument = nonNull(this.documents.get (rootDocumentUri));
        this.version = OpenApiVersionParser.parseVersion(rootDocument);
    }

    public Map<String, @Nullable Object> bundle () {
        Bucket bundled = createBucket(root.getScope(), rootDocument, root.getLocation());
        walkBucket (bundled);

        Map<String, @Nullable Object> rawValues = bundled.getRawValues ();
        mergeComponents (rawValues);
        return rawValues;
    }

    private void mergeComponents (Map<String, @Nullable Object> bundle) {
        Map<String, @Nullable Object> bundleComponents = asObject(bundle.get ("components"));
        if (bundleComponents == null) {
            bundleComponents = new LinkedHashMap<> ();

            mergeMaps (bundleComponents);

            if (!bundleComponents.isEmpty ()) {
                bundle.put ("components", bundleComponents);
            }
        } else {
            mergeMaps (bundleComponents);
        }
    }

    private void mergeMaps (Map<String, @Nullable Object> components) {
        mergeMap (components, "schemas", schemas);
        mergeMap (components, "responses", responses);
        mergeMap (components, "parameters", parameters);
        mergeMap (components, "examples", examples);
        mergeMap (components, "requestBodies", requestBodies);
        mergeMap (components, "headers", headers);
        mergeMap (components, "securitySchemes", securitySchemes);
        mergeMap (components, "links", links);
        mergeMap (components, "callbacks", callbacks);
        mergeMap (components, "pathItems", pathItems);
        mergeMap (components, "mediaTypes", mediaTypes);
    }

    private void mergeMap (
            Map<String, @Nullable Object> components,
            String property,
            Map<String, @Nullable Object> propertyValues
    ) {
        if (!propertyValues.isEmpty ()) {
            Map<String, @Nullable Object> bundlePaths = asObject(components.get (property));
            if (bundlePaths == null) {
                components.put (property, propertyValues);
            } else {
                bundlePaths.putAll (propertyValues);
            }
        }
    }

    private void walkBucket (Bucket bucket) {
        Scope scope = bucket.getScope ();
        JsonPointer location = bucket.getLocation ();
        SchemaVersion version = scope.getVersion ();

        List<Runnable> modifications = new ArrayList<> ();

        bucket.forEach ((name, value) -> {
            JsonPointer propLocation = location.append (name);
            Keyword keyword = version.getKeyword (name);

            if (keyword != null) {
                if (name.equals (Keywords.REF) && isString (value)) {
                    Runnable modify = walkRef (bucket, propLocation);
                    if (modify != null) {
                        modifications.add (modify);
                    }

                  // not OpenAPI 3.0
    //            } else if (name.equals (Keywords.RECURSIVE_REF) && isString (value)) {
    //                Ref ref = createRef (scope, name, value);
    //                walkRef (ref, propLocation);

                } else if (keyword.isNavigable() && keyword.isSchema () && isObject (value)) {
                    walkSchema (scope, value, propLocation);

                } else if (keyword.isNavigable() && keyword.isSchemaArray () && isArray (value)) {
                    walkSchemaArray (scope, value, propLocation);

                } else if (keyword.isNavigable() && keyword.isSchemaMap () && value != null) {
                    walkSchemaMap (scope, value, propLocation);
                }
            } else {
                if (isObject(value)) {
                    walkSchema(scope, value, propLocation);

                } else if (isArray(value)) {
                    walkSchemaArray(scope, value, propLocation);
                }
            }
        });

        modifications.forEach (Runnable::run);
    }

    private boolean isExternalDocument (URI documentUri) {
        return ! documentUri.equals (rootDocumentUri);
    }

    private @Nullable Runnable walkRef (Bucket bucket, JsonPointer location) {
        Map<String, @Nullable Object> bucketValues = bucket.getRawValues ();
        Reference reference = context.getReference (bucket);

        URI refUri = reference.getAbsoluteRefUri ();
        boolean loop = walked.contains (refUri);

        URI documentUri = reference.getDocumentUri ();
        boolean external = isExternalDocument (documentUri);
        Bucket documentBucket = createDocumentBucket(documentUri);

        JsonPointer refPointer = reference.getPointer ();
        String refName = refPointer.tail ();
        if (refName.isEmpty()) {
            refName = documentUri.getPath();
        }
        RawValue refValue = getRefValue (documentBucket, refPointer);
        Bucket refBucket = getRefBucket (refPointer, refValue);

        Runnable result = null;
        if (isSchemaRef (location, refBucket) && external) {
            bundleSchema (bucketValues, refName, refValue, loop);

        } else if (isResponsesRef (location) && external) {
            bundleResponse (bucketValues, refName, refValue);

        } else if (isParametersRef (location) && external) {
            bundleParameter (bucketValues, refName, refValue);

        } else if (isExamplesRef (location) && external) {
            bundleExample (bucketValues, refName, refValue);

        } else if (isRequestBodiesRef (location) && external) {
            bundleRequestBody (bucketValues, refName, refValue);

        } else if (isHeadersRef (location) && external) {
            bundleHeader (bucketValues, refName, refValue);

        } else if (isSecuritySchemesRef (location) && external) {
            result = bundleSecurityScheme (bucketValues, refValue);

        } else if (isLinksRef (location) && external) {
            bundleLink (bucketValues, refName, refValue);

        } else if (isCallbacksRef (location) && external) {
            bundleCallback (bucketValues, refName, refValue);

        } else if(isMediaTypeRef(location) && external) {
            if(version == OpenApiVersion.V32) {
                bundleMediaType(bucketValues, refName, refValue);
            }
        } else if (isPathRef (location) && external) {
            if(version == OpenApiVersion.V30) {
                result = bundlePathItem30(bucketValues, refValue);
            } else {
                bundlePathItem(bucketValues, refName, refValue);
            }
        }

        // walk unseen ref
        if (!loop) {
            walked.add (refUri);
            walkBucket (refBucket);
        }

        return result;
    }

    private void bundleSchema (Map<String, @Nullable Object> rawValues, String refName, RawValue refValue, boolean loop) {
        if (!loop) {
            schemas.put (refName, refValue.getValue ());
        }
        rawValues.put (Keywords.REF, createRefPointer ("schemas", refName));
    }

    private void bundleResponse (Map<String, @Nullable Object> rawValues, String refName, RawValue refValue) {
        responses.put (refName, refValue.getValue ());
        rawValues.put (Keywords.REF, createRefPointer ("responses", refName));
    }

    private void bundleParameter (Map<String, @Nullable Object> rawValues, String refName, RawValue refValue) {
        parameters.put (refName, refValue.getValue ());
        rawValues.put (Keywords.REF, createRefPointer ("parameters", refName));
    }

    private void bundleExample (Map<String, @Nullable Object> rawValues, String refName, RawValue refValue) {
        examples.put (refName, refValue.getValue ());
        rawValues.put (Keywords.REF, createRefPointer ("examples", refName));
    }

    private void bundleRequestBody (Map<String, @Nullable Object> rawValues, String refName, RawValue refValue) {
        requestBodies.put (refName, refValue.getValue ());
        rawValues.put (Keywords.REF, createRefPointer ("requestBodies", refName));
    }

    private void bundleHeader (Map<String, @Nullable Object> rawValues, String refName, RawValue refValue) {
        headers.put (refName, refValue.getValue ());
        rawValues.put (Keywords.REF, createRefPointer ("headers", refName));
    }

    private Runnable bundleSecurityScheme (Map<String, @Nullable Object> rawValues, RawValue refValue) {
        Map<String, @Nullable Object> replacement = asObject (refValue.getValue ());
        if (replacement == null) {
            throw new RuntimeException ();
        }

        // postpone in-place modification...  can't remove/add while iterating
        return () -> {
            rawValues.remove (Keywords.REF);
            rawValues.putAll (replacement);
        };
    }

    private void bundleLink (Map<String, @Nullable Object> rawValues, String refName, RawValue refValue) {
        links.put (refName, refValue.getValue ());
        rawValues.put (Keywords.REF, createRefPointer ("links", refName));
    }

    private void bundleCallback (Map<String, @Nullable Object> rawValues, String refName, RawValue refValue) {
        callbacks.put (refName, refValue.getValue ());
        rawValues.put (Keywords.REF, createRefPointer ("callbacks", refName));
    }

    private void bundlePathItem (Map<String, @Nullable Object> rawValues, String refName, RawValue refValue) {
        pathItems.put (refName, refValue.getValue ());
        rawValues.put (Keywords.REF, createRefPointer ("pathItems", refName));
    }

    private Runnable bundlePathItem30(Map<String, @Nullable Object> rawValues, RawValue refValue) {
        // OpenAPI 3.0 has no /components/paths => inline it

        Map<String, @Nullable Object> replacement = asObject (refValue.getValue ());
        if (replacement == null) {
            throw new RuntimeException ();
        }

        // todo, check that it contains only http methods!

        // postpone in-place modification...  can't remove/add while iterating
        return () -> {
            rawValues.remove (Keywords.REF);
            rawValues.putAll (replacement);
        };
    }

    private void bundleMediaType (Map<String, @Nullable Object> rawValues, String refName, RawValue refValue) {
        mediaTypes.put (refName, refValue.getValue ());
        rawValues.put (Keywords.REF, createRefPointer ("mediaTypes", refName));
    }

    private static RawValue getRefValue (Bucket documentBucket, JsonPointer refPointer) {
        RawValue refValue = documentBucket.getRawValue (refPointer);
        if (refValue == null) {
            throw new RuntimeException ();
        }
        return refValue;
    }

    private Bucket getRefBucket(JsonPointer refPointer, RawValue refValue) {
        return createBucket(refValue.getScope(), refValue.getValue(), refPointer);
    }

    private Bucket createDocumentBucket(URI documentUri) {
        Object document = documents.get (documentUri);
        if (document == null) {
            throw new BundleException (documentUri);
        }

        return createBucket(documentUri, document);
    }

    private Bucket createBucket(URI documentUri, Object document) {
        Scope scope = createScope (documentUri, document, context.getVersion());
        return createBucket(scope, document, JsonPointer.empty());
    }

    private Bucket createBucket(Scope scope, @Nullable Object source, JsonPointer location) {
        Bucket bucket = Bucket.createBucket(scope, source, location);
        if (bucket == null) {
            throw new BucketException(scope.getDocumentUri());
        }

        return bucket;
    }

    private String createRefPointer (String type, String refName) {
        return String.format ("#/components/%s/%s", type, JsonPointerSupport.encode(refName));
    }

    private void walkSchema (Scope currentScope, Object value, JsonPointer location) {
        Scope scope = currentScope.move (value);
        Bucket bucket = Bucket.createBucket(scope, value, location);
        if (bucket == null) {
            throw new BucketException(currentScope.getDocumentUri());
        }

        walkBucket (bucket);
    }

    private void walkSchemaArray (Scope currentScope, Object value, JsonPointer location) {
        Collection<Object> items = asArray (value);
        if (items == null) {
            return; // todo error
        }

        int index = 0;
        for (Object item : items) {
            JsonPointer itemLocation = location.append (index);
            if (!Types.isObject (item)) {
                continue;
            }

            walkSchema (currentScope, item, itemLocation);
            index++;
        }
    }

    private void walkSchemaMap (Scope currentScope, Object value, JsonPointer location) {
        Scope targetScope = currentScope.move (value);
        Bucket bucket = Bucket.createBucket(targetScope, value, location);
        if (bucket == null) {
            return; // // todo error
        }

        bucket.forEach ((propName, propValue) -> {
            JsonPointer propLocation = location.append (propName);
            walkSchema (targetScope, requiresNonNull(propValue), propLocation);
        });
    }

    private boolean isSchemaRef (JsonPointer location, Bucket bucket) {
//        Object rawType = bucket.getProperty (Keywords.TYPE);
//        if (rawType == null) {
//            return false;
//        }
//
//        if (!(rawType instanceof String)) {
//            return false;
//        };
//
//        String type = (String)rawType;
//
//        return type.equals ("object");

        List<String> tokens = location.getTokens ();

        // check for /**/schema/$ref
        boolean schema = tokens.size () > 2
            && tokens.get (tokens.size () - 2).equals ("schema")
            && tokens.get (tokens.size () - 1).equals (Keywords.REF);

        // check for /**/properties/*/$ref
        boolean property = tokens.size () > 3
            && tokens.get (tokens.size () - 3).equals ("properties")
            && tokens.get (tokens.size () - 1).equals (Keywords.REF);

        return schema || property;
    }

    private boolean isResponsesRef (JsonPointer location) {
        // check for /**/responses/*/$ref
        List<String> tokens = location.getTokens ();
        return tokens.size () > 3
            && tokens.get (tokens.size () - 3).equals ("responses")
            && tokens.get (tokens.size () - 1).equals (Keywords.REF);
    }

    private boolean isParametersRef (JsonPointer location) {
        // check for /**/parameters/*/$ref
        List<String> tokens = location.getTokens ();
        return tokens.size () > 3
            && tokens.get (tokens.size () - 3).equals ("parameters")
            && tokens.get (tokens.size () - 1).equals (Keywords.REF);
    }

    private boolean isExamplesRef (JsonPointer location) {
        // check for /**/examples/*/$ref
        List<String> tokens = location.getTokens ();
        return tokens.size () > 3
            && tokens.get (tokens.size () - 3).equals ("examples")
            && tokens.get (tokens.size () - 1).equals (Keywords.REF);
    }

    private boolean isRequestBodiesRef (JsonPointer location) {
        // check for /**/requestBody/$ref
        List<String> tokens = location.getTokens ();
        return tokens.size () > 2
            && tokens.get (tokens.size () - 2).equals ("requestBody")
            && tokens.get (tokens.size () - 1).equals (Keywords.REF);
    }

    private boolean isHeadersRef (JsonPointer location) {
        // check for /**/headers/*/$ref
        List<String> tokens = location.getTokens ();
        return tokens.size () > 3
            && tokens.get (tokens.size () - 3).equals ("headers")
            && tokens.get (tokens.size () - 1).equals (Keywords.REF);
    }

    private boolean isSecuritySchemesRef (JsonPointer location) {
        // check for /components/securitySchemes/*/$ref
        List<String> tokens = location.getTokens ();
        return tokens.size () > 3
            && tokens.get (tokens.size () - 3).equals ("securitySchemes")
            && tokens.get (tokens.size () - 1).equals (Keywords.REF);
    }

    private boolean isLinksRef (JsonPointer location) {
        // check for /**/links/*/$ref
        List<String> tokens = location.getTokens ();
        return tokens.size () > 3
            && tokens.get (tokens.size () - 3).equals ("links")
            && tokens.get (tokens.size () - 1).equals (Keywords.REF);
    }

    private boolean isCallbacksRef (JsonPointer location) {
        // check for /**/callbacks/*/$ref
        List<String> tokens = location.getTokens ();
        return tokens.size () > 3
            && tokens.get (tokens.size () - 3).equals ("callbacks")
            && tokens.get (tokens.size () - 1).equals (Keywords.REF);
    }

    private boolean isPathRef (JsonPointer location) {
        // check for /paths/*/$ref
        List<String> tokens = location.getTokens ();
        return tokens.size () == 3
            && tokens.get (0).equals ("paths")
            && tokens.get (2).equals (Keywords.REF);
    }

    private boolean isMediaTypeRef (JsonPointer location) {
        // check for /**/content/*/$ref
        List<String> tokens = location.getTokens ();
        return tokens.size () > 3
            && tokens.get (tokens.size () - 3).equals ("content")
            && tokens.get (tokens.size () - 1).equals (Keywords.REF);
    }
}
