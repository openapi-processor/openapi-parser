plugins {
    id("openapi-parser.java-conventions")
}

dependencies {
    implementation(project(":openapi-parser-interfaces"))

    implementation(libs.snakeyaml)
}
