import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	// JPA
	id( "org.jetbrains.kotlin.plugin.jpa") version "1.6.20-RC2"
	// This makes it unnecessary to declare all jpa classes as open
	id("org.jetbrains.kotlin.plugin.allopen") version "1.6.20-RC2"

	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"

	// The following is needed below for org.openapitools.generator.gradle.plugin.tasks.GenerateTask
	id("org.openapi.generator") version "5.3.0"
	// I want to be able to get the openapi file from another repo (SSOT)
	// used in downloadOpenapi below
	id("de.undercouch.download") version "5.0.2"
}

allOpen {
	// This makes it unnecessary to declare all jpa classes as open
	annotations("javax.persistence.Entity", "javax.persistence.MappedSuperclass", "javax.persistence.Embedabble")
}

group = "name.hampton.mike"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	// Should the following be implementation("org.jetbrains.kotlin:kotlin-stdlib") ?
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	// Add openapi
	implementation("org.springdoc:springdoc-openapi-data-rest:1.6.6")
	implementation("org.springdoc:springdoc-openapi-ui:1.6.6")
	implementation("org.springdoc:springdoc-openapi-kotlin:1.6.6")

	// JPA
	implementation("org.hibernate:hibernate-core:5.6.7.Final")
	testImplementation("org.hibernate:hibernate-testing:5.6.7.Final")
	testImplementation("com.h2database:h2:2.1.210")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	// H2 database
	runtimeOnly("com.h2database:h2")

	// Add moshi for Generating a Client From an Existing Specification.  "testImplementation"
	// is in github, "implementation" is in website
	// testImplementation("com.squareup.moshi:moshi-kotlin:1.13.0")
	// testImplementation("com.squareup.moshi:moshi-adapters:1.13.0")
	// testImplementation("com.squareup.okhttp3:okhttp:4.9.3")

	implementation("ch.qos.logback:logback-classic:1.2.11")
	implementation("ch.qos.logback:logback-core:1.2.11")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.mockk:mockk:1.12.3")
}

val oasPackage = "name.hampton.mike.thing"
val oasName = "example_openapi.yaml"
val oasTagOrBranch = "main"

val oasUrl = "https://raw.githubusercontent.com/michael-reed-hampton/apiFirstAPI/$oasTagOrBranch/$oasName"
val oasSpecLocation = project.layout.buildDirectory.file(oasName)
val oasGenOutputDir = project.layout.buildDirectory.dir("generated-oas")

tasks.register("downloadOpenapi", de.undercouch.gradle.tasks.download.Download::class) {
	src(oasUrl)
	dest(oasSpecLocation.get().toString())
}

tasks.register("generateServer", org.openapitools.generator.gradle.plugin.tasks.GenerateTask::class) {
	input = oasSpecLocation.get().toString()
	outputDir.set(oasGenOutputDir.get().toString())
	modelPackage.set("$oasPackage.model")
	apiPackage.set("$oasPackage.api")
	packageName.set(oasPackage)
	generatorName.set("kotlin-spring")
	configOptions.set(
		mapOf(
			"dateLibrary" to "java8",
			"interfaceOnly" to "true",
			"useTags" to "true",
			"delegatePattern" to "true"
		)
	)
}


sourceSets {
	val main by getting
	main.java.srcDir("${oasGenOutputDir.get()}/src/main/kotlin")
	// val test by getting
	// test.java.srcDir("${clientOutput.get()}/src/main/kotlin")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
	// We want to re-generate the server
	dependsOn("generateServer")
}

tasks.named("generateServer") {
	dependsOn(":downloadOpenapi")
}

tasks.withType<Test> {
	useJUnitPlatform()
}