dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
    implementation(project(":core:application"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.data:spring-data-commons")
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    implementation("org.slf4j:slf4j-api")
    implementation("io.swagger.core.v3:swagger-annotations-jakarta:2.2.22")
    implementation("org.springframework.security:spring-security-crypto")
    implementation("io.jsonwebtoken:jjwt-api:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.5")
}
