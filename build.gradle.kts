plugins {
    `java-library`
    `maven-publish`
    signing
    id("io.spring.dependency-management") version "1.1.7"
    id("io.spring.nullability") version "0.0.12"
    id("pl.allegro.tech.build.axion-release") version "1.21.1"
    id("com.diffplug.spotless") version "8.4.0"
}

scmVersion {
    tag {
        prefix.set("")
    }
}

group = "io.github.gadnex"
version = scmVersion.version
description = "Spring Boot library used to generate Datastar Server Sent Events and rendering the HTML using JTE"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
    withJavadocJar()
    withSourcesJar()
}

repositories {
    mavenCentral()
}

val springBootVersion by extra("4.0.5")

dependencyManagement {
    imports {
        mavenBom("org.springframework.boot:spring-boot-dependencies:$springBootVersion")
    }
}

dependencies {
    // Spring Web MVC
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")

    // JTE
    implementation("gg.jte:jte-spring-boot-starter-4:3.2.3")

    // JUnit
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    
    // Fix for the Mockito self-attachment warning
    val mockitoAgent = configurations.testRuntimeClasspath.get()
        .find { it.name.contains("mockito-core") }
    if (mockitoAgent != null) {
        jvmArgs("-javaagent:$mockitoAgent", "-Xshare:off")
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "io.github.gadnex"
            artifactId = "jte-datastar-spring-boot-starter"
            from(components["java"])
            pom {
                name.set("jte-datastar-spring-boot-starter")
                description.set("Spring Boot library used to generate Datastar Server Sent Events and rendering the HTML using JTE")
                url.set("https://github.com/Gadnex/jte-datastar-spring-boot-starter")
                inceptionYear.set("2025")
                licenses {
                    license {
                        name.set("Apache-2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("gadnex")
                        name.set("William Gadney")
                    }
                }
                scm {
                    connection.set("scm:git@github.com:Gadnex/jte-datastar-spring-boot-starter.git")
                    developerConnection.set("scm:git:ssh://github.com/Gadnex/jte-datastar-spring-boot-starter.git")
                    url.set("http://github.com/Gadnex/jte-datastar-spring-boot-starter.git")
                }
            }
        }
    }

    repositories {
        maven {
            url = uri(layout.buildDirectory.dir("maven-release"))
        }
    }
}

signing {
    sign(publishing.publications)
}

spotless {
    java {
        googleJavaFormat()
    }
}
