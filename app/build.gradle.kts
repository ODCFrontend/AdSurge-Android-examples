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
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.adsurge.sdk:adn-sdk:1.9.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
}
