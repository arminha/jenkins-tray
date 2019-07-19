/*
 * Copyright (C) 2017  Armin Häberling
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.github.arminha.jenkinstray

import java.nio.file.Path
import java.nio.file.Paths

/**
 * Implements parts of the XDG directory specification with sensible fallbacks on Windows systems.
 */
object XdgBasedir {
    private const val XDG_CONFIG_HOME: String = "XDG_CONFIG_HOME"

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