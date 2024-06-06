import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization") version "1.9.22"
}

group = "de.oskarschulz"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation("com.mikepenz:multiplatform-markdown-renderer-jvm:0.20.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.9.3")
}

compose.desktop {
    application {
        mainClass = "MainKt"
        javaHome = javaToolchains.launcherFor {
            languageVersion.set(JavaLanguageVersion.of(17))
        }.map { it.metadata.installationPath }.get().asFile.absolutePath
        nativeDistributions {
            targetFormats(TargetFormat.AppImage, TargetFormat.Exe)
            packageName = "VoteKT"
            packageVersion = "1.0.0"
        }
    }
}

