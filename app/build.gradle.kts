plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.javademo"
    compileSdk = 36
    defaultConfig {
        applicationId = "com.example.javademo"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("com.adsurge.sdk:adn-sdk:1.9.0")
}
