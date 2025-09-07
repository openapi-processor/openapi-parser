import org.gradle.api.Project

fun Project.isPlatform(): Boolean {
    return pluginManager.hasPlugin("java-platform")
}
