plugins {
    `kotlin-dsl`
}

dependencies {
    // catalog hack: https://github.com/gradle/gradle/issues/15383#issuecomment-779893192
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
    implementation(files(build.javaClass.superclass.protectionDomain.codeSource.location))

    implementation(build.plugin.build)
    implementation(build.plugin.kotlin)
    implementation(build.plugin.checker)
    implementation(build.plugin.updates)
    implementation(build.plugin.jacocolog)
}
