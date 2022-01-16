import org.gradle.api.Project

fun Project.buildProperty(property: String): String {
    val prop: String? = findProperty(property) as String?
    if(prop != null) {
        return prop
    }

    val env: String? = System.getenv(property)
    if (env != null) {
        return env
    }

    return "n/a"
}

fun Project.buildSignKey(property: String): String {
    return buildProperty(property).replace("\\n", "\n")
}

fun Project.isReleaseVersion(): Boolean {
    return !(version.toString().endsWith("SNAPSHOT"))
}

fun Project.isPlatform(): Boolean {
    return pluginManager.hasPlugin("java-platform")
}
