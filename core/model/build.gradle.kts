plugins {
    alias(libs.plugins.moneyconvertor.jvm)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.kotlin.serialization.json)
}