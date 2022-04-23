/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.support

import io.kotest.core.spec.style.StringSpec
import io.kotest.data.blocking.forAll
import io.kotest.data.row
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue


class UriValidatorSpec : StringSpec({

    forAll(
        // scheme
        row("a:", "first scheme char should be alpha", true),
        row("1:", "first scheme char should be alpha", false),
        row("A:", "scheme allows uppercase or lowercase chars", true),
        row("aaAzZ.+-1:", "scheme with valid chars",true),
        row("", "scheme should not be empty", false),
        row("a!:", "should not contain invalid char", false),
        row("a/:", "should not contain invalid char", false),
        row("a?:", "should not contain invalid char", false),
        row("a#:", "should not contain invalid char", false),

        // authority
        row("any:", "authority may be empty", true),
        row("any://any", "authority must start with //", true),
        row("any://user@any", "authority may contain userinfo", true),
        row("any://-.~_!$&'()*+,;=:%40:80%2f::::::@example.com", "authority userinfo allows special chars", true),
        row("any://any:123", "authority may contain port", true),
        row("any://user@any:123", "authority may contain userinfo & port", true),
        row("any://[2001:db8::7]", "authority with ipv6", true),
        row("any://[2001:db8::7]:123", "authority with ipv6 & port", true),
        row("any://1.2.3.4", "authority with ipv4", true),
        row("any://1.2.3.4:123", "authority with ipv4 & port", true),

    ) { uri, description, valid  ->
        "<$uri> - $description" {
            if (valid)
                UriValidator(uri).validateAbsolute().shouldBeTrue()
            else
                UriValidator(uri).validateAbsolute().shouldBeFalse()
        }
    }

    forAll(
        // scheme
        row("//foo.bar", "protocol relative with empty path", true),
        row("//foo.bar/", "protocol relative starts with / path", true),
        row("//", "no authority should not start with //", false),
        row("", "absolute empty", true),
        row("/", "absolute empty", true),
        row("/a", "absolute unreserved", true),
        row("/%20", "absolute percent encoded", true),
        row("/%2", "absolute bad percent encoded", false),
        row("/:", "absolute colon", true),
        row("/@", "absolute at", true),
        row("/!", "absolute sub-delims", true),
        row("\\", "invalid char", false),
    ) { uri, description, valid  ->
        "<$uri> - $description" {
            if (valid)
                UriValidator(uri).validate().shouldBeTrue()
            else
                UriValidator(uri).validate().shouldBeFalse()
        }
    }
})
