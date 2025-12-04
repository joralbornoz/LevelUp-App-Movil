package com.levelup.data.repository

import com.levelup.data.Sucursal
import com.levelup.data.remote.RetrofitClient
import com.levelup.data.remote.dto.SucursalDto

class SucursalRepository {

    private val api = RetrofitClient.sucursalApi

    private fun mapToUi(dto: SucursalDto): Sucursal {
        return Sucursal(
            id = dto.id ?: 0L,
            nombre = dto.nombre,
            direccion = dto.direccion,
            ciudad = dto.ciudad ?: "",
            horario = dto.horario ?: "",
            telefono = dto.telefono ?: ""
        )
    }

    suspend fun obtenerSucursales(): List<Sucursal> {
        return api.getSucursales().map { mapToUi(it) }
    }
}