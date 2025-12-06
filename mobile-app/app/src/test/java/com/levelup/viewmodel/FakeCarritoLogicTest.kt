package com.levelup.viewmodel

import com.levelup.data.Productos
import org.junit.Assert.*
import org.junit.Test

class FakeCarritoLogicTest {

    private fun productosDemo() = listOf(
        Productos("P1", "Producto 1", 1000, "Desc 1", 2),
        Productos("P2", "Producto 2", 2000, "Desc 2", 5)
    )

    @Test
    fun `agregarEnCarrito agrega producto cuando hay stock`() {
        val logic = FakeCarritoLogic(productosDemo())

        logic.agregarEnCarrito("P1")

        assertEquals(listOf("P1"), logic.carrito.value)
        assertNull(logic.mensajeCarrito.value)
    }

    @Test
    fun `agregarEnCarrito no permite superar el stock`() {
        val logic = FakeCarritoLogic(productosDemo())

        // stock P1 = 2
        logic.agregarEnCarrito("P1")
        logic.agregarEnCarrito("P1") // aquí ya llega al límite
        logic.agregarEnCarrito("P1") // este debería fallar

        assertEquals(listOf("P1", "P1"), logic.carrito.value)
        assertEquals(
            "No puedes agregar más. Stock disponible: 2",
            logic.mensajeCarrito.value
        )
    }

    @Test
    fun `total calcula correctamente el monto del carrito`() {
        val logic = FakeCarritoLogic(productosDemo())

        logic.agregarEnCarrito("P1") // 1000
        logic.agregarEnCarrito("P2") // 2000
        logic.agregarEnCarrito("P2") // 2000

        val total = logic.total()

        assertEquals(5000, total)  // 1000 + 2000 + 2000
    }

    @Test
    fun `limpiarCarrito deja el carrito vacio`() {
        val logic = FakeCarritoLogic(productosDemo())

        logic.agregarEnCarrito("P1")
        logic.agregarEnCarrito("P2")

        logic.limpiarCarrito()

        assertTrue(logic.carrito.value.isEmpty())
    }
}
