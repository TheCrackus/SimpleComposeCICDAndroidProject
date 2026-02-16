plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // Firebase services
    alias(libs.plugins.google.services)

    // Crashlytics
    alias(libs.plugins.crashlytics)
    id("jacoco")

    // App distributions
    alias(libs.plugins.appdistribution)
}

android {
    namespace = "com.example.simplecomposecicdandroidproject"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.simplecomposecicdandroidproject"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // Firebase BoM
    implementation(platform(libs.firebase.bom))

    // Crashlytics SDK
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)

    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

jacoco {
    toolVersion = "0.8.12"
}

tasks.withType<Test>().configureEach {
    extensions.configure(JacocoTaskExtension::class.java) {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")

    reports {
        xml.required.set(true)
        html.required.set(true)
    }

    val fileFilter = listOf(
        "**/R.class", "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "**/android/**/*.*"
    )

    val buildDirPath = layout.buildDirectory.asFile.get().path

    val debugTree = fileTree(
        "$buildDirPath/intermediates/javac/debug/classes"
    ) {
        exclude(fileFilter)
    }
    val kotlinTree = fileTree(
        "$buildDirPath/tmp/kotlin-classes/debug"
    ) {
        exclude(fileFilter)
    }

    classDirectories.setFrom(files(debugTree, kotlinTree))
    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))
    executionData.setFrom(fileTree(layout.buildDirectory.asFile.get()) {
        include(
            "jacoco/testDebugUnitTest.exec",
            "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec"
        )
    })
}

firebaseAppDistribution {
    appId = "1:251883887219:android:0cb177c78604a1525c2806"
    groups = "dev-testers"
    releaseNotes = "Dev build from GitHub Actions"
    serviceCredentialsFile = System.getenv("FIREBASE_SERVICE_ACCOUNT_FILE")
}
