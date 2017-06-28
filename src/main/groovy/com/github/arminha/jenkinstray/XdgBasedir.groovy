package com.github.arminha.jenkinstray

import groovy.transform.CompileStatic

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Implements parts of the XDG directory specification with sensible fallbacks on Windows systems.
 */
@CompileStatic
class XdgBasedir {
  static final String XDG_CONFIG_HOME = "XDG_CONFIG_HOME"

  /**
   * Returns the XDG_CONFIG_HOME directory.
   */
  static Path configHome() {
    def env = System.getenv()
    if (isWindows()) {
      return Paths.get(env['LOCALAPPDATA'])
    }
    def value = env[XDG_CONFIG_HOME]?.trim()
    if (value) {
      Paths.get(value)
    } else {
      Paths.get(env['HOME'], '.config')
    }
  }

  private static boolean isWindows() {
    System.getProperty('os.name').toLowerCase().startsWith('windows')
  }
}
