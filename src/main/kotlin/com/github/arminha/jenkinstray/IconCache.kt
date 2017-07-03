package com.github.arminha.jenkinstray

import java.awt.Image
import java.awt.Toolkit

class IconCache {
    private val iconWidth: Int
    private val iconHeight: Int
    private val cache: MutableMap<String, Image>

    constructor(iconWidth: Int, iconHeight: Int) {
        this.iconWidth = iconWidth
        this.iconHeight = iconHeight
        this.cache = HashMap()
    }

    fun getImage(resourceName: String): Image {
        var img = cache[resourceName]
        if (img == null) {
            img = loadImage(resourceName)
            cache.put(resourceName, img)
        }
        return img
    }

    private fun loadImage(resourceName: String): Image {
        val url = Thread.currentThread().contextClassLoader.getResource(resourceName)
        val img = Toolkit.getDefaultToolkit().getImage(url)
        return img.getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH)
    }
}