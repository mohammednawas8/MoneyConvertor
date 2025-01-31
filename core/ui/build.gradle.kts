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
}