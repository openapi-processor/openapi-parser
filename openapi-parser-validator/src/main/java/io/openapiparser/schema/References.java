/*
 * Copyright 2023 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.schema;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class References {
    private static class RefEntry {
        Ref ref;

        public RefEntry (Ref ref) {
            this.ref = ref;
        }

        @Override
        public boolean equals (@Nullable Object o) {
            if (this == o)
                return true;

            if (o == null || getClass () != o.getClass ())
                return false;

            RefEntry pending = (RefEntry) o;
            return ref.getFullRefUri ().equals (pending.ref.getFullRefUri ());
        }

        @Override
        public int hashCode () {
            return Objects.hash (ref.getFullRef ());
        }

        @Override
        public String toString () {
            return ref.getFullRef ();
        }
    }

    private final Set<RefEntry> references = new HashSet<> ();

    public void add (Ref ref) {
        references.add (new RefEntry (ref));
    }

    /**
     * loop the references.
     *
     * @param consumer consumer callback that handles the ref
     */
    public void each (Consumer<Ref> consumer) {
        for (RefEntry next : references) {
            consumer.accept (next.ref);
        }
    }
}
