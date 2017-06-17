package com.github.arminha.jenkinstray

import com.github.arminha.jenkinstray.data.JenkinsStatus
import groovy.transform.CompileStatic

@CompileStatic
class Main {
  static void main(String[] args) {
    def view = new JenkinsView("test", null)
    def jobs = view.retrieveJobs()
    def status = view.aggregateStatus(jobs)
    println(status)
  }
}