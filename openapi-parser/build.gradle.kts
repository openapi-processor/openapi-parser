plugins {
    id("openapi-parser.java-conventions")
    id("openapi-parser.publish-conventions")
}

//repositories {
//    maven {
//        url = uri("https://jitpack.io")
//    }
//}

dependencies {
    implementation(project(":openapi-parser-support"))
    implementation(project(":openapi-parser-validator"))
    implementation(project(":openapi-parser-interfaces"))
//    implementation("net.jimblackler.jsonschemafriend:core:0.11.2")

    testImplementation(project(":openapi-parser-snakeyaml"))
    testImplementation(project(":openapi-parser-jackson"))
    testImplementation(project(":memory-protocol"))
    testImplementation(platform(libs.jackson.bom))
    testImplementation(libs.jackson.databind)
    testImplementation(libs.jackson.yaml)
    testImplementation(libs.jackson.kotlin)
    testImplementation(libs.logback)
}

publishing {
    publications {
        getByName<MavenPublication>("openapiparser") {
            pom {
                name.set("openapi-parser")
                description.set("OpenAPI 3.0/3.1 parser")
            }
        }
    }
}

tasks {
    // groovy tests depend on kotlin tests
//    compileTestGroovy {
//        dependsOn(compileTestKotlin)
//        classpath += files(compileTestKotlin)
//    }
}

/*
configure<org.checkerframework.gradle.plugin.CheckerFrameworkExtension> {
//    skipCheckerFramework = true
//    excludeTests = true
    extraJavacArgs = listOf("-Awarns")

    checkers = listOf(
        "org.checkerframework.checker.nullness.NullnessChecker",
//        "org.checkerframework.checker.interning.InterningChecker",
//        "org.checkerframework.checker.resourceleak.ResourceLeakChecker",
//        "org.checkerframework.checker.index.IndexChecker"
    )
}
*/
