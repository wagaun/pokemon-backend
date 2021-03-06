import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	war
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"
	jacoco
}

group = "com.truelayer"
version = "1.0.0-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
	testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.hamcrest:java-hamcrest:2.0.0.0")
	testImplementation("io.mockk:mockk:1.12.2")

}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

// This was done in order to generate a single war file, this can be risky as I'm not sure exactly why it's needed.
tasks.getByName<War>("war") {
	enabled = false
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
tasks.jacocoTestReport {
	dependsOn(tasks.test) // tests are required to run before generating the report
	reports {
		xml.required.set(false)
		csv.required.set(false)
		html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
	}
}

jacoco {
	toolVersion = "0.8.7"
	reportsDirectory.set(layout.buildDirectory.dir("customJacocoReportDir"))
}

