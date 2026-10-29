// Workaround for AGP AndroidLocationsException when both ANDROID_PREFS_ROOT and ANDROID_USER_HOME are set
try {
    val peClass = Class.forName("java.lang.ProcessEnvironment")
    val envField = peClass.getDeclaredField("theEnvironment")
    envField.isAccessible = true
    @Suppress("UNCHECKED_CAST")
    val env = envField.get(null) as MutableMap<String, String>
    env.remove("ANDROID_PREFS_ROOT")

    val cienvField = peClass.getDeclaredField("theCaseInsensitiveEnvironment")
    cienvField.isAccessible = true
    @Suppress("UNCHECKED_CAST")
    val cienv = cienvField.get(null) as MutableMap<String, String>
    cienv.remove("ANDROID_PREFS_ROOT")
} catch (t: Throwable) {
    // ignore
}

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
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "QLSV_KTHP"
include(":app")

 