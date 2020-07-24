plugins {
    maven
    java
    kotlin("jvm") version "1.3.72"
}

group = "xyz.reitmaier"
version = "0.1"

repositories {
    mavenCentral()
    // maven(url = "https://oss.sonatype.org/content/repositories/snapshots") // pi4j-v2 snapshots
}

dependencies {
    // Currently using armv6l pi4j-v2 library to address issues:
    // https://github.com/Pi4J/pi4j-v2/issues/26
    // https://github.com/Pi4J/pi4j-v2/issues/15
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk8"))



    implementation("org.slf4j:slf4j-api:2.0.0-alpha0")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}
