plugins {
    kotlin("jvm") version "1.9.23"
}

group = "de.randombyte"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
}

kotlin {
    jvmToolchain(11)
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "de.randombyte.seratoriser.Seratoriser"
    }
    configurations["compileClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
}
