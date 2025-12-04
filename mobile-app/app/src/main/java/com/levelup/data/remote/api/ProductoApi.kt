package com.levelup.data.remote.api

import com.levelup.data.remote.dto.ProductoDto
import retrofit2.http.*

interface ProductoApi {

    @GET("api/productos")
    suspend fun getProductos(): List<ProductoDto>

    @GET("api/productos/{codigo}")
    suspend fun getProductoPorCodigo(
        @Path("codigo") codigo: String
    ): ProductoDto

    @PUT("api/productos/{codigo}")
    suspend fun actualizarProducto(
        @Path("codigo") codigo: String,
        @Body producto: ProductoDto
    ): ProductoDto
}
