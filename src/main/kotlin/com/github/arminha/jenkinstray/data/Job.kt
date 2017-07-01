package com.github.arminha.jenkinstray.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.Optional

@JsonIgnoreProperties(ignoreUnknown = true)
class Job(val name: String, val color: Color, val lastBuild: Build?) {

    fun buildTimestamp(): Optional<Long> {
        return Optional.ofNullable(lastBuild).map({ it.timestamp })
    }
}