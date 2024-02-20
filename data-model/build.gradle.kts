plugins {
  `java-library`
  jacoco
  id("org.hypertrace.avro-plugin") version "0.3.1"
  id("org.hypertrace.publish-plugin")
  id("org.hypertrace.jacoco-report-plugin")
  id("org.owasp.dependencycheck") version "8.2.1"
}

tasks.test {
  useJUnitPlatform()
}

dependencies {
  api("org.apache.avro:avro:1.11.3")
  constraints {
    api("org.apache.commons:commons-compress:1.26.0") {
      because("Multiple vulnerabilities in avro-declared version")
      because("https://nvd.nist.gov/vuln/detail/CVE-2023-42503")
      because("https://nvd.nist.gov/vuln/detail/CVE-2024-25710")
    }
    api("com.fasterxml.jackson.core:jackson-databind:2.14.2") {
      because("version 2.12.7.1 has a vulnerability https://snyk.io/vuln/SNYK-JAVA-COMFASTERXMLJACKSONCORE-3038424")
    }
  }
  api("commons-codec:commons-codec:1.14")
  api("io.micrometer:micrometer-core:1.5.3")

  implementation("com.google.guava:guava:30.0-jre")
  implementation("org.apache.commons:commons-lang3:3.10")

  testImplementation("org.junit.jupiter:junit-jupiter:5.6.2")
  testImplementation("org.mockito:mockito-core:3.3.3")
}
