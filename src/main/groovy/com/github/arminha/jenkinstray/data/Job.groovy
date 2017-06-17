package com.github.arminha.jenkinstray.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import groovy.transform.CompileStatic
import groovy.transform.Immutable

@CompileStatic
@Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
class Job {
  String name
  Color color
  Build lastBuild

  Optional<Long> buildTimestamp() {
    Optional.ofNullable(lastBuild).map({ Build build -> build.timestamp })
  }
}
