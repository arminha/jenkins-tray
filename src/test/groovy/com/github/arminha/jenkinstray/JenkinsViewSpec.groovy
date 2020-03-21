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

import com.github.arminha.jenkinstray.data.Build
import com.github.arminha.jenkinstray.data.BuildResult
import com.github.arminha.jenkinstray.data.Color
import com.github.arminha.jenkinstray.data.JenkinsStatus
import com.github.arminha.jenkinstray.data.Job
import spock.lang.Specification

import static java.nio.charset.StandardCharsets.UTF_8

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

  def "parse empty job list"() {
    when:
    def json = '{"jobs": []}'
    def list = view.parseJobList(new ByteArrayInputStream(json.getBytes(UTF_8)))

    then:
    list == []
  }

  def "parse job list with one job"() {
    when:
    def json = '''{"jobs": [
  {
      "name": "jobname",
      "color": "blue",
      "lastBuild": {
        "number": 28,
        "result": "SUCCESS",
        "timestamp": 1547148202107
      }
  }
]}'''
    def list = view.parseJobList(new ByteArrayInputStream(json.getBytes(UTF_8)))

    then:
    list == [new Job("jobname", Color.Blue, new Build(28, BuildResult.Success, 1547148202107))]
  }

  def "parse job with minimal data"() {
    when:
    def json = '''{"jobs": [
  {
      "name": "jobname",
      "color": "grey_anime",
      "lastBuild": {
        "number": 28,
        "result": null,
        "timestamp": 1547148202107
      }
  }
]}'''
    def list = view.parseJobList(new ByteArrayInputStream(json.getBytes(UTF_8)))

    then:
    list == [new Job("jobname", Color.GreyAnime, new Build(28, null, 1547148202107L))]
  }
}
