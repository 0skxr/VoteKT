pluginManagement {
    dependencyResolutionManagement {
        repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
        repositories {
            google()
            mavenCentral()
            gradlePluginPortal()
            maven {
                setUrl("https://oss.sonatype.org/content/repositories/snapshots")
            }
            maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
            google()
            gradlePluginPortal()
            mavenCentral()
        }
    }

    plugins {
        kotlin("jvm").version(extra["kotlin.version"] as String)
        id("org.jetbrains.compose").version(extra["compose.version"] as String)
    }
}

rootProject.name = "VoteKT"
