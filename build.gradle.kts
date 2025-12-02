import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.13.1" apply false
    id("org.jetbrains.kotlin.android") version "2.2.21" apply false
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.21" apply false
    id("com.google.devtools.ksp") version "2.2.21-2.0.4" apply false
    id("vkid.manifest.placeholders") version "1.1.0" apply true
}

val lp = gradleLocalProperties(rootDir, providers)

vkidManifestPlaceholders {
    init(clientId = lp.getProperty("APP_ID"), clientSecret = lp.getProperty("CLIENT_SECRET"))
}