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
        maven("https://devrepo.kakao.com/nexus/repository/kakaomap-releases/")
<<<<<<< HEAD
    }
}

rootProject.name = "android-map-notification"
include(":app")
=======
        maven("https://devrepo.kakao.com/nexus/content/groups/public/") //카카오 sdk
    }
}

rootProject.name = "android-map-refactoring"
include(":app")

>>>>>>> refactoring/step_2
