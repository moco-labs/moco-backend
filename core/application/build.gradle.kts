plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.spring")
    id("io.spring.dependency-management")
}

dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
    api(project(":core:domain"))
    implementation(project(":adapters:persistence"))

    implementation("org.springframework:spring-context")
    implementation("org.springframework:spring-tx")

    implementation("jakarta.validation:jakarta.validation-api")
    implementation("org.slf4j:slf4j-api")
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework.ai:spring-ai-core:1.0.0-M6")
    implementation("org.springframework.security:spring-security-crypto")
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")
    implementation("io.swagger.core.v3:swagger-annotations-jakarta:2.2.22")

    testImplementation("org.mockito:mockito-core:5.10.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation("io.mockk:mockk:1.13.9")
}
