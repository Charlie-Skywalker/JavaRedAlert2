group = "org.example.redalert.lwjgl3"
version = "unspecified"

val lwjglNatives = Pair(
    System.getProperty("os.name")!!,
    System.getProperty("os.arch")!!
).let { (name, arch) ->
    when {
        "FreeBSD".equals(name) ->
            "natives-freebsd"
        arrayOf("Linux", "SunOS", "Unit").any { name.startsWith(it) } ->
            if (arrayOf("arm", "aarch64").any { arch.startsWith(it) })
                "natives-linux${if (arch.contains("64") || arch.startsWith("armv8")) "-arm64" else "-arm32"}"
            else if (arch.startsWith("ppc"))
                "natives-linux-ppc64le"
            else if (arch.startsWith("riscv"))
                "natives-linux-riscv64"
            else
                "natives-linux"
        arrayOf("Mac OS X", "Darwin").any { name.startsWith(it) } ->
            "natives-macos${if (arch.startsWith("aarch64")) "-arm64" else ""}"
        arrayOf("Windows").any { name.startsWith(it) } ->
            if (arch.contains("64"))
                "natives-windows${if (arch.startsWith("aarch64")) "-arm64" else ""}"
            else
                "natives-windows-x86"
        else ->
            throw Error("Unrecognized or unsupported platform. Please set \"lwjglNatives\" manually")
    }
}

dependencies {
    implementation(platform(libs.lwjgl.bom))
    
    val action:Action<ExternalModuleDependency> = Action<ExternalModuleDependency> {
        this.artifacts.forEach { artifact ->
            artifact.classifier = lwjglNatives
        }
    }
    
    implementation(libs.lwjgl, action)
    implementation(libs.lwjgl.assimp, action)
    implementation(libs.lwjgl.bgfx, action)
    implementation(libs.lwjgl.cuda, action)
    implementation(libs.lwjgl.egl, action)
    implementation(libs.lwjgl.fmod, action)
    implementation(libs.lwjgl.freetype, action)
    implementation(libs.lwjgl.glfw, action)
    implementation(libs.lwjgl.harfbuzz, action)
    implementation(libs.lwjgl.hwloc, action)
    implementation(libs.lwjgl.jawt, action)
    implementation(libs.lwjgl.jemalloc, action)
    implementation(libs.lwjgl.ktx, action)
    implementation(libs.lwjgl.libdivide, action)
    implementation(libs.lwjgl.llvm, action)
    implementation(libs.lwjgl.lmdb, action)
    implementation(libs.lwjgl.lz4, action)
    implementation(libs.lwjgl.meow, action)
    implementation(libs.lwjgl.meshoptimizer, action)
    implementation(libs.lwjgl.msdfgen, action)
    implementation(libs.lwjgl.nanovg, action)
    implementation(libs.lwjgl.nfd, action)
    implementation(libs.lwjgl.nuklear, action)
    implementation(libs.lwjgl.odbc, action)
    implementation(libs.lwjgl.openal, action)
    implementation(libs.lwjgl.opencl, action)
    implementation(libs.lwjgl.opengl, action)
    implementation(libs.lwjgl.opengles, action)
    implementation(libs.lwjgl.openvr, action)
    implementation(libs.lwjgl.openxr, action)
    implementation(libs.lwjgl.opus, action)
    implementation(libs.lwjgl.ovr, action)
    implementation(libs.lwjgl.par, action)
    implementation(libs.lwjgl.remotery, action)
    implementation(libs.lwjgl.rpmalloc, action)
    implementation(libs.lwjgl.shaderc, action)
    implementation(libs.lwjgl.spvc, action)
    implementation(libs.lwjgl.sse, action)
    implementation(libs.lwjgl.stb, action)
    implementation(libs.lwjgl.tinyexr, action)
    implementation(libs.lwjgl.tinyfd, action)
    implementation(libs.lwjgl.tootle, action)
    implementation(libs.lwjgl.vma, action)
    if (lwjglNatives == "natives-macos" || lwjglNatives == "natives-macos-arm64") {
        implementation(libs.lwjgl.vulkan, action)
    } else {
        implementation(libs.lwjgl.vulkan)
    }
    implementation(libs.lwjgl.xxhash, action)
    implementation(libs.lwjgl.yoga, action)
    implementation(libs.lwjgl.zstd, action)
    
    implementation(libs.lwjglx.awt)
    implementation(libs.joml)
    implementation(libs.joml.primitives)
    implementation(libs.steamworks4j)
    implementation(libs.steamworks4j.server)
}
