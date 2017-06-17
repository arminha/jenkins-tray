package com.github.arminha.jenkinstray.data

import groovy.transform.CompileStatic
import groovy.transform.ToString

@CompileStatic
@ToString
class JenkinsStatus {
  static enum Status {
    Success,
    Unstable,
    Failure,
    NotBuilt,
    Unknown
  }
  final Status status;
  final Optional<Job> causedBy;

  private JenkinsStatus(Status status, Optional<Job> causedBy) {
    this.status = status
    this.causedBy = causedBy
  }

  static JenkinsStatus unknown() {
    new JenkinsStatus(JenkinsStatus.Status.Unknown, Optional.empty())
  }

  static JenkinsStatus fromJob(Job job) {
    switch (job.color) {
      case Color.Red:
      case Color.RedAnime:
        return new JenkinsStatus(Status.Failure, Optional.of(job))
      case Color.Yellow:
      case Color.YellowAnime:
        return new JenkinsStatus(Status.Unstable, Optional.of(job))
      case Color.Blue:
      case Color.BlueAnime:
        return new JenkinsStatus(Status.Success, Optional.empty())
      case Color.NotBuilt:
      case Color.NotBuiltAnime:
        return new JenkinsStatus(Status.NotBuilt, Optional.empty())
      default:
        return new JenkinsStatus(Status.Unknown, Optional.empty())
    }
  }

  JenkinsStatus aggregate(JenkinsStatus other) {
    if (status == Status.Unknown) {
      return other
    }
    if (status == Status.NotBuilt) {
      return other.status == JenkinsStatus.Status.Unknown ? this : other
    }
    if (status == Status.Success) {
      if (other.status == Status.Unstable || other.status == Status.Failure) {
        return other
      }
      return this
    }
    if (status == Status.Unstable) {
      if (other.status == Status.Failure) {
        return other
      }
      if (other.status == Status.Unstable) {
        return moreRecent(this, other)
      }
      return this
    }
    if (status == Status.Failure) {
      if (other.status == Status.Failure) {
        return moreRecent(this, other)
      }
      return this
    }
  }

  private static JenkinsStatus moreRecent(JenkinsStatus s1, JenkinsStatus s2) {
    def t1 = s1.causedBy.flatMap({ Job j -> j.buildTimestamp() }).orElse(0L)
    def t2 = s2.causedBy.flatMap({ Job j -> j.buildTimestamp() }).orElse(0L)
    return t1 >= t2 ? s1 : s2
  }
}
