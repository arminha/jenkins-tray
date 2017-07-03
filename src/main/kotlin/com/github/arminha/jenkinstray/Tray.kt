package com.github.arminha.jenkinstray

import com.github.arminha.jenkinstray.data.JenkinsStatus
import java.awt.Image
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.SystemTray
import java.awt.TrayIcon

class Tray {
    private var status: JenkinsStatus = JenkinsStatus.Unknown
    private val systemTray: SystemTray
    private val icon: TrayIcon
    private val iconCache: IconCache
    private val title: String

    constructor(title: String) {
        this.title = title
        systemTray = SystemTray.getSystemTray()
        val size = systemTray.trayIconSize
        iconCache = IconCache(size.width, size.height)
        icon = TrayIcon(loadImage(status), title, PopupMenu())
    }

    fun setStatus(status: JenkinsStatus) {
        if (this.status != status) {
            this.status = status
            icon.image = loadImage(status)
            val cause = when (status) {
                is JenkinsStatus.Unstable -> " (${status.causedBy.name})"
                is JenkinsStatus.Failure -> " (${status.causedBy.name})"
                else -> ""
            }
            val state = status.javaClass.simpleName
            icon.toolTip = "$title - $state$cause"
        }
    }

    fun addMenuItem(label: String, callback: () -> Unit) {
        val item = MenuItem()
        item.label = label
        item.addActionListener({
            callback()
        })
        icon.popupMenu.add(item)
    }

    fun show() {
        systemTray.add(icon)
    }

    private fun loadImage(status: JenkinsStatus): Image {
        return iconCache.getImage(iconName(status))
    }

    private fun iconName(status: JenkinsStatus): String {
        return when (status) {
            JenkinsStatus.Success -> "blue.png"
            is JenkinsStatus.Unstable -> "yellow.png"
            is JenkinsStatus.Failure -> "red.png"
            else -> "grey.png"
        }
    }
}
