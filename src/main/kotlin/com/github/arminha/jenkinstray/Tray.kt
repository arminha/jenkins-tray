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

import com.github.arminha.jenkinstray.data.JenkinsStatus
import java.awt.Image
import java.awt.MenuItem
import java.awt.PopupMenu
import java.awt.SystemTray
import java.awt.TrayIcon

class Tray(private val title: String) {
    private var status: JenkinsStatus = JenkinsStatus.Unknown
    private val systemTray: SystemTray = SystemTray.getSystemTray()
    private val icon: TrayIcon
    private val iconCache: IconCache

    init {
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
        item.addActionListener {
            callback()
        }
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
