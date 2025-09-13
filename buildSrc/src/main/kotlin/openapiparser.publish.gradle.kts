plugins {
    `maven-publish`
    signing
   id("io.openapiprocessor.build.plugin.publish")
}

val componentName = if (isPlatform()) { "javaPlatform" } else { "java" }

publishing {
    publications {
        create<MavenPublication>("openapiparser") {
            from(components[componentName])
        }
    }
}

publishingCentral {
    stagingDir = rootProject.layout.buildDirectory.dir("staging")
    deploymentDir = rootProject.layout.buildDirectory.dir("deployment")
    deploymentName = "parser"
    waitFor = "VALIDATED"
}
