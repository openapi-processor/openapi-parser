plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    maven {
      url = uri("https://plugins.gradle.org/m2/")
    }
}

dependencies {
//    idea does not find the catalog
//    implementation(libs.plugin.kotlin)
//    implementation(libs.plugin.checker)
//    implementation(libs.plugin.outdated)

    implementation(libs("plugin.kotlin"))
    implementation(libs("plugin.checker"))
    implementation(libs("plugin.outdated"))
}

@Suppress("UnstableApiUsage")
fun libs(dependency: String): Provider<MinimalExternalModuleDependency> {
    val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
    return libs.findDependency(dependency).orElseThrow {
        Exception("can't find dependency $dependency")
    }
}
