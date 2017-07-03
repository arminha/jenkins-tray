package com.github.arminha.jenkinstray

import java.awt.Desktop
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object Main {
    @JvmStatic
    fun main(args: Array<String>) {
        val config = readConfigFile()
        start(config)
    }

    fun readConfigFile(): Config {
        val configPath = XdgBasedir.configHome().resolve("jenkins-tray").resolve("settings.toml")
        val config = Config()
        if (Files.exists(configPath)) {
            config.readFromFile(configPath)
        } else {
            config.jenkinsUrl = "https://example.com/"
            createNonExistingDirs(configPath)
            config.writeToFile(configPath)
            println("Please edit config file at $configPath")
            System.exit(0)
        }
        return config
    }

    private fun createNonExistingDirs(configPath: Path) {
        val dir = configPath.parent
        if (dir != null) {
            Files.createDirectories(dir)
        }
    }

    fun start(config: Config) {
        val view = JenkinsView(config.jenkinsUrl, config.username, config.accessToken)
        val tray = Tray(config.name ?: config.jenkinsUrl)
        tray.addMenuItem("Open Jenkins", {
            Desktop.getDesktop().browse(URI.create(view.url))
        })
        tray.addMenuItem("Update", {
            update(view, tray)
        })
        tray.addMenuItem("Exit", {
            System.exit(0)
        })

        val executor = Executors.newSingleThreadScheduledExecutor()
        executor.scheduleWithFixedDelay({ update(view, tray) }, 500, 30000, TimeUnit.MILLISECONDS)

        tray.show()
    }

    fun update(view: JenkinsView, tray: Tray) {
        val jobs = view.retrieveJobs()
        val status = view.aggregateStatus(jobs)
        tray.setStatus(status)
        println(status)
    }
}
