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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.arminha.jenkinstray.data.JenkinsStatus
import com.github.arminha.jenkinstray.data.Job
import java.nio.charset.StandardCharsets
import java.util.Base64
import kotlin.collections.ArrayList
import okhttp3.OkHttpClient
import okhttp3.Request

class JenkinsView(val url: String, val username: String?, val accessToken: String?) {
    companion object {
        val httpClient = OkHttpClient()
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class JobList(val jobs: List<Job>)

    fun retrieveJobs(): List<Job> {
        val request = Request.Builder()
                .url(url + "api/json?tree=jobs[name,color,lastBuild[number,result,timestamp]]")
        if (username != null && accessToken != null) {
            val auth = username + ":" + accessToken
            val encodedAuth = Base64.getMimeEncoder().encodeToString(
                    auth.toByteArray(StandardCharsets.ISO_8859_1))
            val authHeader = "Basic " + encodedAuth
            request.addHeader("Authorization", authHeader)
        }
        val mapper = jacksonObjectMapper()
        val response = httpClient.newCall(request.build()).execute()
        val list = response.use {
            val status = response.code()
            if (status in 200..299) {
                val content = response.body()!!.bytes()
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