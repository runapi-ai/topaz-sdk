plugins {
  `java-library`
  `maven-publish`
}

description = "RunAPI Topaz Java SDK for Topaz workflows."

java {
  withSourcesJar()
  withJavadocJar()
}

dependencies {
  api("ai.runapi:runapi-core:0.1.1")

  testImplementation(platform("org.junit:junit-bom:5.10.3"))
  testImplementation("org.junit.jupiter:junit-jupiter")
}

publishing {
  publications {
    create<MavenPublication>("mavenJava") {
      from(components["java"])
      artifactId = "runapi-topaz"
      pom {
        name = "RunAPI Topaz Java SDK"
        description = "RunAPI Topaz Java SDK for Topaz workflows."
        url = "https://runapi.ai/models/topaz"
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
          url = "https://github.com/runapi-ai/topaz-sdk"
          connection = "scm:git:https://github.com/runapi-ai/topaz-sdk.git"
          developerConnection = "scm:git:ssh://git@github.com/runapi-ai/topaz-sdk.git"
        }
      }
    }
  }
}
