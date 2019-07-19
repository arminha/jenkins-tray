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
package com.github.arminha.jenkinstray

import java.awt.Image
import java.awt.Toolkit

class IconCache(private val iconWidth: Int, private val iconHeight: Int) {
    private val cache: MutableMap<String, Image>

    init {
        this.cache = HashMap()
    }

    fun getImage(resourceName: String): Image {
        var img = cache[resourceName]
        if (img == null) {
            img = loadImage(resourceName)
            cache[resourceName] = img
        }
        return img
    }

    private fun loadImage(resourceName: String): Image {
        val url = Thread.currentThread().contextClassLoader.getResource(resourceName)
        val img = Toolkit.getDefaultToolkit().getImage(url)
        return img.getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH)
    }
}