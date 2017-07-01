package com.github.arminha.jenkinstray

import com.github.arminha.jenkinstray.data.Build
import com.github.arminha.jenkinstray.data.BuildResult
import com.github.arminha.jenkinstray.data.Color
import com.github.arminha.jenkinstray.data.JenkinsStatus
import com.github.arminha.jenkinstray.data.Job
import spock.lang.Specification

class JenkinsViewSpec extends Specification {
  def view = new JenkinsView("", null, null)

  def "aggregate empty job list"() {
    when:
    def status = view.aggregateStatus([])

    then:
    status == JenkinsStatus.Unknown.INSTANCE
  }

  def "aggregate one job"() {
    when:
    def job = new Job("test", Color.NotBuilt, null)
    def status = view.aggregateStatus([job])

    then:
    status == JenkinsStatus.NotBuilt.INSTANCE
  }

  def "aggregate two jobs"() {
    when:
    def job1 = new Job("test1", Color.Yellow, new Build(1, BuildResult.Unstable, 5))
    def job2 = new Job("test2", Color.Red, new Build(2, BuildResult.Failure, 10))
    def status = view.aggregateStatus([job1, job2])

    then:
    status == new JenkinsStatus.Failure(job2)
  }
}
