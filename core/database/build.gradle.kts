plugins {
    alias(libs.plugins.moneyconvertor.android.library)
    alias(libs.plugins.moneyconvertor.android.room)
    alias(libs.plugins.moneyconvertor.android.hilt)
}

android {
    namespace = "com.mc.database"
}

dependencies {
    implementation(project(":core:model"))
}