rootProject.name = "openapi-parser"

include("openapi-parser")
include("openapi-parser-bom")
include("json-schema-validator")
include("json-schema-validator-bom")
include("io-jackson")
include("io-jackson3")
include("io-snakeyaml")
include("io-interfaces")
include("memory-protocol")

System.setProperty("sonar.gradle.skipCompile", "true")
