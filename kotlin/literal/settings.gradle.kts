rootProject.name = "grpc-kotlin-collatz"

// when running the assemble task, ignore the android & graalvm related subprojects
if (startParameter.taskRequests.find { it.args.contains("assemble") } == null) {
    include("protos", "stub", "stub-lite", "client", "server")
} else {
    include("protos", "stub", "server")
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
    }
}
