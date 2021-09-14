package io.openapiparser.jackson;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import io.openapiparser.Converter;
import io.openapiparser.ConverterException;
import io.openapiparser.support.Strings;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link Converter} based on jackson.
 */
public class JacksonConverter implements Converter {
    private static final String CONVERT_ERROR = "failed to convert %s document.";

    private static final Pattern FIRST_CHAR = Pattern.compile("\\s*(.)");

    private static final ObjectMapper json = new ObjectMapper();
    private static final ObjectMapper yaml = new ObjectMapper(new YAMLFactory ());

    public Map<String, Object> convert (String api) throws ConverterException {
        if (Strings.isEmpty (api)) {
            throw new ConverterException (String.format (CONVERT_ERROR, "empty"), null);
        }

        if (isJson (api)) {
            return convertJson (api);
        } else {
            return convertYaml (api);
        }
    }

    private Map<String, Object> convertJson (String api) throws ConverterException {
        try {
            return json.readValue (api, getMapTypeReference ());
        } catch (Exception e) {
            throw new ConverterException (String.format (CONVERT_ERROR, "json"), e);
        }
    }

    private Map<String, Object> convertYaml (String api) throws ConverterException {
        try {
            return yaml.readValue (api, getMapTypeReference ());
        } catch (Exception e) {
            throw new ConverterException (String.format (CONVERT_ERROR, "yaml"), e);
        }
    }

    private TypeReference<Map<String, Object>> getMapTypeReference () {
        return new TypeReference<Map<String, Object>> () {};
    }

    private boolean isJson (String source) {
        return "{".equals(getFirstChar (source));
    }

    private static String getFirstChar (String content) {
      Matcher matcher = FIRST_CHAR.matcher(content);
      if (matcher.find()) {
        return matcher.group(1);
      }
      return null;
    }

}
