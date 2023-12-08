plugins {
    java
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "com.crackz"
version = "a-0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.11")
    implementation("com.formdev:flatlaf:3.2.5")
}

tasks.shadowJar {
    archiveBaseName.set("jIntel")
    archiveClassifier.set("fat")
    archiveVersion.set(version.toString())

    manifest {
        attributes["Main-Class"] = "jIntel" // Replace with your main class
    }
}
