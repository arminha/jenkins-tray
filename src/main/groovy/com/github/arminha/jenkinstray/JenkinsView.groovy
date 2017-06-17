package com.github.arminha.jenkinstray

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.arminha.jenkinstray.data.JenkinsStatus
import com.github.arminha.jenkinstray.data.Job
import groovy.transform.CompileStatic
import groovy.transform.Immutable

@CompileStatic
@Immutable
class JenkinsView {
  String url
  String accessToken

  @Immutable
  @JsonIgnoreProperties(ignoreUnknown = true)
  static class JobList {
    List<Job> jobs
  }

  List<Job> retrieveJobs() {
    def json = (url + 'api/json?tree=jobs[name,color,lastBuild[number,result,timestamp]]')
        .toURL().text
    ObjectMapper mapper = new ObjectMapper()
    def list = mapper.readValue(json, JobList)
    list.jobs
  }

  JenkinsStatus aggregateStatus(List<Job> jobs) {
    def status = JenkinsStatus.unknown()
    for(job in jobs) {
      def next = JenkinsStatus.fromJob(job)
      status = status.aggregate(next)
    }
    status
  }
}
