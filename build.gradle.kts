// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    id("com.google.devtools.ksp") version "1.9.10-1.0.13" apply false
    id("vkid.manifest.placeholders") version "1.1.0" apply true
}

val lp = com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir)

vkidManifestPlaceholders {
    init(clientId = lp.getProperty("APP_ID"), clientSecret = lp.getProperty("CLIENT_SECRET"))
}