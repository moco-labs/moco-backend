plugins {
    kotlin("jvm") version "1.9.25"
    kotlin("plugin.spring") version "1.9.25"
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.7"
    id("com.diffplug.spotless") version "7.0.3"
}

group = "lab.ujumeonji"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

extra["jacksonVersion"] = "2.15.4"
extra["springAiVersion"] = "1.0.3"
extra["jjwtVersion"] = "0.12.5"

subprojects {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.spring")
    apply(plugin = "io.spring.dependency-management")

    repositories {
        mavenCentral()
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
        }
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }
    }

    kotlin {
        compilerOptions {
            freeCompilerArgs.addAll("-Xjsr305=strict")
        }
    }

    configurations.all {
        resolutionStrategy.cacheChangingModulesFor(0, "seconds")
        resolutionStrategy.cacheDynamicVersionsFor(0, "seconds")
    }

    dependencies {
        testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
        testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
        testImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
        implementation("org.jetbrains.kotlin:kotlin-reflect")
        implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${property("jacksonVersion")}")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

spotless {
    kotlin {
        target("**/*.kt")
        ktlint("1.0.1")
        leadingTabsToSpaces(4)
        trimTrailingWhitespace()
        endWithNewline()
    }
    kotlinGradle {
        target("*.gradle.kts")
        ktlint("1.0.1")
        leadingTabsToSpaces(4)
        trimTrailingWhitespace()
        endWithNewline()
    }
}
