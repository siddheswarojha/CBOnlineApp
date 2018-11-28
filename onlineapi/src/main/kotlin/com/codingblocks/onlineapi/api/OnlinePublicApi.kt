package com.codingblocks.onlineapi.api

import com.codingblocks.onlineapi.models.*
import com.google.gson.JsonObject
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface OnlinePublicApi {


    @get:GET("courses")
    val courses: Call<ArrayList<Course>>

    @GET("courses/{id}")
    fun courseById(
            @Path("id") id: String
    ): Call<Course>

    @GET("instructors")
    fun instructors(
            @Query("include") include: Array<String>? = null
    ): Call<ArrayList<Instructor>>

    @GET("instructors/{id}")
    fun instructorsById(@Path("id") id: String): Call<Instructor>

    @GET("courses")
    fun getRecommendedCourses(@Query("exclude") query: String = "ratings",
                              @Query("filter[recommended]") recommended: String = "true",
                              @Query("filter[unlisted]") unlisted: String = "false",
                              @Query("include") include: String = "instructors,runs",
                              @Query("sort") sort: String = "difficulty"): Call<ArrayList<Course>>

    @GET("courses")
    fun getAllCourses(@Query("exclude") query: String = "ratings",
                      @Query("filter[unlisted]") unlisted: String = "false",
                      @Query("include") include: String = "instructors,runs",
                      @Query("sort") sort: String = "difficulty"): Call<ArrayList<Course>>

    @GET("sections/{id}")
    fun getSections(@Path("id") id: String,
                    @Query("exclude") query: String = "contents.*",
                    @Query("include") include: String = "contents",
                    @Query("sort") sort: String = "content.section_content.order"): Deferred<Response<Sections>>

    @GET("courses/{id}/rating")
    fun getCourseRating(@Path("id") id: String): Call<RatingModel>

    @GET("users/me")
    fun getMe(@Header("Authorization") authorization: String): Call<JsonObject>


    @POST("login")
    @FormUrlEncoded
    fun getToken(@Field("code") code: String): Call<JsonObject>

    @GET("runs")
    fun getMyCourses(@Header("Authorization") authorization: String,
                     @Query("enrolled") enrolled: String = "true",
                     @Query("include") include: String = "course,run_attempts"): Call<ArrayList<MyCourseRuns>>

    @GET("run_attempts/{runid}")
    fun enrolledCourseById(@Header("Authorization") authorization: String,
                           @Path("runid") id: String): Call<MyRunAttempt>


    @GET("run_attempts/{runid}/progress")
    fun getMyCourseProgress(@Header("Authorization") authorization: String,
                            @Path("runid") id: String): Call<HashMap<Any, Any>>

    /*
    Hit this endpoint to get the API KEY for downloading video and the m3u8 files
    The videoUrl param contains the URL of the file that you're trying to download.
    In case you get a 403, hit this endpoint again and fetch a fresh api

    response :
    {
        policyString : "",
        signature : "",
        keyId : ""
    }

    Send this response as a query param to the API endpoint that lets you download the video
     */
    @GET("aws/cookie")
    fun getVideoDownloadKey(@Query("url") videoUrl: String)

    /*
    send the video_url that you receive in ContentLectureType
    fileType is the type of file you plan on downloading, start with index.m3u8 then video.m3u8 and video.key
    Store them in internal storage, inside Android/Data/com.codingblocks.cbonlineapp/files/data/{video_url}

    Hit this endpoint again by traversing through the returned ArrayList in MediaUtils.getCourseDownloadUrls()
     */

    @GET("{videoUrl}/{fileType}")
    fun getIndexm3u8(@Path("videoUrl") videoUrl: String, @Path("fileType") fileType: String)

}