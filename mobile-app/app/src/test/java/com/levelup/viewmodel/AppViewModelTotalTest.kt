package com.levelup.ui.viewmodel

import android.app.Application
import com.levelup.MainDispatcherRule
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import com.levelup.data.Productos
import kotlinx.coroutines.flow.MutableStateFlow


class AppViewModelTotalTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val app: Application = mockk(relaxed = true)

    @Test
    fun `total es 0 cuando el carrito esta vacio`() {
        val viewModel = AppViewModel(app)

        // Al iniciar, el carrito está vacío
        assertEquals(0, viewModel.carrito.value.size)

        // Por lo tanto, el total debe ser 0
        assertEquals(0, viewModel.total())
    }
    @Test
    fun `total calcula correctamente`() {
        val app = mockk<Application>(relaxed = true)
        val vm = AppViewModel(app)

        // Injectar productos
        val productosField = vm.javaClass.getDeclaredField("_productos")
        productosField.isAccessible = true
        val productosFlow = productosField.get(vm) as MutableStateFlow<List<Productos>>
        productosFlow.value = listOf(
            Productos("P1", "Producto 1", 1000, "desc", 10),
            Productos("P2", "Producto 2", 2000, "desc", 10)
        )

        // Injectar carrito
        val carritoField = vm.javaClass.getDeclaredField("_carrito")
        carritoField.isAccessible = true
        val carritoFlow = carritoField.get(vm) as MutableStateFlow<List<String>>
        carritoFlow.value = listOf("P1", "P2", "P2")  // 1×1000 + 2×2000 = 5000

        // Ejecutar lógica
        val total = vm.total()

        assertEquals(5000, total)
    }
}
