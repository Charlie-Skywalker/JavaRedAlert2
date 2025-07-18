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
    implementation(platform("org.lwjgl:lwjgl-bom:3.3.6"))
    
    implementation("org.lwjgl", "lwjgl")
    implementation("org.lwjgl", "lwjgl-assimp")
    implementation("org.lwjgl", "lwjgl-bgfx")
    implementation("org.lwjgl", "lwjgl-cuda")
    implementation("org.lwjgl", "lwjgl-egl")
    implementation("org.lwjgl", "lwjgl-fmod")
    implementation("org.lwjgl", "lwjgl-freetype")
    implementation("org.lwjgl", "lwjgl-glfw")
    implementation("org.lwjgl", "lwjgl-harfbuzz")
    implementation("org.lwjgl", "lwjgl-hwloc")
    implementation("org.lwjgl", "lwjgl-jawt")
    implementation("org.lwjgl", "lwjgl-jemalloc")
    implementation("org.lwjgl", "lwjgl-ktx")
    implementation("org.lwjgl", "lwjgl-libdivide")
    implementation("org.lwjgl", "lwjgl-llvm")
    implementation("org.lwjgl", "lwjgl-lmdb")
    implementation("org.lwjgl", "lwjgl-lz4")
    implementation("org.lwjgl", "lwjgl-meow")
    implementation("org.lwjgl", "lwjgl-meshoptimizer")
    implementation("org.lwjgl", "lwjgl-msdfgen")
    implementation("org.lwjgl", "lwjgl-nanovg")
    implementation("org.lwjgl", "lwjgl-nfd")
    implementation("org.lwjgl", "lwjgl-nuklear")
    implementation("org.lwjgl", "lwjgl-odbc")
    implementation("org.lwjgl", "lwjgl-openal")
    implementation("org.lwjgl", "lwjgl-opencl")
    implementation("org.lwjgl", "lwjgl-opengl")
    implementation("org.lwjgl", "lwjgl-opengles")
    implementation("org.lwjgl", "lwjgl-openvr")
    implementation("org.lwjgl", "lwjgl-openxr")
    implementation("org.lwjgl", "lwjgl-opus")
    implementation("org.lwjgl", "lwjgl-ovr")
    implementation("org.lwjgl", "lwjgl-par")
    implementation("org.lwjgl", "lwjgl-remotery")
    implementation("org.lwjgl", "lwjgl-rpmalloc")
    implementation("org.lwjgl", "lwjgl-shaderc")
    implementation("org.lwjgl", "lwjgl-spvc")
    implementation("org.lwjgl", "lwjgl-sse")
    implementation("org.lwjgl", "lwjgl-stb")
    implementation("org.lwjgl", "lwjgl-tinyexr")
    implementation("org.lwjgl", "lwjgl-tinyfd")
    implementation("org.lwjgl", "lwjgl-tootle")
    implementation("org.lwjgl", "lwjgl-vma")
    implementation("org.lwjgl", "lwjgl-vulkan")
    implementation("org.lwjgl", "lwjgl-xxhash")
    implementation("org.lwjgl", "lwjgl-yoga")
    implementation("org.lwjgl", "lwjgl-zstd")
    implementation ("org.lwjgl", "lwjgl", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-assimp", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-bgfx", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-freetype", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-glfw", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-harfbuzz", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-hwloc", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-jemalloc", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-ktx", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-libdivide", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-llvm", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-lmdb", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-lz4", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-meow", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-meshoptimizer", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-msdfgen", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-nanovg", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-nfd", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-nuklear", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-openal", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-opengl", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-opengles", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-openvr", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-openxr", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-opus", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-ovr", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-par", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-remotery", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-rpmalloc", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-shaderc", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-spvc", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-sse", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-stb", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-tinyexr", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-tinyfd", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-tootle", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-vma", classifier = lwjglNatives)
    if (lwjglNatives == "natives-macos" || lwjglNatives == "natives-macos-arm64") implementation ("org.lwjgl", "lwjgl-vulkan", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-xxhash", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-yoga", classifier = lwjglNatives)
    implementation ("org.lwjgl", "lwjgl-zstd", classifier = lwjglNatives)
    implementation("org.lwjglx", "lwjgl3-awt", "0.1.8")
    implementation("org.joml", "joml", "1.10.8")
    implementation("org.joml", "joml-primitives", "1.10.0")
    implementation("com.code-disaster.steamworks4j", "steamworks4j", "1.9.0")
    implementation("com.code-disaster.steamworks4j", "steamworks4j-server", "1.9.0")
}
