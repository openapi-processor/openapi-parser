package io.openapiparser.validator

import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.StringSpec
import io.openapiparser.jackson.JacksonConverter
import io.openapiparser.support.Strings
import io.openapiparser.validator.JsonSchemaObject
import io.openapiparser.validator.Validator
//import net.jimblackler.jsonschemafriend.Schema
//import net.jimblackler.jsonschemafriend.SchemaStore
//import net.jimblackler.jsonschemafriend.ValidationError
//import net.jimblackler.jsonschemafriend.Validator

private const val OPENAPI_SCHEMA_30 =
    "https://raw.githubusercontent.com/OAI/OpenAPI-Specification/main/schemas/v3.0/schema.yaml"

private const val OPENAPI_SCHEMA_31 =
    "https://raw.githubusercontent.com/OAI/OpenAPI-Specification/main/schemas/v3.1/schema.yaml"

class ValidateSpec: StringSpec({

    // val mapper = ObjectMapper(YAMLFactory())
    // val yaml = mapper.readValue(OPENAPI_SCHEMA_30, Object::class.java)

    "test" {
        val mapper = ObjectMapper()

        val readValue1 = mapper.readValue("true", Object::class.java)
        val readValue2 = mapper.readValue("{}", Object::class.java)
        println ("$readValue1 $readValue2")
    }

    "null is valid" {
        val converter = JacksonConverter()
        val draftSource = ValidateSpec::class.java.getResourceAsStream("/json/draft-04/schema.json")
        val draft4Object = converter.convert(Strings.of(draftSource))!! as Map<String, Any>

        val draft4Schema =
            JsonSchemaObject(draft4Object)

        val validator = Validator()
        validator.validate(draft4Schema, draft4Object)
    }

    /*
    "foo2" {
        val converter = JacksonConverter()

        val schemaSource = URI.create(OPENAPI_SCHEMA_30).toURL().openStream()
        val schemaDocument = converter.convert(Strings.of(schemaSource))


        val schemaStore = SchemaStore()
        val schema = schemaStore.loadSchema(schemaDocument)
//        schemaStore.loadSchema(yaml)
        //val schema: Schema = schemaStore.loadSchema(URI.create(OPENAPI_SCHEMA_30))

        val apiSource = ValidateSpec::class.java.getResourceAsStream("/tests/ref-into-another-file/inputs/openapi.yaml")
        val apiDocument = converter.convert(Strings.of(apiSource))

        val validator = Validator {
            println(it)
            true
        }
        validator.validate(schema, apiDocument, Consumer<ValidationError> {
            val its = it.toString()
            println(its)
        })

//        {
//            println(it)
//        }
    }*/

})
