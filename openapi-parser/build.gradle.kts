plugins {
    groovy
    id("openapi-parser.java-conventions")
    id("org.unbroken-dome.test-sets") version "4.0.0"
    id("com.github.ben-manes.versions") version "0.39.0"
    id( "org.checkerframework") version "0.6.3"
}

dependencies {
    implementation(project(":openapi-parser-base"))

    testImplementation(project(":openapi-parser-jackson"))
    testImplementation("org.spockframework:spock-core:2.1-M2-groovy-3.0")
}

tasks {
    // groovy tests depend on kotlin tests
    compileTestGroovy {
        dependsOn(compileTestKotlin)
        classpath += files(compileTestKotlin)
    }
}

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
