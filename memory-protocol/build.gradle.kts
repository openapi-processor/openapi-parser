plugins {
    id("openapi-parser.java-conventions")
}

tasks.withType<JavaCompile>().configureEach {
    options.release.set(11)
}
