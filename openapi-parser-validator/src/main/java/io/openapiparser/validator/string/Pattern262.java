/*
 * Copyright 2022 https://github.com/openapi-processor/openapi-parser
 * PDX-License-Identifier: Apache-2.0
 */

package io.openapiparser.validator.string;

import io.openapiparser.schema.JsonInstance;
import io.openapiparser.schema.JsonSchema;
import io.openapiparser.validator.ValidationMessage;
//import org.jcodings.specific.UTF8Encoding;
//import org.joni.*;

//import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

//import static org.joni.Matcher.FAILED;

/**
 * Useless or bad configuration?
 *
 * It has more failures on the optional regex tests (ecmascript-regex.json) than {@link Pattern}.
 *
 * validates pattern.
 *
 * <p>See specification:
 * <a href="https://datatracker.ietf.org/doc/html/draft-fge-json-schema-validation-00#section-5.2.3">
 *     Draft 4: pattern
 * </a>
 */
public class Pattern262 {

    public Collection<ValidationMessage> validate (
        JsonSchema schema, JsonInstance instance) {

        Collection<ValidationMessage> messages = new ArrayList<> ();

        String pattern = schema.getPattern ();
        if (pattern == null)
            return messages;

        /*
        byte[] patternBytes = pattern.getBytes (StandardCharsets.UTF_8);
        Regex regex = new Regex (
            patternBytes, 0,
            patternBytes.length,
            Option.NONE,
            UTF8Encoding.INSTANCE
        );

        String instanceValue = instance.asString ();
        byte[] instanceBytes = instanceValue.getBytes (StandardCharsets.UTF_8);

        Matcher m = regex.matcher (instanceBytes);
        int valid = m.search (0, instanceBytes.length, Option.DEFAULT);
        if (valid != FAILED) {
            messages.add (new PatternError(instance.getPath (), pattern));
        }
         */

        return messages;
    }
}
