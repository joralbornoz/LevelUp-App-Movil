package com.levelup.data.repository.interfaces

import com.levelup.data.Productos

interface IProductoRepository {

    suspend fun obtenerProductos(): List<Productos>

    suspend fun actualizarProducto(producto: Productos): Productos
}
