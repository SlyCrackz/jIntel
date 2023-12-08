plugins {
    id("java")
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
