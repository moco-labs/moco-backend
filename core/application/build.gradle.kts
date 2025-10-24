dependencies {
    api(project(":core:domain"))
    implementation(project(":adapters:persistence"))

    implementation("org.springframework:spring-context")

    implementation("jakarta.validation:jakarta.validation-api")
    implementation("org.slf4j:slf4j-api")
    implementation("org.springframework.data:spring-data-commons")
    implementation("org.springframework.ai:spring-ai-starter-model-openai")
    implementation("org.springframework.retry:spring-retry")
    implementation("org.springframework.security:spring-security-crypto")
    implementation("io.jsonwebtoken:jjwt-api:${property("jjwtVersion")}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${property("jjwtVersion")}")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:${property("jjwtVersion")}")
}
