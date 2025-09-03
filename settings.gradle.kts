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
include(":modules:features:homepage")
include(":modules:features:settings")
include(":modules:features:app-widgets")
include(":modules:libs:core-ext")
include(":modules:libs:sms")
include(":modules:libs:permissions")
include(":modules:libs:contacts")
include(":modules:libs:intents")
include(":modules:libs:phone-number")
include(":modules:libs:emoji")
 