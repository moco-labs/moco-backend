rootProject.name = "moco-api"

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

include(
    ":core:domain",
    ":core:application",
    ":adapters:web",
    ":adapters:persistence",
    ":adapters:messaging",
    ":adapters:ai",
    ":application",
)
