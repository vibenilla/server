plugins {
    java
    application
    id("com.gradleup.shadow") version "9.3.1"
}

group = "rocks.minestom"
version = "0.1.0"
application.mainClass = "rocks.minestom.Server"

java {
    toolchain.languageVersion = JavaLanguageVersion.of(25)
    withSourcesJar()
    withJavadocJar()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.minestom:minestom:2026.01.08-1.21.11")
    implementation("io.github.togar2:MinestomPvP:2025.12.29-1.21.11")
    implementation("rocks.minestom:placement:0.1.0")
    implementation("rocks.minestom:crafting:0.1.0")
    implementation("rocks.minestom:worldgen:0.1.0")

    // Logging
    implementation("org.tinylog:tinylog-api:2.8.0-M1")
    implementation("org.tinylog:tinylog-impl:2.8.0-M1")
    implementation("org.tinylog:slf4j-tinylog:2.8.0-M1")
}