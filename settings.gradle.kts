pluginManagement {
    repositories {
<<<<<<< HEAD
=======
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://devrepo.kakao.com/nexus/content/groups/public/") }
>>>>>>> upstream/step0
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
        maven("https://devrepo.kakao.com/nexus/repository/kakaomap-releases/")
<<<<<<< HEAD
    }
}

rootProject.name = "android-map-notification"
=======
        maven("https://devrepo.kakao.com/nexus/content/groups/public/")
    }
}

rootProject.name = "android-map-refactoring"
>>>>>>> upstream/step0
include(":app")
