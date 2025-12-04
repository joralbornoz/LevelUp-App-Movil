// src/test/java/com/levelup/viewmodel/AppViewModelCarritoTest.kt
package com.levelup.viewmodel

import android.app.Application
import com.levelup.MainDispatcherRule
import com.levelup.data.DatosUsuario
import com.levelup.data.Productos
import com.levelup.data.repository.*
import com.levelup.ui.viewmodel.AppViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import io.mockk.every

class AppViewModelCarritoTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val productoRepo = mockk<ProductoRepository>()
    private val sucursalRepo = mockk<SucursalRepository>(relaxed = true)
    private val usuarioRepo = mockk<UsuarioRepository>(relaxed = true)
    private val compraRepo = mockk<CompraRepository>(relaxed = true)
    private val dealsRepo = mockk<DealsRepository>(relaxed = true)



    // Mock de DatosUsuario para no tocar DataStore real
    private val datos = mockk<DatosUsuario>(relaxed = true).apply {
        // carrito vacío al inicio
        every { carritoIds } returns flowOf("")
        // email vacío (evita que intente sincronizar usuario en init)
        every { emailUsuario } returns flowOf("")
        every { nombreUsuario } returns flowOf("")
        every { edadUsuario } returns flowOf("")
        every { direccionUsuario } returns flowOf("")
        every { preferenciasUsuario } returns flowOf("")
        every { ordenesCsv } returns flowOf("")
    }

    @Test
    fun `agregarEnCarrito no permite superar el stock`() = runTest {
        // Producto con stock 2
        val prod = Productos(
            codigo = "CTRL001",
            nombre = "Control Inalámbrico",
            precio = 20000,
            descripcion = "Control gamer",
            stock = 2
        )

        coEvery { productoRepo.obtenerProductos() } returns listOf(prod)

        val vm = AppViewModel(
            app = app,
            productoRepository = productoRepo,
            sucursalRepository = sucursalRepo,
            usuarioRepository = usuarioRepo,
            compraRepository = compraRepo,
            dealsRepository = dealsRepo,
            datosUsuario = datos
        )
        // Pre-cargamos productos desde API (rellena vm.productos)
        vm.cargarProductosDesdeApi()
        advanceUntilIdle()

        // Agregamos 3 veces, pero solo debería aceptar 2 por stock
        vm.agregarEnCarrito("CTRL001")
        vm.agregarEnCarrito("CTRL001")
        vm.agregarEnCarrito("CTRL001")

        val carrito = vm.carrito.value
        val mensaje = vm.mensajeCarrito.value

        assertEquals(2, carrito.size)
        assertEquals("No puedes agregar más. Stock disponible: 2", mensaje)
    }

    @Test
    fun `total calcula correctamente el monto del carrito`() = runTest {
        val prod1 = Productos(
            codigo = "P1",
            nombre = "Mouse",
            precio = 10000,
            descripcion = "",
            stock = 10
        )
        val prod2 = Productos(
            codigo = "P2",
            nombre = "Teclado",
            precio = 20000,
            descripcion = "",
            stock = 5
        )

        coEvery { productoRepo.obtenerProductos() } returns listOf(prod1, prod2)

        val vm = AppViewModel(
            app = app,
            productoRepository = productoRepo,
            sucursalRepository = sucursalRepo,
            usuarioRepository = usuarioRepo,
            compraRepository = compraRepo,
            dealsRepository = dealsRepo,
            datosUsuario = datos
        )

        // NO LLAMAR cargarProductosDesdeApi() porque llama a Retrofit real
        // El ViewModel usará DataProductos automáticamente

        vm.agregarEnCarrito("MOUSE1")
        vm.agregarEnCarrito("TECLADO1")

        val total = vm.total()
        assertEquals(35000, total)
    }

    @Test
    fun `limpiarCarrito deja el carrito vacío`() = runTest {
        val prod = Productos(
            codigo = "P1",
            nombre = "Mouse",
            precio = 10000,
            descripcion = "",
            stock = 10
        )

        coEvery { productoRepo.obtenerProductos() } returns listOf(prod)

        val vm = AppViewModel(
            app = app,
            productoRepository = productoRepo,
            sucursalRepository = sucursalRepo,
            usuarioRepository = usuarioRepo,
            compraRepository = compraRepo,
            dealsRepository = dealsRepo,
            datosUsuario = datos
        )

        vm.cargarProductosDesdeApi()
        advanceUntilIdle()

        vm.agregarEnCarrito("P1")
        vm.agregarEnCarrito("P1")

        // ahora limpiamos
        vm.limpiarCarrito()

        assertEquals(0, vm.carrito.value.size)
    }
}
