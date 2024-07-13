plugins {
    alias(libs.plugins.moneyconvertor.android.library)
}

android {
    namespace = "com.mc.testing"
}

dependencies {
    api(kotlin("test"))
    api(project(":core:data"))
    api(project(":core:common"))
    api(project(":core:model"))
    implementation(libs.kotlinx.coroutines.test)
}