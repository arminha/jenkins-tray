package com.github.arminha.jenkinstray

import com.github.arminha.jenkinstray.data.JenkinsStatus
import groovy.transform.CompileStatic

import java.awt.Image
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.SystemTray
import java.awt.Toolkit
import java.awt.TrayIcon

@CompileStatic
class Tray {
  private JenkinsStatus status = JenkinsStatus.unknown()
  private SystemTray systemTray
  private TrayIcon icon
  private int iconWidth
  private int iconHeight

  Tray() {
    systemTray = SystemTray.systemTray
    def size = systemTray.getTrayIconSize()
    iconWidth = (int) size.width
    iconHeight = (int) size.height
    icon = new TrayIcon(loadImage(status), "Jenkins tray", new PopupMenu())
  }

  void setStatus(JenkinsStatus status) {
    if (!this.status.equals(status)) {
      this.status = status
      icon.setImage(loadImage(status))
    }
  }

  void addMenuItem(String label, Closure callback) {
    def item = new MenuItem()
    item.setLabel(label)
    item.addActionListener({
      callback()
    })
    icon.popupMenu.add(item)
  }

  void show() {
    systemTray.add(icon)
  }

  private Image loadImage(JenkinsStatus status) {
    def url = Thread.currentThread().contextClassLoader.getResource(iconName(status))
    def img = Toolkit.defaultToolkit.getImage(url)
    img.getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH)
  }

  private String iconName(JenkinsStatus status) {
    switch (status.status) {
      case JenkinsStatus.Status.Success:
        return 'blue.png'
      case JenkinsStatus.Status.Unstable:
        return 'yellow.png'
      case JenkinsStatus.Status.Failure:
        return 'red.png'
      default:
        return 'grey.png'
    }
  }
}
