package com.levelup.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GameDealDto(
    @SerializedName("title")
    val title: String,

    @SerializedName("salePrice")
    val salePrice: String,

    @SerializedName("normalPrice")
    val normalPrice: String,

    @SerializedName("dealRating")
    val dealRating: String,

    @SerializedName("thumb")
    val thumb: String
)