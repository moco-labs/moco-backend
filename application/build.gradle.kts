plugins {
    id("com.google.cloud.tools.jib") version "3.3.2"
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
    implementation(project(":adapters:web"))
    implementation(project(":adapters:persistence"))
    implementation(project(":adapters:messaging"))
    implementation(project(":adapters:ai"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("me.paulschwarz:spring-dotenv:4.0.0")

    developmentOnly("org.springframework.boot:spring-boot-devtools")
}

tasks.getByName("bootJar") {
    enabled = true
}

jib {
    from {
        image = "eclipse-temurin:21-jre-alpine"
        platforms {
            platform {
                architecture = "amd64"
                os = "linux"
            }
            platform {
                architecture = "arm64"
                os = "linux"
            }
        }
    }
    to {
        image = System.getenv("DOCKER_HUB_IMAGE")
        tags = setOf("latest", version.toString())
        auth {
            username = System.getenv("DOCKER_HUB_USERNAME")
            password = System.getenv("DOCKER_HUB_PASSWORD")
        }
    }
    container {
        mainClass = "lab.ujumeonji.moco.MocoApiApplication"
        jvmFlags = listOf(
            "-Xms1024m",
            "-Xmx1024m",
            "-XX:+UseContainerSupport",
            "-XX:MaxRAMPercentage=75.0",
            "-Djava.security.egd=file:/dev/./urandom"
        )
        ports = listOf(System.getenv("PORT") ?: "8080")
        environment = mapOf(
            "SPRING_PROFILES_ACTIVE" to System.getenv("APP_PROFILE"),
            "LANG" to "en_US.UTF-8"
        )
        creationTime.set("USE_CURRENT_TIMESTAMP")
        workingDirectory = "/app"
        user = "1000:1000"
        volumes = listOf("/app/logs")
        labels = mapOf(
            "maintainer" to "webdev0594",
            "version" to version.toString(),
            "description" to "Moco Application"
        )
    }
}
