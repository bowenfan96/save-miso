package com.example.snapcycle.server

import okhttp3.ResponseBody
import org.junit.Test
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ServerTest {

    @Test
    fun testServer() {
        var msg = ""
        val carbon = ServerAPI.service.getCarbon("Can").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                msg = response.toString()
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            }
        })
        val user =
            ServerAPI.service.createNewUser("TestUser").enqueue(object : Callback<ResponseBody> {
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    msg = response.toString()
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                }
            })
    }

}