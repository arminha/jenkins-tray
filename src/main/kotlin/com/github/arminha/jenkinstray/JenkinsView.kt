package com.github.arminha.jenkinstray

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.arminha.jenkinstray.data.JenkinsStatus
import com.github.arminha.jenkinstray.data.Job
import org.apache.http.HttpHeaders
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import java.nio.charset.StandardCharsets
import java.util.Base64
import kotlin.collections.ArrayList

class JenkinsView(val url: String, val username: String?, val accessToken: String?) {
    companion object {
        val httpClient = HttpClients.createDefault()
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class JobList(val jobs: List<Job>)

    fun retrieveJobs(): List<Job> {
        val request = HttpGet(url + "api/json?tree=jobs[name,color,lastBuild[number,result,timestamp]]")
        if (username != null && accessToken != null) {
            val auth = username + ":" + accessToken;
            val encodedAuth = Base64.getMimeEncoder().encodeToString(
                    auth.toByteArray(StandardCharsets.ISO_8859_1));
            val authHeader = "Basic " + encodedAuth;
            request.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
        }
        val mapper = jacksonObjectMapper()
        val response = httpClient.execute(request)
        val list = response.use {
            val status = response.statusLine.statusCode
            if (status in 200..299) {
                val content = response.entity.content.readBytes()
                try {
                    mapper.readValue<JobList>(content)
                } catch (e: Exception) {
                    println("json = '" + String(content, StandardCharsets.UTF_8) + "'")
                    throw e
                }
            } else {
                println("Response status $status")
                JobList(ArrayList())
            }
        }
        return list.jobs
    }

    fun aggregateStatus(jobs: List<Job>): JenkinsStatus {
        var status: JenkinsStatus = JenkinsStatus.Unknown
        for (job in jobs) {
            val next = JenkinsStatus.fromJob(job)
            status = status.aggregate(next)
        }
        return status
    }

}