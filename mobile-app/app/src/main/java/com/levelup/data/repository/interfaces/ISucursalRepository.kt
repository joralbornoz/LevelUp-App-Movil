package com.levelup.data.repository.interfaces

import com.levelup.data.Sucursal

interface ISucursalRepository {
    suspend fun obtenerSucursales(): List<Sucursal>
}
