/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiprocessor.jsonschema.schema

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.mockk.mockk
import io.openapiprocessor.jsonschema.support.Uris

class SchemaStoreSpec: StringSpec({

        "throws when requesting a json schema from an un-registered uri" {
            val loader = mockk<DocumentLoader>()
            val store = SchemaStore(loader)

            shouldThrow<NotRegisteredException> {
                store.getSchema(Uris.createUri("https://some.scheam.uri"))
            }
        }

})
