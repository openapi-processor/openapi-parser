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
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.30")
//    implementation("org.checkerframework:checkerframework-gradle-plugin:0.6.2")
    // errors
    //implementation("org.unbroken-dome.gradle-plugins:gradle-testsets-plugin:4.0.0")
    //implementation("com.github.ben-manes:gradle-versions-plugin:0.39.0")
}
