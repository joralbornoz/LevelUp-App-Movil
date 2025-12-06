package com.levelup.viewmodel

import com.levelup.data.Productos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Lógica de carrito simplificada para pruebas unitarias.
 * No usa Android, ViewModel ni DataStore.
 */
class FakeCarritoLogic(
    private val productosDisponibles: List<Productos>
) {

    private val _carrito = MutableStateFlow<List<String>>(emptyList())
    val carrito: StateFlow<List<String>> = _carrito.asStateFlow()

    private val _mensajeCarrito = MutableStateFlow<String?>(null)
    val mensajeCarrito: StateFlow<String?> = _mensajeCarrito.asStateFlow()

    fun agregarEnCarrito(codigo: String) {
        val producto = productosDisponibles.firstOrNull { it.codigo == codigo }

        if (producto == null) {
            _mensajeCarrito.value = "Producto no encontrado."
            return
        }

        val stockDisponible = producto.stock
        val cantidadActualEnCarrito = _carrito.value.count { it == codigo }

        if (cantidadActualEnCarrito >= stockDisponible) {
            _mensajeCarrito.value =
                "No puedes agregar más. Stock disponible: $stockDisponible"
            return
        }

        _carrito.value = _carrito.value + codigo
        _mensajeCarrito.value = null
    }

    fun limpiarCarrito() {
        _carrito.value = emptyList()
    }

    fun total(): Int {
        return _carrito.value.sumOf { codigo ->
            productosDisponibles.firstOrNull { it.codigo == codigo }?.precio ?: 0
        }
    }
}
