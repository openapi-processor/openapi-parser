/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser;

import io.openapiparser.support.Strings;

import java.net.URI;
import java.util.*;

public class ReferenceResolver {
    private static final String RESOLVE_ERROR = "failed to resolve %s.";
    private static final String HASH = "#";

    private final URI baseUri;
    private final Reader reader;
    private final Converter converter;
    private final ReferenceRegistry references;
    private final DocumentRegistry documents = new DocumentRegistry ();

    private Node baseNode;

    public ReferenceResolver (
        URI baseUri, Reader reader, Converter converter, ReferenceRegistry references) {
        this.baseUri = baseUri;
        this.reader = reader;
        this.converter = converter;
        this.references = references;
    }

    public void resolve () throws ResolverException {
        baseNode = loadDocument (baseUri);
        documents.add (baseUri, baseNode);
        resolveReferences (baseUri, baseNode);
    }

    private void resolveReferences (URI uri, Node node) throws ResolverException {
        for (String k: node.getKeys ()) {
            if (k.equals (Keywords.REF)) {
                // handle ref
                String ref = node.getString (k);
                if (ref.startsWith (HASH)) {
                    // into same source
                } else {
                    // into other source
                    if (ref.contains (HASH)) {
                        String document = ref.substring(0, ref.indexOf(HASH));
                        URI documentUri = uri.resolve (document);

                        if (!documents.contains (documentUri)) {
                            Node documentNode = loadDocument (documentUri);
                            documents.add (documentUri, documentNode);
                            resolveReferences (documentUri, documentNode);
                        }

                        Node documentNode = documents.get (documentUri);
                        String fragment = ref.substring(ref.indexOf (HASH));
                        Object rawRefNode = getRawRefNode (documentNode, fragment);
                        references.add (baseUri, documentUri, ref, rawRefNode);
                    } else {
                        // source is object
                    }
                }
            } else {
//                if (baseNode.isChildNode(k)) {
//                    recurse
//                }
            }
        }

    }

    private Object getRawRefNode (Node documentNode, String fragment) {
        NodePathFinder finder = new NodePathFinder (documentNode);
        return finder.find (fragment.substring (1));
    }

    @Deprecated
    public void resolve(URI baseUri) throws ResolverException {
        try {
            // check document already loaded..
            baseNode = converter.convert (Strings.of (reader.read (baseUri)));
            documents.add(baseUri, baseNode);

            Set<String> keys = baseNode.getKeys ();
            for (String k: keys) {
                if (k.equals (Keywords.REF)) {
                    // handle ref
                    String ref = baseNode.getString (k);
                    if (ref.startsWith (HASH)) {
                        // link into same source
                    } else {
                        if (ref.contains (HASH)) {
                            // link into file
                            // foo.yaml#/foo

                            // split at #
                            // load file
                            String document = ref.substring(0, ref.indexOf(HASH));
                            URI documentUri = baseUri.resolve (document);

                            // rename to load document
                            resolve (documentUri);


//                            references.add (baseUri, documentUri, ref, rawRefNode);


                            // get ref object from node
                            // my need other documents...?

                        } else {
                            // file is object
                        }
                    }
                } else {
//                    if (baseNode.isChildNode(k)) {
                        // recurse
//                    }
                }
            }

            // todo
            // find references
            // resolve references
            // register references

        } catch (Exception e) {
            throw new ResolverException (String.format (RESOLVE_ERROR, baseUri), e);
        }
    }

    public Node getBaseNode () {
        return baseNode;
    }

    private Node loadDocument (URI documentUri) throws ResolverException {
        try {
            return converter.convert (Strings.of (reader.read (documentUri)));
        } catch (Exception e) {
            throw new ResolverException (String.format (RESOLVE_ERROR, documentUri), e);
        }
    }
}
