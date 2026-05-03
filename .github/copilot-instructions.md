# AI Coding Agent Instructions for openapi-parser

## Project Overview

**openapi-parser** is a Java-based OpenAPI 3.0.x & 3.1 parser and validator with pluggable YAML/JSON converters and document readers. It's organized as a multi-module Gradle project supporting both OpenAPI 3.0 and 3.1 specifications with separate versioned APIs.

## Key Architecture

### Core Modules (from [settings.gradle.kts](settings.gradle.kts))
- **openapi-parser** - Main parser module (depends on json-schema-validator, io-interfaces)
- **json-schema-validator** - JSON Schema validation for OpenAPI documents
- **io-interfaces** - Plugin interfaces: `Reader` (document loading), `Converter` (YAML/JSON parsing), `Writer`
- **io-jackson** & **io-snakeyaml** - Pluggable converter implementations
- **memory-protocol** - In-memory resource handling
- **openapi-parser-bom** & **json-schema-validator-bom** - Bill of Materials (dependency management)

### Data Flow
1. `DocumentLoader` loads document by URI using pluggable `Reader` + `Converter`
2. `OpenApiParser.parse()` detects version (3.0 vs 3.1) by reading "openapi" field
3. `Resolver` resolves `$ref` references (uses Draft4 for 3.0, Draft202012 for 3.1)
4. Version-specific result returned: `OpenApiResult30` or `OpenApiResult31`
5. User calls `result.getModel(OpenApi.class)` to get navigable model tree

### Plugin Architecture
All I/O abstraction via interfaces in [io-interfaces](io-interfaces/):
- Implement `Reader` for custom document loading (default: `UriReader`)
- Implement `Converter` to support new YAML/JSON parsers (default: Jackson & SnakeYAML)
- Implement `Writer` to output documents (JSON/YAML writers available)

## Build & Test Workflow

### Gradle Commands
```bash
# Build with coverage (default)
./gradlew build

# Run specific module tests
./gradlew :openapi-parser:test
./gradlew :json-schema-validator:test

# Check dependency versions for updates
./gradlew dependencyUpdates

# View coverage reports
./gradlew jacocoLogAggregatedCoverage

# Check specific dependency
./gradlew dependencyInsight --dependency org.slf4j:slf4j-api --configuration testRuntimeClasspath
```

### Test Framework
- **JUnit 5** (Jupiter) for test execution
- **Kotest** (StringSpec style) with table-driven tests (`kotest-table`)
- **MockK** for mocking in Kotlin tests
- Kotlin test files use `Spec` suffix (e.g., `OpenApiParserSpec.kt`)

### Convention Plugins (buildSrc)
Three apply to all modules:
- **openapiparser.library** - JVM toolchain (Java 11 build, 17 test), Checker Framework (null safety), JaCoCo (coverage)
- **openapiparser.test** - JUnit 5, Kotest, MockK setup
- **openapiparser.publish** - Maven Central publishing

## Project Conventions & Patterns

### Version Management
- Detected via `openapi` field in YAML/JSON: `3.0.x` → `OpenApiResult30`, `3.1.x` → `OpenApiResult31`
- Separate model packages: `io.openapiparser.model.v30.*` vs `io.openapiparser.model.v31.*`
- Use generic `OpenApiResult` interface in public APIs, cast to version-specific when needed

### Error Handling
- `ParserException` - wraps parsing failures with document URI for context
- `ConverterException` - YAML/JSON conversion failures
- `UnknownVersionException` - unsupported OpenAPI versions

### Testing Patterns (examples from [OpenApiParserSpec.kt](openapi-parser/src/test/kotlin/io/openapiparser/OpenApiParserSpec.kt), [ApiBuilder.kt](openapi-parser/src/test/kotlin/io/openapiparser/support/ApiBuilder.kt))
- Use `ApiBuilder` test utility: fluent API to build parsers with inline YAML
- `buildParser()` → `parser.parse(URI)` → `result.getModel(OpenApi.class)`
- Test resources in `src/test/resources/` organized by feature (e.g., `bundle-ref-schema/`)
- Table-driven tests with Kotest for multiple cases

### Null Safety
- Checker Framework annotations: `@Nullable`, `@NonNull` on methods returning/accepting potentially null values
- Use `nonNull()` helper (from jsonschema support) for assertions in critical paths
- Default values: missing optional properties return false/empty collection per OpenAPI spec

## Integration Points & Dependencies

### Key External Libraries
- **Jackson 2.20** (jackson-bom) - JSON/YAML parsing (io-jackson module)
- **SnakeYAML 2.5** - Alternative YAML parser (io-snakeyaml module)
- **jsonpath 2.9** - Document traversal (optional, for overlay features)
- **SLF4J 2.0.17** + Logback 1.5.18 - Logging

### JSON Schema Validator Integration
- Embedded JSON Schema validator for OpenAPI validation
- Supports multiple JSON Schema drafts (Draft 4, Draft 6, Draft 7, Draft 202012)
- Test suites in [json-schema-validator/src/test/resources/suites/](json-schema-validator/src/test/resources/suites/)

### Overlay & Bundling (Experimental)
- `OverlayParser` handles Overlay 1.0 specifications
- `OpenApiBundler` combines multi-file references into single document
- `OverlayApplier` applies overlay transformations using JSONPath

## Documentation & References

- [Main README](README.adoc) - overview, usage examples (current 2023.3 API)
- [openapi-parser README](openapi-parser/README.adoc) - detailed API usage with code examples
- Module READMEs - specific implementation details
- Published on Maven Central: `io.openapiprocessor:openapi-parser:*`

## Common Development Tasks

**Adding a new OpenAPI feature**: Add to model classes, update converters in json-schema-validator
**Adding yaml/json parser**: Implement `Converter` interface in new module (see io-jackson/jackson pattern)
**Adding custom document loading**: Implement `Reader` interface, pass to `DocumentLoader`
**Testing multi-file OpenAPI**: Use `DocumentStore` to preload documents, pass to parser
**Performance**: Resolver caches $refs; reuse `DocumentLoader` across parses when possible
