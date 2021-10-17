plugins {
    id("openapi-parser.java-conventions")
}

dependencies {
    implementation(platform("com.fasterxml.jackson:jackson-bom:2.12.5"))
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml")

    implementation(project(":openapi-parser-base"))
}
