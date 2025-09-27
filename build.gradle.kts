plugins {
  id("java")
  id("org.jetbrains.kotlin.jvm") version "2.2.0"
  id("org.jetbrains.intellij.platform") version "2.9.0"
  id("com.ncorti.ktfmt.gradle") version "0.24.0"
}

java { toolchain { languageVersion.set(JavaLanguageVersion.of(17)) } }

repositories {
  mavenCentral()
  intellijPlatform { defaultRepositories() }
}

dependencies { intellijPlatform { intellijIdeaCommunity("2023.3.6") } }

group = "ca.aerb"

version = "0.6.0"

repositories {
  mavenCentral()
  gradlePluginPortal()
}

tasks {
  signPlugin {
    certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
    privateKey.set(System.getenv("PRIVATE_KEY"))
    password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
  }

  publishPlugin { token.set(System.getenv("PUBLISH_TOKEN")) }
}
