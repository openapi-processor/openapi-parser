import org.gradle.accessors.dm.LibrariesForLibs

plugins {
    id("maven-publish")
    id("signing")
}

// see buildSrc/build.gradle catalog hack
val libs = the<LibrariesForLibs>()

val componentName = if (isPlatform()) { "javaPlatform" } else { "java" }

publishing {
    publications {
        create<MavenPublication>("openapiparser") {
            from(components[componentName])

            pom {
                group = "io.openapiprocessor"
                version = libs.versions.openapiparser.get()

                name.set("openapi-parser")
                description.set("OpenAPI 3.0/3.1 parser")
                url.set("https://github.com/openapi-processor/openapi-parser")

                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }

                developers {
                    developer {
                        id.set("hauner")
                        name.set("Martin Hauner")
                    }
                }

                scm {
                   url.set("https://github.com/openapi-processor/openapi-parser")
                }
            }
        }
    }

    repositories {
        maven {
            val releaseRepository = uri("https://ossrh-staging-api.central.sonatype.com/service/local/staging/deploy/maven2")
            val snapshotRepository = uri("https://central.sonatype.com/repository/maven-snapshots")
            url = uri(if (isReleaseVersion()) releaseRepository else snapshotRepository)

            credentials {
                username = buildProperty("PUBLISH_USER")
                password = buildProperty("PUBLISH_KEY")
            }
        }

        repositories {
            maven {
                name = "central"
                url = uri(rootProject.layout.buildDirectory.dir("central"))
            }
        }
    }
}

//tasks.withType<Sign>().configureEach {
//    onlyIf { isReleaseVersion() }
//}

/*
// does not work on oss.sonatype.org?
tasks.withType<GenerateModuleMetadata>().configureEach {
    enabled = false
}
*/

// signing requires the sign key and pwd as environment variables:
//
// ORG_GRADLE_PROJECT_signKey=...
// ORG_GRADLE_PROJECT_signPwd=...

signing {
    setRequired({
        gradle.taskGraph.allTasks.any {
            it.name.contains("ToSonatype") || it.name.contains("ToCentral")
        }
    })

    val signKey: String? by project
    val signPwd: String? by project
    useInMemoryPgpKeys(signKey, signPwd)

    sign(publishing.publications["openapiparser"])
}
