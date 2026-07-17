import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.javadoc.Javadoc
import org.gradle.api.tasks.testing.Test
import org.gradle.api.publish.PublishingExtension
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService

plugins {
  base
}

group = "ai.runapi"
version = "0.1.1"

allprojects {
  group = rootProject.group
  version = rootProject.version
}

subprojects {
  val testJavaVersion = providers.gradleProperty("testJavaVersion")

  plugins.withId("java-base") {
    val toolchains = extensions.getByType<JavaToolchainService>()

    tasks.withType<JavaCompile>().configureEach {
      options.encoding = "UTF-8"
      options.release = 8
      options.compilerArgs.add("-Xlint:-options")
    }

    tasks.withType<Test>().configureEach {
      useJUnitPlatform()
      testJavaVersion.orNull?.let { version ->
        javaLauncher = toolchains.launcherFor {
          languageVersion = JavaLanguageVersion.of(version)
        }
      }
    }

    tasks.withType<Javadoc>().configureEach {
      (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:all,-missing", "-quiet")
    }
  }

  plugins.withId("maven-publish") {
    extensions.configure<PublishingExtension>("publishing") {
      repositories {
        maven {
          name = "runapiStaging"
          val stagingDir = providers.gradleProperty("runapiMavenStagingDir")
            .orElse(layout.buildDirectory.dir("runapi-maven-staging").map { it.asFile.absolutePath })
          url = uri(stagingDir.get())
        }
      }
    }
  }
}

project(":runapi-core").version = "0.1.5"

subprojects {
  if (name != "runapi-core") {
    tasks.matching { task ->
      task.name in setOf("compileJava", "compileTestJava", "test", "javadoc", "publishToMavenLocal")
    }.configureEach {
      dependsOn(":runapi-core:publishToMavenLocal")
    }
  }
}

