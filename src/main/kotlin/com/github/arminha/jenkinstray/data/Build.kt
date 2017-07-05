package com.github.arminha.jenkinstray.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Build(val number: Int, val result: BuildResult?, val timestamp: Long)