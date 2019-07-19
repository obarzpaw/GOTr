package com.example.gotr.data.source.remote


import com.example.gotr.data.Character
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface GotAPIService {
    @GET("api/show/characters/bySlug/{character}")
    fun getCharacter(@Path("character") character: String): Call<Character>

    companion object Factory {
        fun create(): GotAPIService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.got.show/")
                .build()

            return retrofit.create(GotAPIService::class.java)
        }
    }
}