package com.levelup.data.repository

import com.levelup.data.Compra
import com.levelup.data.remote.RetrofitClient
import com.levelup.data.remote.dto.CompraDto

class CompraRepository {

    private val api = RetrofitClient.compraApi

    suspend fun crearCompra(
        emailUsuario: String,
        total: Int,
        detalle: String
    ): Compra {
        val dto = CompraDto(
            id = null,
            emailUsuario = emailUsuario,
            fecha = null,
            total = total,
            detalle = detalle
        )

        val creada = api.crearCompra(dto)

        return Compra(
            id = creada.id ?: 0L,
            emailUsuario = creada.emailUsuario,
            fecha = creada.fecha ?: "",
            total = creada.total,
            detalle = creada.detalle
        )
    }

    suspend fun obtenerComprasPorUsuario(email: String): List<Compra> {
        val listaDto = api.getComprasPorUsuario(email)
        return listaDto.map { dto ->
            Compra(
                id = dto.id ?: 0L,
                emailUsuario = dto.emailUsuario,
                fecha = dto.fecha ?: "",
                total = dto.total,
                detalle = dto.detalle
            )
        }
    }
}
