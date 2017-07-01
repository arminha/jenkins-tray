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
}