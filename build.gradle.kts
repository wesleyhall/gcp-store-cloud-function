plugins {
    kotlin("jvm") version "1.9.22"
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "gcp.store.function"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("com.google.cloud.functions:functions-framework-api:1.1.0")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.+")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}