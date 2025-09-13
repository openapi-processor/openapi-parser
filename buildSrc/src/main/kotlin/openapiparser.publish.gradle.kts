plugins {
    `maven-publish`
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
    publication = "openapiparser"
    stagingDir = rootProject.layout.buildDirectory.dir("staging")
    deploymentDir = rootProject.layout.buildDirectory.dir("deployment")
    deploymentName = "parser"
}
