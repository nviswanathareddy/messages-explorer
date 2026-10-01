import java.util.Properties

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.compose)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")

if (localPropertiesFile.exists()) {
  localPropertiesFile.inputStream().use {
    localProperties.load(it)
  }
}

android {
  namespace = "com.nviswanathareddy.messagesexplorer"
  compileSdk {
    version = release(37)
  }
  defaultConfig {
    applicationId = "com.nviswanathareddy.messagesexplorer"
    minSdk = 24
    targetSdk = 37
    versionCode = 1
    versionName = "1.0"
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    buildConfigField(
        "String",
        "LOGO_DEV_TOKEN",
        "\"${localProperties.getProperty("LOGO_DEV_TOKEN", "")}\"",
    )
  }
  buildTypes {
    release {
      optimization {
        enable = false
      }
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  buildToolsVersion = "36.0.0"
}

dependencies {
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.compose.foundation)
  implementation(libs.androidx.compose.foundation.layout)
  implementation(libs.androidx.compose.material3)
  implementation(libs.androidx.compose.ui)
  implementation(libs.androidx.compose.ui.graphics)
  implementation(libs.androidx.compose.ui.tooling.preview)
  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation("io.coil-kt:coil-compose:2.7.0")
  implementation("androidx.compose.material:material-icons-extended")
  implementation(libs.androidx.material3)
  implementation(libs.androidx.ui)
  testImplementation(libs.junit)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.compose.ui.test.junit4)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(libs.androidx.junit)
  debugImplementation(platform(libs.androidx.compose.bom))
  debugImplementation(libs.androidx.compose.ui.test.manifest)
  debugImplementation(libs.androidx.compose.ui.tooling)
}
