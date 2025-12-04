package com.levelup.data.repository

import com.levelup.data.Productos
import com.levelup.data.remote.RetrofitClient
import com.levelup.data.remote.api.ProductoApi
import com.levelup.data.remote.dto.ProductoDto
import com.levelup.data.repository.interfaces.IProductoRepository

class ProductoRepository(
    private val api: ProductoApi = RetrofitClient.productoApi   // 👈
) : IProductoRepository {

    override suspend fun obtenerProductos(): List<Productos> {
        val listaDto = api.getProductos()
        return listaDto.map { dto ->
            Productos(
                codigo = dto.codigo,
                nombre = dto.nombre,
                precio = dto.precio,
                descripcion = dto.descripcion ?: "",
                stock = dto.stock
            )
        }
    }

    override suspend fun actualizarProducto(producto: Productos): Productos {
        val dto = ProductoDto(
            codigo = producto.codigo,
            nombre = producto.nombre,
            precio = producto.precio,
            descripcion = producto.descripcion,
            stock = producto.stock
        )
        val actualizado = api.actualizarProducto(producto.codigo, dto)
        return Productos(
            codigo = actualizado.codigo,
            nombre = actualizado.nombre,
            precio = actualizado.precio,
            descripcion = actualizado.descripcion ?: "",
            stock = actualizado.stock
        )
    }
}
