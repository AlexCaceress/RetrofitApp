package com.example.retrofit2.data

import com.example.retrofit2.models.AllMemesData
import com.example.retrofit2.models.Meme
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {

    @GET("get_memes")
    suspend fun getMemesList() : Response<AllMemesData>

}