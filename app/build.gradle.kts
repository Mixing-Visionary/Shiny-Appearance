plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.relay)
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "ru.visionary.mixing.shiny_appearance"
    compileSdk = 35
    buildFeatures {
        buildConfig = true
    }
    defaultConfig {
        buildConfigField(
            "String",
            "MINIO_ACCESS_KEY_ANDROID",
            "\"${project.findProperty("MINIO_ACCESS_KEY_ANDROID") ?: ""}\""
        )
        buildConfigField(
            "String",
            "MINIO_SECRET_KEY_ANDROID",
            "\"${project.findProperty("MINIO_SECRET_KEY_ANDROID") ?: ""}\""
        )
    }
    defaultConfig {
        applicationId = "ru.visionary.mixing.shiny_appearance"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "0.0.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.12"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val nav_version = "2.8.4"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    implementation("com.amazonaws:aws-android-sdk-s3:2.72.0")
    implementation("com.amazonaws:aws-android-sdk-core:2.72.0")

    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")

    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.10.0")
    implementation("com.squareup.retrofit2:converter-gson:2.10.0")

    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:$nav_version")

    implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")

    androidTestImplementation("androidx.navigation:navigation-testing:$nav_version")

    implementation("io.coil-kt.coil3:coil-compose:3.0.4")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.4")
    implementation("io.coil-kt.coil3:coil-gif:3.0.4")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}