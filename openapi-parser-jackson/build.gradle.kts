plugins {
    id("openapi-parser.java-conventions")
}

dependencies {
    implementation(project(":openapi-parser-base"))
    implementation(project(":openapi-parser-interfaces"))

    implementation(platform(libs.jackson.bom))
    implementation(libs.jackson.databind)
    implementation(libs.jackson.yaml)
}
