pluginManagement {
    repositories {
        google()
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

rootProject.name = "PersonalLifeApp"

// App module
include(":app")

// Core modules
include(":core:common")
include(":core:designsystem")
include(":core:model")
include(":core:navigation")
include(":core:data")
include(":core:testing")

// Feature modules
include(":feature:auth")
include(":feature:dashboard")
include(":feature:profile")
include(":feature:settings")
include(":feature:modulemanager")

// Widget Platform modules
include(":core:widgetengine")
include(":core:widgetdata")
include(":core:widgetpreview")
include(":feature:widgetstudio")

