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
            jenkins.put("user", username)
        }
        if (accessToken != null) {
            jenkins.put("access_token", accessToken)
        }
        if (name != null) {
            jenkins.put("name", name)
        }
        val map = mapOf<String, Any?>("jenkins" to jenkins)
        Files.newOutputStream(path).use {
            TomlWriter().write(map, it)
        }
    }
}
