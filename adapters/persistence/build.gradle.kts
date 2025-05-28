dependencyManagement {
    imports {
        mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
    }
}

dependencies {
    implementation(project(":core:domain"))
    api("org.springframework.boot:spring-boot-starter-data-mongodb")
}
