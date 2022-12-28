import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import java.io.ByteArrayOutputStream

plugins {
    id("java-library")
    id("io.papermc.paperweight.userdev") version "1.3.11"
    id("net.minecrell.plugin-yml.bukkit") version "0.5.2"
}

group = "pl.tuso.duels"
version = "1.0"
description = "Just duels plugin!"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

repositories {
    mavenCentral()
}

dependencies {
    paperDevBundle("1.19.2-R0.1-SNAPSHOT")
}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
    }
}

bukkit {
    load = BukkitPluginDescription.PluginLoadOrder.POSTWORLD
    main = "pl.tuso.duels.Duels"
    apiVersion = "1.19"
    authors = listOf("tuso")
    version = getVersion().toString() + String.format(" (Git → %s)", getGitHash())

    commands {
        register("duels") {
            permission = "duels.command"
            aliases = listOf("duel")
        }
    }
}

fun getGitHash(): String {
    val stdout = ByteArrayOutputStream()
    exec {
        commandLine("git", "rev-parse", "--short", "HEAD")
        standardOutput = stdout
    }
    return stdout.toString().trim()
}