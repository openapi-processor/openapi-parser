plugins {
    id("openapiparser.test")
    id("openapiparser.library")
}

tasks.named("jacocoLogTestCoverage").configure {
    enabled = false
}
