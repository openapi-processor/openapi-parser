plugins {
    id("openapi-parser.java-conventions")
}

dependencies {
    implementation(project(":openapi-parser-interfaces"))

    testImplementation(project(":openapi-parser-memory"))
}
