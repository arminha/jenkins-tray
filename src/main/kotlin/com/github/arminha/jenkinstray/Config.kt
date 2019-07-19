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

import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import java.nio.file.Files
import java.nio.file.Path

class Config {
    var jenkinsUrl: String = ""
    var name: String? = null
    var username: String? = null
    var accessToken: String? = null

    fun readFromFile(path: Path) {
        val toml = Toml().read(path.toFile())
        val jenkins = toml.getTable("jenkins")
        jenkinsUrl = jenkins.getString("url")
        username = jenkins.getString("user")
        accessToken = jenkins.getString("access_token")
        name = jenkins.getString("name")
    }

    fun writeToFile(path: Path) {
        val jenkins = mutableMapOf<String, String?>("url" to jenkinsUrl)
        if (username != null) {
            jenkins["user"] = username
        }
        if (accessToken != null) {
            jenkins["access_token"] = accessToken
        }
        if (name != null) {
            jenkins["name"] = name
        }
        val map = mapOf<String, Any?>("jenkins" to jenkins)
        Files.newOutputStream(path).use {
            TomlWriter().write(map, it)
        }
    }
}
