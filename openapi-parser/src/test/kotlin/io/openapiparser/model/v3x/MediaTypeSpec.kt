/*
 * Copyright 2021 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.model.v3x

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.nulls.shouldNotBeNull
import io.openapiparser.model.v30.mediaType as mediaType30
import io.openapiparser.model.v31.mediaType as mediaType31

class MediaTypeSpec: StringSpec({

    "gets media type schema" {
        mediaType30("schema: {}").schema.shouldNotBeNull()
        mediaType31("schema: {}").schema.shouldNotBeNull()
    }

    "gets media type schema is null if missing" {
        mediaType30().schema.shouldBeNull()
        mediaType31().schema.shouldBeNull()
    }


})
