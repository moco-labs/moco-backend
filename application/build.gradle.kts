plugins {
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
