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

import com.beust.klaxon.Json

enum class Color {
    @Json("red")
    Red,
    @Json("red_anime")
    RedAnime,
    @Json("yellow")
    Yellow,
    @Json("yellow_anime")
    YellowAnime,
    @Json("blue")
    Blue,
    @Json("blue_anime")
    BlueAnime,
    @Json("grey")
    Grey,
    @Json("grey_anime")
    GreyAnime,
    @Json("disabled")
    Disabled,
    @Json("disabled_anime")
    DisabledAnime,
    @Json("aborted")
    Aborted,
    @Json("aborted_anime")
    AbortedAnime,
    @Json("notbuilt")
    NotBuilt,
    @Json("notbuilt_anime")
    NotBuiltAnime,
}