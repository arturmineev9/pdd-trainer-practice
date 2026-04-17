pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "PddTrainerPractice"
include(":app")
include(":core:common")
include(":core:model")
include(":feature:questions:api")
include(":feature:questions:impl")
include(":core:database")
include(":feature:statistics:impl")
include(":feature:statistics:api")
include(":feature:scanner:api")
include(":feature:scanner:impl")
