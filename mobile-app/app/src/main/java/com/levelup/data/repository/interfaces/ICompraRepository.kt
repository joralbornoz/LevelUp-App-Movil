package com.levelup.data.repository.interfaces

import com.levelup.data.Compra

interface ICompraRepository {

    suspend fun crearCompra(emailUsuario: String, total: Int, detalle: String)

    suspend fun obtenerComprasPorUsuario(email: String): List<Compra>
}
