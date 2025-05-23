plugins {
	java
	id("org.springframework.boot") version "3.4.3"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "mobile"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.springframework.boot:spring-boot-starter-jdbc:3.4.3")
	testImplementation("org.springframework.boot:spring-boot-starter-jdbc")

	// 기존 의존성들 -- 회원가입과 관련되어 의존성 추가
	implementation("org.springframework.boot:spring-boot-starter-security")

	implementation("org.springframework.security:spring-security-crypto")
	// Lombok 추가
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// JPA 의존성 추가
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")

	// 롬복
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")


	runtimeOnly("org.postgresql:postgresql")

	//jwt
	implementation("io.jsonwebtoken:jjwt-api:0.11.5")
	implementation("io.jsonwebtoken:jjwt-impl:0.11.5")
	implementation("io.jsonwebtoken:jjwt-jackson:0.11.5") // JSON 처리를 위해 Jackson 사용

}

tasks.withType<Test> {
	useJUnitPlatform()
}


