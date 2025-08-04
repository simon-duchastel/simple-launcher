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
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SimpleLauncher"
include(":modules:app")
include(":modules:libs:ui")
include(":modules:features:app-list")
include(":modules:features:homepage-action")
include(":modules:libs:sms")
include(":modules:libs:permissions")
include(":modules:features:homepage")
 