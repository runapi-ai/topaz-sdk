plugins {
  `java-library`
  `maven-publish`
}

description = "Shared authentication, HTTP, retry, polling, file, account, and error primitives for RunAPI Java SDKs."

java {
  withSourcesJar()
  withJavadocJar()
}

dependencies {
  api("org.jspecify:jspecify:1.0.0")

  api("com.fasterxml.jackson.core:jackson-databind:2.17.2")
  api("com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.17.2")
  implementation("org.apache.httpcomponents.client5:httpclient5:5.3.1")

  testImplementation(platform("org.junit:junit-bom:5.10.3"))
  testImplementation("org.junit.jupiter:junit-jupiter")
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
      artifactId = "runapi-core"
      pom {
        name = "RunAPI Core Java SDK"
        description = "Shared authentication, HTTP, retry, polling, error, and resource primitives for RunAPI Java SDKs."
        url = "https://runapi.ai/docs#runapi-sdks"
        licenses {
          license {
            name = "Apache License, Version 2.0"
            url = "https://www.apache.org/licenses/LICENSE-2.0"
          }
        }
        developers {
          developer {
            id = "runapi"
            name = "RunAPI"
            email = "contact@runapi.ai"
          }
        }
        scm {
          url = "https://github.com/runapi-ai/core-sdk"
          connection = "scm:git:https://github.com/runapi-ai/core-sdk.git"
          developerConnection = "scm:git:ssh://git@github.com/runapi-ai/core-sdk.git"
        }
      }
    }
  }
}
