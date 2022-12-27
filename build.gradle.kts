import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

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

    commands {
        register("duels") {
            permission = "duels.command"
            aliases = listOf("duel")
        }
    }
}