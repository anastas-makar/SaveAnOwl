pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven(url = "https://artifactory-external.vkpartner.ru/artifactory/vkid-sdk-android/")
        maven(url = "https://artifactory-external.vkpartner.ru/artifactory/maven/")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        maven {
            url = uri("file:C:/Users/KNS/StudioProjects/flow/flow/build/repo")
        }
        maven {
            url = uri("file:C:/Users/KNS/StudioProjects/FallingDiamonds/app/build/repo")
        }
        maven {
            url = uri("file:C:/Users/KNS/AndroidStudioProjects/DiamondTimer/app/build/repo")
        }
        maven {
            url = uri("file:C:/Users/KNS/AndroidStudioProjects/OwlGame/app/build/repo")
        }
        maven {
            url = uri("file:C:/Users/KNS/AndroidStudioProjects/Todos/app/build/repo")
        }
        maven {
            url = uri("https://maven.pkg.github.com/anastas-makar/DiamondApi")
            credentials {
                username = System.getenv("GITHUB_USERNAME")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
        maven {
            url = uri("https://maven.pkg.github.com/anastas-makar/AuthVk")
            credentials {
                username = System.getenv("GITHUB_USERNAME")
                password = System.getenv("GITHUB_TOKEN")
            }
        }

        maven {
            url =
                uri("https://artifactory-external.vkpartner.ru/artifactory/vkid-sdk-android/")
        }
        maven {
            url = uri("https://artifactory-external.vkpartner.ru/artifactory/maven/")
        }
        maven {
            url =
                uri("https://artifactory-external.vkpartner.ru/artifactory/vk-id-captcha/android/")
        }
    }
}

rootProject.name = "Save An Owl"
include(":app")
 