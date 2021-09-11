plugins {
    id("openapi-parser.java-conventions")
    id("org.unbroken-dome.test-sets") version "4.0.0"
    id("com.github.ben-manes.versions") version "0.39.0"
}

dependencies {
    implementation(project(":openapi-parser-api"))
}
