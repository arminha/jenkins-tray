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

  Tray() {
    systemTray = SystemTray.systemTray
    icon = new TrayIcon(loadImage(status), "Jenkins tray", new PopupMenu())
  }

  void setStatus(JenkinsStatus status) {
    icon.setImage(loadImage(status))
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
    Toolkit.defaultToolkit.getImage(url)
  }

  private String iconName(JenkinsStatus status) {
    switch (status.status) {
      case JenkinsStatus.Status.Success:
        return "blue.png"
      default:
        return "grey.png"
    }
  }
}
