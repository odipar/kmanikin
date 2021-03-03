import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.30"
}

group = "org.jmanikin"
version = "0.2-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.jmanikin:jmanikin-core:0.2.1")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "1.8"
}