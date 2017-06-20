package com.github.arminha.jenkinstray

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.arminha.jenkinstray.data.JenkinsStatus
import com.github.arminha.jenkinstray.data.Job
import groovy.transform.CompileStatic
import groovy.transform.Immutable
import org.apache.http.HttpHeaders
import org.apache.http.client.methods.CloseableHttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients

import java.nio.charset.StandardCharsets

@CompileStatic
@Immutable
class JenkinsView {
  static final CloseableHttpClient httpClient = HttpClients.createDefault()

  String url
  String username
  String accessToken

  @Immutable
  @JsonIgnoreProperties(ignoreUnknown = true)
  static class JobList {
    List<Job> jobs
  }

  List<Job> retrieveJobs() {
    def request = new HttpGet(url + 'api/json?tree=jobs[name,color,lastBuild[number,result,timestamp]]')
    if (username != null && accessToken != null) {
      String auth = username + ":" + accessToken;
      String encodedAuth = Base64.mimeEncoder.encodeToString(
          auth.getBytes(StandardCharsets.ISO_8859_1));
      String authHeader = "Basic " + encodedAuth;
      request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
    }
    ObjectMapper mapper = new ObjectMapper()
    CloseableHttpResponse response = httpClient.execute(request)
    def list = response.withCloseable {
      int status = response.statusLine.statusCode
      if (status >= 200 && status < 300) {
        mapper.readValue(response.entity.content, JobList)
      } else {
        println("Response status $status")
        new JobList([])
      }
    }
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
