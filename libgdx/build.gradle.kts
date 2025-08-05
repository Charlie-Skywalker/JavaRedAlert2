group = "org.example.redalert.libgdx"
version = "unspecified"

dependencies {
    api(project(":game"))
    
    api(libs.libgdx.ashley)
    api(libs.libgdx.box2dlights)
    api(libs.libgdx.controllers.core)
    api(libs.libgdx)
    api(libs.libgdx.ai)
    api(libs.libgdx.box2d)
    api(libs.libgdx.bullet)
    api(libs.libgdx.freetype)
    
    api(libs.libgdx.vis.ui)
    api(libs.libgdx.artemis.odb)
    
    api(libs.libgdx.ktx.actors)
    api(libs.libgdx.ktx.ai)
    api(libs.libgdx.ktx.app)
    api(libs.libgdx.ktx.artemis)
    api(libs.libgdx.ktx.ashley)
    api(libs.libgdx.ktx.assets)
    api(libs.libgdx.ktx.assets.async)
    api(libs.libgdx.ktx.async)
    api(libs.libgdx.ktx.box2d)
    api(libs.libgdx.ktx.collections)
    api(libs.libgdx.ktx.freetype)
    api(libs.libgdx.ktx.freetype.async)
    api(libs.libgdx.ktx.graphics)
    api(libs.libgdx.ktx.i18n)
    api(libs.libgdx.ktx.inject)
    api(libs.libgdx.ktx.json)
    api(libs.libgdx.ktx.log)
    api(libs.libgdx.ktx.math)
    api(libs.libgdx.ktx.preferences)
    api(libs.libgdx.ktx.reflect)
    api(libs.libgdx.ktx.scene2d)
    api(libs.libgdx.ktx.style)
    api(libs.libgdx.ktx.tiled)
    api(libs.libgdx.ktx.vis)
    api(libs.libgdx.ktx.vis.style)
    
    api(libs.kotlin.stdlib)
    api(libs.kotlinx.coroutines.core)
    
    if (properties["enableGraalNative"] == "true") {
        implementation(libs.libgdx.graal.helper)
    }
}

tasks.getByName<JavaCompile>("compileJava") {
    options.encoding = "UTF-8"
}

tasks.getByName<JavaCompile>("compileTestJava") {
    options.encoding = "UTF-8"
}

// eclipse.project.name = appName + '-core'
