import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    `java-library`
    `maven-publish`
}

group = "com.jamesgoodwin"
version = "0-1-SNAPSHOT"

repositories {
    mavenCentral()
//    maven { url = uri("https://dl.bintray.com/aballano/maven/") }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = "com.jamesgoodwin"
            artifactId = "burraco-engine"
            version = "0.1-SNAPSHOT"

            from(components["java"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/jamesgoodwin/burraco-game")
            credentials {
                username = System.getenv("GITHUB_ACTOR")
                password = System.getenv("GITHUB_TOKEN")
            }
        }
//        maven { url = uri("https://dl.bintray.com/aballano/maven/") }
    }
}

dependencies {
//    compile("mnemonik:mnemonik:2.1.0")
    testImplementation(kotlin("test-junit"))
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}
