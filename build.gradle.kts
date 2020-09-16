plugins {
  groovy
  application
  kotlin("jvm") version "1.4.10"
  id("edu.sc.seis.launch4j") version "2.4.6"
}

val appMainClass = "com.github.arminha.jenkinstray.Main"
val jvmOpts = listOf("-Xms10m", "-Xmx32m", "-Xss228k", "-XX:+UseSerialGC")

application {
  mainClass.set(appMainClass)
  applicationDefaultJvmArgs = jvmOpts
}

repositories {
  mavenCentral()
}

dependencies {
  implementation("com.beust:klaxon:5.2")
  implementation("com.squareup.okhttp3:okhttp:3.14.7")
  implementation("com.moandjiezana.toml:toml4j:0.7.2")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  testImplementation("org.codehaus.groovy:groovy:2.5.10")
  testImplementation("org.spockframework:spock-core:1.3-groovy-2.5")
}

launch4j {
  mainClassName = appMainClass
  dontWrapJar = true
  // add dependencies to classpath
  copyConfigurable = sourceSets.main.get().runtimeClasspath.filter { it.name.endsWith(".jar") }
  jvmOptions = jvmOpts.toMutableSet()
}

val createExe by tasks.createExe
distributions {
  main {
    contents {
      from(createExe.outputs) {
        include("*.exe")
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
      }
    }
  }
}

tasks.named<Wrapper>("wrapper") {
  gradleVersion = "6.6.1"
}
