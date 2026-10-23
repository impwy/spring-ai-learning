plugins {
    java
    id("org.springframework.boot") version "4.1.1"
    id("io.spring.dependency-management") version "1.1.7"
    id("org.openjfx.javafxplugin") version "0.1.0"
}
val springAiVersion by extra("2.0.1")

group = "com"
version = "0.0.1-SNAPSHOT"
description = "back-ai"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

javafx {
    version = "25.0.4"
    modules("javafx.controls")
}

dependencies {
//    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springframework.ai:spring-ai-starter-model-ollama")
    implementation("org.springframework.ai:spring-ai-starter-model-openai")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    runtimeOnly("io.netty:netty-resolver-dns-native-macos::osx-aarch_64")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    testImplementation("org.springframework.boot:spring-boot-webtestclient")

    implementation(platform("org.springframework.ai:spring-ai-bom:2.0.1"))
}
dependencyManagement {
    imports {
        mavenBom("org.springframework.ai:spring-ai-bom:$springAiVersion")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

springBoot {
    mainClass = "com.backai.BackAiApplication"
}

tasks.register<JavaExec>("runChat") {
    group = "application"
    description = "JavaFX 채팅 클라이언트를 실행합니다. 서버는 bootRun으로 별도 실행합니다."
    mainClass = "com.backai.ai.fx.ChattingFX"

    val runtime = sourceSets.main.get().runtimeClasspath
    val javafxModules = runtime.filter { it.name.startsWith("javafx-") }
    classpath = runtime.minus(javafxModules)
    javaLauncher = javaToolchains.launcherFor(java.toolchain)
    jvmArgumentProviders.add(CommandLineArgumentProvider {
        listOf("--module-path", javafxModules.asPath, "--add-modules", "javafx.controls",
            "--enable-native-access=javafx.graphics")
    })
}
