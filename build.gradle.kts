plugins {
    id("java")
}

group = "io.github.irishgreencitrus"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
}

val velocityVersion = "3.2.0-SNAPSHOT"
dependencies {
    compileOnly("com.velocitypowered:velocity-api:$velocityVersion")
    annotationProcessor("com.velocitypowered:velocity-api:$velocityVersion")
}

tasks.test {
    useJUnitPlatform()
}