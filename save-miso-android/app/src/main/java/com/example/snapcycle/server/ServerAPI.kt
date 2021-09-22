package com.example.snapcycle.server

import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

class ServerAPI {

    interface APIService {
        @GET("/item/{item}")
        fun getCarbon(@Path("item") item: String): Call<ResponseBody>

        @GET("/newuser/{username}")
        fun createNewUser(@Path("username") item: String): Call<ResponseBody>

        @GET("/scores/{username}")
        fun getUserScore(@Path("username") item: String): Call<ResponseBody>

        @GET("/update/{username_score}")
        fun setUserScore(@Path("username_score") item: String): Call<ResponseBody>

        @GET("/updatehealth/{username_health}")
        fun updateHealth(@Path("username_health") item: String): Call<ResponseBody>

        // THIS IS FOR POST METHOD - WE NOT USING THIS
        // @Headers("Content-type: application/json")
        // @POST("/api/post_some_data")
        // fun getVectors(@Body body: JsonObject): Call<ResponseBody>
    }

    companion object {
        private val retrofit = Retrofit.Builder()
            .baseUrl("https://snapcycle-backend.herokuapp.com")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()

        var service: APIService = retrofit.create(APIService::class.java)
    }
}