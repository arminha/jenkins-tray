package com.github.arminha.jenkinstray

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Implements parts of the XDG directory specification with sensible fallbacks on Windows systems.
 */
object XdgBasedir {
    private val XDG_CONFIG_HOME: String = "XDG_CONFIG_HOME"

    /**
     * Returns the XDG_CONFIG_HOME directory.
     */
    fun configHome(): Path {
        val env = System.getenv()
        if (isWindows()) {
            return Paths.get(env["LOCALAPPDATA"])
        }
        val value = env[XDG_CONFIG_HOME]?.trim()
        return if (value != null) {
            Paths.get(value)
        } else {
            Paths.get(env["HOME"], ".config")
        }
    }

    private fun isWindows(): Boolean {
        return System.getProperty("os.name").toLowerCase().startsWith("windows")
    }
}