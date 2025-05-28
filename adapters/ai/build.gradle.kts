dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        mavenBom("org.springframework.ai:spring-ai-bom:${property("springAiVersion")}")
    }
}

dependencies {
    implementation("org.springframework.ai:spring-ai-core:1.0.0-M6")
    implementation("org.springframework.ai:spring-ai-vertex-ai-gemini-spring-boot-starter:1.0.0-M6")
}
