package com.github.arminha.jenkinstray.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import groovy.transform.CompileStatic
import groovy.transform.Immutable

@CompileStatic
@Immutable
@JsonIgnoreProperties(ignoreUnknown = true)
class Build {
  int number
  BuildResult result
  long timestamp
}
