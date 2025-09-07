import io.openapiprocessor.build.core.dsl.initFrom
import io.openapiprocessor.build.core.dsl.initSignKey
import io.openapiprocessor.build.core.dsl.sonatype
import io.openapiprocessor.build.core.getPomProperties
import org.gradle.accessors.dm.LibrariesForLibs

plugins {
   id("io.openapiprocessor.build.plugin.publish-central")
}

// see buildSrc/build.gradle.kts
val libs = the<LibrariesForLibs>()

val componentName = if (isPlatform()) { "javaPlatform" } else { "java" }

publishing {
    publications {
        create<MavenPublication>("openapiparser") {
            from(components[componentName])

            pom {
                pom.initFrom(getPomProperties(project))
            }
        }
    }

    repositories {
        sonatype(project)
    }
}

signing {
    initSignKey()
    sign(publishing.publications["openapiparser"])
}

publishProcessor {
    publish = true
}
