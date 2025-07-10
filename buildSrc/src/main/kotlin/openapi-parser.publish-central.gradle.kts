import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublisher
import java.net.http.HttpRequest.BodyPublishers.*
import java.net.http.HttpResponse.BodyHandlers
import java.nio.charset.StandardCharsets
import java.nio.file.Path
import java.util.*

interface PublishCentralPluginExtension {
    val username: Property<String>
    val password: Property<String>
    val stagingDir: DirectoryProperty
}

project.extensions.create<PublishCentralPluginExtension>("centralPublishing")

tasks.register<Zip>("generateUploadBundle") {
    group = "publishing central"

    archiveBaseName = "bundle"
    destinationDirectory = layout.buildDirectory.dir("deployment")

    val extension = project.extensions.findByType(PublishCentralPluginExtension::class)!!
    from(extension.stagingDir)
}

tasks.register<PublishTask>("publishToMavenCentral") {
    group = "publishing central"
    dependsOn(tasks.getByPath("generateUploadBundle"))

    val extension = project.extensions.findByType(PublishCentralPluginExtension::class)!!
    username.value(extension.username)
    password.value(extension.password)
    stagingDir.value(extension.stagingDir)
}

@Suppress("unused")
enum class DeploymentState {
    PENDING,
    VALIDATING,
    VALIDATED,
    PUBLISHING,
    PUBLISHED,
    FAILED
}

@Serializable
data class Status(
    val deploymentId: UUID,
    val deploymentName: String,
    val deploymentState: DeploymentState,
    val purls: Set<String>
) {
    val published: Boolean = deploymentState == DeploymentState.PUBLISHED
}


abstract class PublishTask : DefaultTask() {

    @Input
    val username = project.objects.property<String>()

    @Input
    val password = project.objects.property<String>()

    @InputDirectory
    val stagingDir = project.objects.directoryProperty()

    @TaskAction
    fun publish() {
        val deploymentId = uploadBundle()

        var count = 0
        while (count < 8) {
            Thread.sleep(10_000)
            count++

            val status = checkStatus(deploymentId)
            if (status.published) {
                logger.lifecycle("successfully published bundle: {}", status.deploymentId)
                return
            }
        }
    }

    private fun checkStatus(deploymentId: String): Status {
        val client = HttpClient.newHttpClient()
        val token = createBearerToken()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://central.sonatype.com/api/v1/publisher/status?id=$deploymentId"))
            .header("Authorization", token)
            .POST(noBody())
            .build()

        val response = client.send(request, BodyHandlers.ofString(Charsets.UTF_8))
        val statusCode = response.statusCode()
        val responseBody = response.body()

        if (statusCode != 200) {
            throw GradleException(
                String.format("failed to check status: status (%d) / body (%s)", statusCode, responseBody))
        } else {
            val status = Json.decodeFromString<Status>(responseBody)
            logger.lifecycle("successfully checked status => {}", status.deploymentState)
            return status
        }
    }

    private fun uploadBundle(): String {
        val client = HttpClient.newHttpClient()
        val token = createBearerToken()

        val bundle = getBundle()
        val boundary = createBoundary()
        val requestBody = buildMultipartBody(boundary, bundle)

        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://central.sonatype.com/api/v1/publisher/upload?publishingType=USER_MANAGED"))
            .header("Authorization", token)
            .header("Content-Type", "multipart/form-data; boundary=$boundary")
            .POST(requestBody)
            .build()

        val response = client.send(request, BodyHandlers.ofString(Charsets.UTF_8))
        val statusCode = response.statusCode()
        val responseBody = response.body()

        if (statusCode != 201) {
            throw GradleException(
                String.format("failed to upload bundle: status (%d) / body (%s)", statusCode, responseBody))
        } else {
            logger.lifecycle("successfully uploaded bundle file: {}", bundle)
            logger.lifecycle("deployment id: {}",responseBody)
            return responseBody
        }
    }

    private fun createBearerToken(): String {
        val credentials = "${username.get()}:${password.get()}"
        val bytes = credentials.toByteArray(StandardCharsets.UTF_8)
        val encoded = Base64.getEncoder().encodeToString(bytes)
        return "Bearer $encoded"
    }

    private fun createBoundary(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }

    private fun getBundle(): Path {
        val zip = project.tasks.getByPath("generateUploadBundle") as Zip
        return zip.archiveFile.get().asFile.toPath()
    }

    private fun buildMultipartBody(boundary: String, file: Path): BodyPublisher {
        val crlf = "\r\n"
        val marker = "--"

        val fileName = file.fileName.toString()

        val boundaryStart = """$crlf$marker$boundary$crlf"""
        val fileStartCd = """Content-Disposition: form-data; name="bundle"; filename="$fileName"$crlf"""
        val fileStartCt = """Content-Type: application/octet-stream$crlf$crlf"""
        val boundaryEnd = """$crlf$marker$boundary$marker$crlf"""

        return concat(
            ofString(boundaryStart),
            ofString(fileStartCd),
            ofString(fileStartCt),
            ofFile(file),
            ofString(boundaryEnd))
    }
}
