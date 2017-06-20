package com.github.arminha.jenkinstray

import groovy.transform.CompileStatic

import java.awt.Desktop
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

@CompileStatic
class Main {

  static void main(String[] args) {
    new Main().start("https://example.com/", null, null)
  }

  void start(String url, String username, String accessToken) {
    def view = new JenkinsView(url, username, accessToken)
    def tray = new Tray()
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