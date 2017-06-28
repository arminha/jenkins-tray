package com.github.arminha.jenkinstray

import groovy.transform.CompileStatic

import java.awt.Desktop
import java.nio.file.Files
import java.nio.file.Path
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@CompileStatic
class Main {

  static void main(String[] args) {
    def main = new Main()
    def config = main.readConfigFile()
    main.start(config)
  }

  Config readConfigFile() {
    def configPath = XdgBasedir.configHome().resolve('jenkins-tray').resolve('settings.toml')
    def config = new Config()
    if (Files.exists(configPath)) {
      config.readFromFile(configPath)
    } else {
      config.jenkinsUrl = 'https://example.com/'
      createNonExistingDirs(configPath)
      config.writeToFile(configPath)
      println("Please edit config file at $configPath")
      System.exit(0)
    }
    config
  }

  private void createNonExistingDirs(Path configPath) {
    def dir = configPath.getParent()
    if (dir) {
      Files.createDirectories(dir)
    }
  }

  void start(Config config) {
    def view = new JenkinsView(config.jenkinsUrl, config.username, config.accessToken)
    def tray = new Tray(config.name ?: config.jenkinsUrl)
    tray.addMenuItem("Open Jenkins", {
      Desktop.getDesktop().browse(view.url.toURI())
    })
    tray.addMenuItem("Update", {
      update(view, tray)
    })
    tray.addMenuItem("Exit", {
      System.exit(0)
    })

    def executor = Executors.newSingleThreadScheduledExecutor()
    executor.scheduleWithFixedDelay({ update(view, tray) }, 500, 30000, TimeUnit.MILLISECONDS)

    tray.show()
  }

  void update(JenkinsView view, Tray tray) {
    def jobs = view.retrieveJobs()
    def status = view.aggregateStatus(jobs)
    tray.setStatus(status)
    println(status)
  }
}