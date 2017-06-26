package com.github.arminha.jenkinstray

import com.moandjiezana.toml.Toml
import com.moandjiezana.toml.TomlWriter
import groovy.transform.Canonical
import groovy.transform.CompileStatic

import java.nio.file.Files
import java.nio.file.Path

@CompileStatic
@Canonical
class Config {
  String jenkinsUrl
  String username
  String accessToken

  void readFromFile(Path path) {
    def toml = new Toml().read(path.toFile())
    def jenkins = toml.getTable('jenkins')
    jenkinsUrl = jenkins.getString('url')
    username = jenkins.getString('user')
    accessToken = jenkins.getString('access_token')
  }

  void writeToFile(Path path) {
    def jenkins = [url:jenkinsUrl]
    if (username) {
      jenkins['user'] = username
    }
    if (accessToken) {
      jenkins['access_token'] = accessToken
    }
    def map = [jenkins:jenkins]
    Files.newOutputStream(path).withCloseable {
      new TomlWriter().write(map, it)
    }
  }
}
