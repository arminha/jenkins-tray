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

import com.fasterxml.jackson.annotation.JsonProperty

enum class Color {
    @JsonProperty("red")
    Red,
    @JsonProperty("red_anime")
    RedAnime,
    @JsonProperty("yellow")
    Yellow,
    @JsonProperty("yellow_anime")
    YellowAnime,
    @JsonProperty("blue")
    Blue,
    @JsonProperty("blue_anime")
    BlueAnime,
    @JsonProperty("grey")
    Grey,
    @JsonProperty("grey_anime")
    GreyAnime,
    @JsonProperty("disabled")
    Disabled,
    @JsonProperty("disabled_anime")
    DisabledAnime,
    @JsonProperty("aborted")
    Aborted,
    @JsonProperty("aborted_anime")
    AbortedAnime,
    @JsonProperty("notbuilt")
    NotBuilt,
    @JsonProperty("notbuilt_anime")
    NotBuiltAnime,
}