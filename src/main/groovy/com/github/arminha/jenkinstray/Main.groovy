package com.github.arminha.jenkinstray

import groovy.transform.CompileStatic

@CompileStatic
class Main {
  static void main(String[] args) {
    def view = new JenkinsView("test", null)
    println(view.retrieveJobs())
  }
}