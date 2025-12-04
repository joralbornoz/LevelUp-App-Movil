package com.levelup.viewmodel

import android.app.Application
import com.levelup.MainDispatcherRule
import com.levelup.data.DatosUsuario
import com.levelup.data.Productos
import com.levelup.data.repository.CompraRepository
import com.levelup.data.repository.DealsRepository
import com.levelup.data.repository.ProductoRepository
import com.levelup.data.repository.SucursalRepository
import com.levelup.data.repository.UsuarioRepository
import com.levelup.data.repository.interfaces.ISucursalRepository
import com.levelup.data.repository.interfaces.IDealsRepository
import com.levelup.data.repository.interfaces.IProductoRepository
import com.levelup.data.repository.interfaces.ICompraRepository
import com.levelup.data.repository.interfaces.IUsuarioRepository
import com.levelup.ui.viewmodel.AppViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class AppViewModelTotalTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val productoRepo = mockk<ProductoRepository>()
    private val sucursalRepo = mockk<SucursalRepository>(relaxed = true)
    private val usuarioRepo = mockk<UsuarioRepository>(relaxed = true)
    private val compraRepo = mockk<CompraRepository>(relaxed = true)
    private val dealsRepo = mockk<DealsRepository>(relaxed = true)


    /**
     * Caso 1: total() debe sumar correctamente los precios
     * de los productos que están en el carrito.
     */
    @Test
    fun `total calcula correctamente el monto del carrito`() = runTest {
        // Arrange: 2 productos
        val prod1 = Productos(
            codigo = "CTRL001",
            nombre = "Control Inalámbrico",
            precio = 20000,
            descripcion = "Control gamer",
            stock = 10
        )
        val prod2 = Productos(
            codigo = "MOUSE001",
            nombre = "Mouse Gamer",
            precio = 15000,
            descripcion = "Mouse RGB",
            stock = 5
        )

        coEvery { productoRepo.obtenerProductos() } returns listOf(prod1, prod2)

        val vm = AppViewModel(app)


        vm.cargarProductosDesdeApi()

        advanceUntilIdle()


        vm.agregarEnCarrito("CTRL001")
        vm.agregarEnCarrito("CTRL001")
        vm.agregarEnCarrito("MOUSE001")

        // Act
        val total = vm.total()


        assertEquals(55000, total)
    }


    @Test
    fun `pagarYGuardarOrden limpia el carrito`() = runTest {
        val prod = Productos(
            codigo = "CTRL001",
            nombre = "Control Inalámbrico",
            precio = 20000,
            descripcion = "Control gamer",
            stock = 5
        )

        coEvery { productoRepo.obtenerProductos() } returns listOf(prod)

        val vm = AppViewModel(app)


        vm.cargarProductosDesdeApi()
        advanceUntilIdle()


        vm.agregarEnCarrito("CTRL001")
        vm.agregarEnCarrito("CTRL001")


        vm.pagarYGuardarOrden()
        advanceUntilIdle()


        val carritoFinal = vm.carrito.first()
        assertEquals(0, carritoFinal.size)
    }
}
