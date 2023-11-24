plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("kotlin-kapt")
}

android {
    namespace = "fr.dev.majdi.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    hilt {
        enableAggregatingTask = true
    }
}

dependencies {
    implementation (project(":domain"))

    //Hilt Library
    implementation ("com.google.dagger:hilt-android:2.44")
    kapt ("com.google.dagger:hilt-android-compiler:2.44")
    //Retrofit Library
    api ("com.squareup.retrofit2:retrofit:2.9.0")
    api ("com.squareup.retrofit2:converter-gson:2.9.0")
    api ("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    api ("com.squareup.okhttp3:logging-interceptor:4.11.0")
    api ("com.squareup.okhttp3:okhttp:4.11.0")
    api ("com.google.code.gson:gson:2.9.1")

    //Room Library
    implementation ("androidx.room:room-runtime:2.6.0")
    implementation ("androidx.room:room-rxjava2:2.6.0")
    kapt ("androidx.room:room-compiler:2.6.0")

}