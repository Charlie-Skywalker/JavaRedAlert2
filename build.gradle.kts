plugins {
    alias(libs.plugins.kotlin.jvm)
}

subprojects.forEach {
    it.plugins.let { plugins ->
        for (provider in listOf(
            libs.plugins.kotlin.jvm
        )) {
            plugins.apply(provider.get().pluginId)
        }
    }
    it.kotlin {
        jvmToolchain(libs.versions.java.get().toInt())
    }
    it.dependencies {
        testImplementation(libs.kotlin.test)
    }
    it.tasks.withType<Test>().configureEach {
        useJUnitPlatform()
    }
}
