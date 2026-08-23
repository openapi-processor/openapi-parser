rootProject.name = "openapi-parser"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    includeBuild("build-logic")
}

dependencyResolutionManagement {
    versionCatalogs {
        create("build") {
            from(files("./gradle/build.versions.toml"))
        }
    }
}

plugins {
    id("io.github.ben-manes.versions.settings") version("0.61.0")
}

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
