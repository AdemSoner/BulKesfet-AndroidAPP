package com.example.bulkesfet.service

import com.example.bulkesfet.model.PlaceModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT

// BASE URL = bulkesfet/places

interface PlaceAPI {
    @GET("bulkesfet/place2")
    fun getPlaceDetails(): Call<List<PlaceModel>>

    @GET("bulkesfet/allList?category=flight|hotel|transportation")
    suspend fun getCategoryAllInfo(): List<PlaceModel>

    @GET("bulkesfet/allList?category=flight")
    suspend fun getCategoryFlightInfo(): List<PlaceModel>

    @GET("bulkesfet/allList?category=hotel")
    suspend fun getCategoryHotelInfo(): List<PlaceModel>

    @GET("bulkesfet/allList?category=transportation")
    suspend fun getCategoryTransportationInfo(): List<PlaceModel>

    @GET("bulkesfet/allList?category=topdestination")
    suspend fun getTopDestinationInfo(): List<PlaceModel>

    @GET("bulkesfet/allList?category=nearby")
    suspend fun getNearbyInfo(): List<PlaceModel>

    @GET("bulkesfet/allList?category=mightneed")
    suspend fun getMightNeedInfo(): List<PlaceModel>

    @GET("bulkesfet/allList?category=toppick")
    suspend fun getTopPickInfo(): List<PlaceModel>

    @GET("bulkesfet/allList?isBookmark=true")
    suspend fun getBookmarkTrueInfo(): List<PlaceModel>

    @GET("bulkesfet/allList?isTrip=true")
    suspend fun getTripTrueInfo(): List<PlaceModel>
}

