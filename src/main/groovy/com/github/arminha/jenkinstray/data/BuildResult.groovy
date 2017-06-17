package com.github.arminha.jenkinstray.data

import com.fasterxml.jackson.annotation.JsonProperty
import groovy.transform.CompileStatic

@CompileStatic
enum BuildResult {
  @JsonProperty("SUCCESS")
  Success,
  @JsonProperty("UNSTABLE")
  Unstable,
  @JsonProperty("FAILURE")
  Failure,
  @JsonProperty("NOT_BUILT")
  NotBuilt,
  @JsonProperty("ABORTED")
  Aborted,
}
