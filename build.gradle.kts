plugins {
	kotlin("jvm") version "1.9.24"
	kotlin("plugin.spring") version "1.9.24"
	id("org.springframework.boot") version "3.3.2"
	id("io.spring.dependency-management") version "1.1.6"
	kotlin("plugin.jpa") version "1.9.24"
	application
}

group = "backend"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

application {
	mainClass.set("backend.graabackend.GraaBackendApplicationKt")
}

tasks.jar {
	manifest {
		attributes(
			"Main-Class" to application.mainClass.get()
		)
	}
}

tasks.register("buildDocker", Exec::class) {
	dependsOn("build")
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("com.squareup.retrofit2:retrofit:2.9.0")
	implementation("com.squareup.retrofit2:converter-gson:2.9.0")
//	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
//	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:1.8.1")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.reactivestreams:reactive-streams:1.0.4")
	implementation("io.projectreactor:reactor-core:3.5.2") // или последнюю версию
	implementation("org.reactivestreams:reactive-streams:1.0.4")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("io.github.cdimascio:dotenv-kotlin:6.4.0") // для доступа к .env
	implementation("io.springfox:springfox-boot-starter:3.0.0")// swagger
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	//Логирование
	implementation("org.slf4j:slf4j-api:2.0.7")  // SLF4J API
	implementation("ch.qos.logback:logback-classic:1.4.12")  // Logback
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
