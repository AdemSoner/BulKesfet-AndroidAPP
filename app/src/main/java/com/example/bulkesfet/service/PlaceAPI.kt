package com.example.bulkesfet.service

import com.example.bulkesfet.model.PlaceModel
import retrofit2.Call
import retrofit2.http.GET

interface PlaceAPI {
    @GET("bulkesfet/place")
    fun getPlaceDetails(): Call<List<PlaceModel>>
}

