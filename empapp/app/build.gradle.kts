plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.empapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.empapp"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation("com.airbnb.android:lottie:6.0.0") // Use the latest version
    implementation ("com.google.android.material:material:1.9.0")
    implementation ("com.google.android.material:material:1.5.0")
    implementation (libs.core.ktx)
    implementation (libs.constraintlayout.v213)
    implementation (libs.cardview)
    implementation (libs.gridlayout)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.materialdatetimepicker)
    implementation(libs.gridlayout)
    implementation(libs.play.services.location)
    implementation(libs.play.services.maps)
    implementation(libs.core.animation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
