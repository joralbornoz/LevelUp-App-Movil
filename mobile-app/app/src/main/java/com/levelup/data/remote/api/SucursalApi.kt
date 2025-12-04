package com.levelup.data.remote.api

import com.levelup.data.remote.dto.SucursalDto
import retrofit2.http.GET
import retrofit2.http.Path

interface SucursalApi {

    @GET("sucursales")
    suspend fun getSucursales(): List<SucursalDto>

    @GET("sucursales/{id}")
    suspend fun getSucursal(
        @Path("id") id: Long
    ): SucursalDto
}
