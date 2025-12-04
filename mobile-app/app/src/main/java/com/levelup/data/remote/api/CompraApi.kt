package com.levelup.data.remote.api

import com.levelup.data.remote.dto.CompraDto
import retrofit2.http.*

interface CompraApi {

    @POST("api/compras")
    suspend fun crearCompra(
        @Body compra: CompraDto
    ): CompraDto

    @GET("api/compras/usuario/{email}")
    suspend fun getComprasPorUsuario(
        @Path("email") email: String
    ): List<CompraDto>
}
