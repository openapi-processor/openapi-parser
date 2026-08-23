import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

abstract class GenerateManifestTask : DefaultTask() {
    @get:InputFiles
    abstract val testFiles: ConfigurableFileCollection

    @get:InputDirectory
    abstract val baseResourcesDir: DirectoryProperty

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @TaskAction
    fun generate() {
        val baseDirFile = baseResourcesDir.get().asFile
        val paths = testFiles.files
            .map { file -> "/" + file.relativeTo(baseDirFile).path }
            .sortedWith(compareBy<String> { path -> path.count { it == '/' } }.thenBy { it })

        val suitesFolder = outputDir.get().asFile.resolve("suites")
        suitesFolder.mkdirs()

        suitesFolder.resolve("JSON-Schema-Test-Suite.txt").writeText(paths.joinToString("\n"))
    }
}
