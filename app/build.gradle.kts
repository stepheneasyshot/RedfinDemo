import groovy.namespace.QName
import groovy.util.Node
import groovy.xml.XmlParser
import groovy.xml.XmlUtil
import java.io.FileOutputStream

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
}

android {
    namespace = "com.stephen.redfindemo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.stephen.redfindemo"
        minSdk = 30
        targetSdk = 34
        versionCode = 32
        versionName = "2.1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
        }
    }

    signingConfigs {
        val aaosSign = "stephen"
        register("aaos") {
            keyAlias = aaosSign
            keyPassword = aaosSign
            storeFile = file("./keystores/platform.jks")
            storePassword = aaosSign
        }
    }

    buildFeatures {
        viewBinding = true
        aidl = true
        buildConfig = true
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("aaos")
            isMinifyEnabled = false
            isDebuggable = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            signingConfig = signingConfigs.getByName("aaos")
            isMinifyEnabled = false
            isDebuggable = false
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
    val appName = "RedfinDemo"
    android.applicationVariants.configureEach {
        val buildType = this.buildType.name
        outputs.all {
            if (this is com.android.build.gradle.internal.api.ApkVariantOutputImpl) {
                this.outputFileName = "${appName}_${defaultConfig.versionName}_${buildType}.apk"
            }
        }
    }
}

dependencies {
    implementation(fileTree("libs").include( "*.aar"))
    implementation(libs.jcip.annotations)
    compileOnly(files("libs/framework.jar"))
    implementation(files("libs/android.car.jar"))
    implementation(libs.redfinCommonHelper)
    implementation(libs.cymchad.baseRecyclerViewAdapterHelper)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    debugImplementation(libs.leakcanary)
}

project.tasks.preBuild.get().doLast {
    // 在 preBuild 任务执行完之后处理
    // 定义修改 .iml 文件中 Android SDK 优先级方法
    fun changeSdkOrder(path: String) {
        runCatching {
            val imlFile = File(path)
            with(XmlParser().parse(imlFile)) {
                // 从 .iml 文件中读取 NewModuleRootManager 节点
                val rootManagerComponent = getAt(QName.valueOf("component"))
                    .map { it as Node }
                    .first { it.attribute("name") == "NewModuleRootManager" }
                // 从 NewModuleRootManager 节点中获取 Android SDK 配置节点
                val jdkEntry = rootManagerComponent.getAt(QName.valueOf("orderEntry"))
                    .map { it as Node }
                    .first { it.attribute("type") == "jdk" }
                // 保存节点参数
                val jdkName = jdkEntry.attribute("jdkName")
                val jdkType = jdkEntry.attribute("jdkType")
                println("> Task :${project.name}:preBuild:doLast:changedSdkOrder jdkEntry = $jdkEntry")
                // 从 NewModuleRootManager 节点中移除 Android SDK 配置节点
                rootManagerComponent.remove(jdkEntry)
                // 重新将 Android SDK 配置节点添加到 NewModuleRootManager 的最后
                rootManagerComponent.appendNode(
                    "orderEntry", mapOf(
                        "type" to "jdk",
                        "jdkName" to jdkName,
                        "jdkType" to jdkType
                    )
                )
                // 将新生成的 .iml 写入文件
                XmlUtil.serialize(this, FileOutputStream(imlFile))
            }
        }
    }

    // 修改 .iml 文件
    println("> Task :${project.name}:preBuild:doLast:changedSdkOrder")
    changeSdkOrder(rootDir.absolutePath + "/.idea/modules/app/RedfinDemo.app.main.iml")
}