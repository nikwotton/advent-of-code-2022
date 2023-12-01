import org.gradle.api.tasks.wrapper.Wrapper.DistributionType.ALL

plugins {
    kotlin("jvm") version "1.9.21"
    kotlin("plugin.serialization") version "1.9.21"
}

repositories {
    mavenCentral()
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
            }
        }
    }

    wrapper {
        gradleVersion = "8.5"
        distributionType = ALL
    }
}
