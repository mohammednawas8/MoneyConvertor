plugins {
    alias(libs.plugins.moneyconvertor.android.feature)
    alias(libs.plugins.moneyconvertor.android.compose)
}

android {
    namespace = "com.mc.currencyconvertor"
}

dependencies {
    implementation(project(":core:data"))
    implementation(project(":core:model"))
}