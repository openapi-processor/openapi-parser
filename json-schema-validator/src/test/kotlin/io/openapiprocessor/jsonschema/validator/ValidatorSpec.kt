package io.openapiprocessor.jsonschema.validator

import io.kotest.core.spec.style.FreeSpec

class ValidatorSpec: FreeSpec({
    /*
    //val converter = JacksonConverter()
    val json = ObjectMapper()
    json.registerModule(KotlinModule())

    val draft4 = ValidatorSpec::class.java.getResource("/suites/JSON-Schema-Test-Suite/tests/draft4")
    val root = Path.of(draft4.toURI())
    val paths = Files.walk(root)

    paths.filter { path -> !Files.isDirectory(path) }
        .forEach { path ->
            path.name - {
                val doc: Collection<Suite> = json.readValue(Files.readAllBytes(path),
                    json.typeFactory.constructCollectionType(List::class.java, Suite::class.java))

                for (item in doc) {
                    item.description - {
                        val schema: Map<String, Any> = item.schema as Map<String, Any>
                        val draft4Schema =
                            JsonSchemaObject(schema)

                        for (test in item.tests) {
                            test.description {
                                val validator =
                                    Validator()
                                val messages = validator.validate(draft4Schema, test.data)

                                if (test.valid) {
                                    messages.shouldBeEmpty()
                                } else {
                                    messages.shouldNotBeEmpty()
                                }

                            }
                        }
                    }
                }
            }
        }*/
})
