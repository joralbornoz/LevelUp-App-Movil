package com.levelup.data.repository

import com.levelup.data.external.Deal
import com.levelup.data.remote.RetrofitClient
import com.levelup.data.repository.interfaces.IDealsRepository

class DealsRepository : IDealsRepository {

    private val api = RetrofitClient.cheapSharkApi

    override suspend fun obtenerDeals(): List<Deal> {
        val lista = api.getTopDeals()

        return lista.map { dto ->
            Deal(
                titulo = dto.title,
                precioOferta = dto.salePrice,
                precioNormal = dto.normalPrice,
                rating = dto.dealRating,
                imagen = dto.thumb
            )
        }
    }
}
