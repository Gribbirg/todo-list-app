rootProject.name = "TodoApp"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
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
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include(":app")
include(":feature:list")
include(":feature:edit")
include(":domain")
include(":core:data")
include(":core:utils")
include(":core:network")
include(":core:db")
include(":core:ui")
