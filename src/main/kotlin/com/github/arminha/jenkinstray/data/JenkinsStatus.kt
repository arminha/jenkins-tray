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

sealed class JenkinsStatus {
    object Success : JenkinsStatus()
    data class Unstable(val causedBy: Job) : JenkinsStatus()
    data class Failure(val causedBy: Job) : JenkinsStatus()
    object NotBuilt : JenkinsStatus()
    object Unknown : JenkinsStatus()

    companion object {
        fun fromJob(job: Job): JenkinsStatus {
            return when (job.color) {
                Color.Red, Color.RedAnime -> Failure(job)
                Color.Yellow, Color.YellowAnime -> Unstable(job)
                Color.Blue, Color.BlueAnime -> Success
                Color.NotBuilt, Color.NotBuiltAnime -> NotBuilt
                else -> Unknown
            }
        }
    }

    fun aggregate(other: JenkinsStatus): JenkinsStatus {
        fun moreRecent(job1: Job, job2: Job): Job {
            val t1 = job1.buildTimestamp().orElse(0)
            val t2 = job2.buildTimestamp().orElse(0)
            return if (t1 >= t2) job1 else job2
        }
        return when (this) {
            Unknown -> other
            NotBuilt -> when (other) {
                Unknown -> this
                else -> other
            }
            Success -> when (other) {
                is Unstable, is Failure -> other
                else -> this
            }
            is Unstable -> when (other) {
                is Failure -> other
                is Unstable -> Unstable(moreRecent(this.causedBy, other.causedBy))
                else -> this
            }
            is Failure -> when (other) {
                is Failure -> Failure(moreRecent(this.causedBy, other.causedBy))
                else -> this
            }
        }
    }

    override fun toString(): String {
        return when(this) {
            Unknown -> "Unknown"
            NotBuilt -> "NotBuilt"
            Success -> "Success"
            is Unstable -> "Unstable($causedBy)"
            is Failure -> "Failure($causedBy)"
        }
    }
}