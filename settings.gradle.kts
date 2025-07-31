pluginManagement {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        maven(uri("https://jitpack.io"))
        maven(uri("https://s01.oss.sonatype.org"))
        maven(uri("https://oss.sonatype.org/content/repositories/snapshots/"))
        maven(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        google()
        maven(uri("https://jitpack.io"))
        maven(uri("https://s01.oss.sonatype.org"))
        maven(uri("https://oss.sonatype.org/content/repositories/snapshots/"))
        maven(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
    }
}

plugins {
    // Applies the foojay-resolver plugin to allow automatic download of JDKs.
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

rootProject.name = "JavaRedAlert2"

include(":game")
include(":awt")
include(":lwjgl3")
include(":libgdx")
include(":libgdx-lwjgl3")

include("base")
include("common")