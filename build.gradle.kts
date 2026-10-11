plugins {
    java
    alias(libs.plugins.lombok)
    id("org.springframework.boot") version "4.1.0"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.sonarqube") version "7.5.0.8588"
    jacoco
}

group = "io.hexlet"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("net.datafaker:datafaker:2.7.0")

    testImplementation("org.instancio:instancio-junit:5.6.0")
    testImplementation("com.h2database:h2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation("net.javacrumbs.json-unit:json-unit-assertj:6.2.0")

    runtimeOnly("com.h2database:h2")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    useJUnitPlatform()
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) { exclude("build/generated/**") }
        })
    )
    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestReport)
}

sonar {
    properties {
        property("sonar.projectKey", "artmper_hexlet-spring-blog")
        property("sonar.organization", "artmper")
        property("sonar.host.url", "https://sonarcloud.io")
        property("sonar.java.coveragePlugin", "jacoco")
        property("sonar.junit.reportPaths", "build/test-results/test")
        property("sonar.jacoco.reportPaths", "build/jacoco/test.exec")
        property("sonar.coverage.jacoco.xmlReportPaths", "build/reports/jacoco/test/jacocoTestReport.xml")
        property("sonar.sources", "src/main/java,build/generated/sources")
    }
}

tasks.named("sonar") {
    dependsOn(tasks.jacocoTestReport)
}