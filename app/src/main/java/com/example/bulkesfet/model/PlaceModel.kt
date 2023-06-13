package com.example.bulkesfet.model

import com.google.gson.annotations.SerializedName

data class PlaceModel(
    @SerializedName("id")
    val id: String,
    @SerializedName("placeName")
    val placeName: String,
    @SerializedName("category")
    val category: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("price")
    val price: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("images")
    val images: List<String>
)