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
package com.github.arminha.jenkinstray.data

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import java.util.Optional

@JsonIgnoreProperties(ignoreUnknown = true)
data class Job(val name: String, val color: Color, val lastBuild: Build?) {

    fun buildTimestamp(): Optional<Long> {
        return Optional.ofNullable(lastBuild).map { it.timestamp }
    }
}