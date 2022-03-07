import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.6.4"
	id("io.spring.dependency-management") version "1.0.11.RELEASE"
	kotlin("jvm") version "1.6.10"
	kotlin("plugin.spring") version "1.6.10"

	// The following is needed below for org.openapitools.generator.gradle.plugin.tasks.GenerateTask
	id("org.openapi.generator") version "5.3.0"
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
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// Add openapi
	implementation("org.springdoc:springdoc-openapi-data-rest:1.6.4")
	implementation("org.springdoc:springdoc-openapi-ui:1.6.4")
	implementation("org.springdoc:springdoc-openapi-kotlin:1.6.4")

	// Add moshi for Generating a Client From an Existing Specification.  "testImplementation"
	// is in github, "implementation" is in website
	// testImplementation("com.squareup.moshi:moshi-kotlin:1.13.0")
	// testImplementation("com.squareup.moshi:moshi-adapters:1.13.0")
	// testImplementation("com.squareup.okhttp3:okhttp:4.9.3")

	implementation("ch.qos.logback:logback-classic:1.2.3")
	implementation("ch.qos.logback:logback-core:1.2.3")
}

val oasPackage = "name.hampton.mike.thing"
val oasSpecLocation = "src/main/resources/thing-spec.yaml"
val oasGenOutputDir = project.layout.buildDirectory.dir("generated-oas")


tasks.register("generateServer", org.openapitools.generator.gradle.plugin.tasks.GenerateTask::class) {
	input = project.file(oasSpecLocation).path
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

tasks.withType<Test> {
	useJUnitPlatform()
}