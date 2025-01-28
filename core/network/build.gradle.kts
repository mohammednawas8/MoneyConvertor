import java.util.Properties

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

private val apiKey = localProperties.getProperty("apiKey")
    .takeIf { it != "null" }
    ?: File("./ci_text.txt").readText()

plugins {
    alias(libs.plugins.moneyconvertor.android.library)
    alias(libs.plugins.moneyconvertor.android.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "com.mc.network"

    defaultConfig {
        buildConfigField(type = "String", name = "apiKey", value = "\"$apiKey\"")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.retrofit.logger)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.kotlin.serialization.json)

    implementation(project(":core:model"))
}