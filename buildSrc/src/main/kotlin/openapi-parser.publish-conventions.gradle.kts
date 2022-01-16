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
            val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2"
            val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots"
            url = uri(if (isReleaseVersion()) releasesRepoUrl else snapshotsRepoUrl)

            credentials {
                username = buildProperty("PUBLISH_USER")
                password = buildProperty("PUBLISH_USER")
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

signing {
    useInMemoryPgpKeys(buildSignKey("SIGN_KEY"), buildProperty("SIGN_PWD"))
    sign(publishing.publications["openapiparser"])
}


//nexusStaging {
//    username = publishUser
//    password = publishKey
//}