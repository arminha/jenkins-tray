package com.github.arminha.jenkinstray

import groovy.transform.CompileStatic

import java.nio.file.Path
import java.nio.file.Paths

@CompileStatic
class XdgBasedir {
  static final String XDG_CONFIG_HOME = "XDG_CONFIG_HOME"

  static Path configHome() {
    def env = System.getenv()
    def value = env[XDG_CONFIG_HOME]?.trim()
    if (value) {
      Paths.get(value)
    } else {
      Paths.get(env['HOME'], '.config')
    }
  }
}
