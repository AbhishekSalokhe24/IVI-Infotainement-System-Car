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

rootProject.name = "IVI-Infotainment"

// App Shell
include(":app")

// Features
include(":features:media")
include(":features:navigation")
include(":features:climate")
include(":features:vehicle")
include(":features:phone")
include(":features:settings")

// Libraries
include(":libraries:designsystem")
include(":libraries:carservice")
include(":libraries:core")
include(":libraries:testing")
