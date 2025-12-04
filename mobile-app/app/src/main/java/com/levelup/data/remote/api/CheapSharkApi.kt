package com.levelup.data.remote.api

import com.levelup.data.remote.dto.GameDealDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CheapSharkApi {

    @GET("deals")
    suspend fun getTopDeals(
        @Query("upperPrice") upperPrice: String = "25",
        @Query("sortBy") sortBy: String = "DealRating"
    ): List<GameDealDto>
}
