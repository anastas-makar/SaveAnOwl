pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
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
    }
}

rootProject.name = "Save An Owl"
include(":app")
 