plugins {
    alias(libs.plugins.moneyconvertor.android.library)
    alias(libs.plugins.moneyconvertor.android.compose)
}

android {
    namespace = "com.mc.ui"
}

dependencies {
    implementation(project(":core:designssystem"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.compose.material.navigation)
    implementation(libs.androidx.material3)
    api(libs.androidx.compose.material2)
    api(libs.androidx.compose.foundation)
}