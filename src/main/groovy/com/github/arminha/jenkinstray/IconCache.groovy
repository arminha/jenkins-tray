package com.github.arminha.jenkinstray

import groovy.transform.CompileStatic

import java.awt.Image
import java.awt.Toolkit

@CompileStatic
class IconCache {
  private final int iconWidth
  private final int iconHeight
  private final Map<String, Image> cache

  IconCache(int iconWidth, int iconHeight) {
    this.iconWidth = iconWidth
    this.iconHeight = iconHeight
    this.cache = new HashMap<>()
  }

  Image getImage(String resourceName) {
    def img = cache.get(resourceName)
    if (img == null) {
      img = loadImage(resourceName)
      cache.put(resourceName, img)
    }
    img
  }

  private Image loadImage(String resourceName) {
    def url = Thread.currentThread().contextClassLoader.getResource(resourceName)
    def img = Toolkit.defaultToolkit.getImage(url)
    img.getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH)
  }

}
