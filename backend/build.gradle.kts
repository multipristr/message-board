plugins {
    java
    id("org.springframework.boot") version "2.7.18"
    id("io.spring.dependency-management") version "1.1.6"
}

group = "org.messages"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("io.jsonwebtoken:jjwt-api:0.12.6")
    implementation("io.jsonwebtoken:jjwt-impl:0.12.6")
    implementation("io.jsonwebtoken:jjwt-jackson:0.12.6")
    implementation("org.neo4j:neo4j-jdbc-driver:3.5.2")
    implementation("com.zaxxer", "HikariCP", "4.0.3")
    implementation("org.mockito:mockito-core:4.11.0")
    implementation("org.springdoc:springdoc-openapi-ui:1.8.0")
    implementation("org.slf4j:jcl-over-slf4j:1.7.36")
    implementation("org.slf4j:log4j-over-slf4j:1.7.36")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.security:spring-security-test")
}

tasks {
    test {                                  
        useJUnitPlatform()
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}
