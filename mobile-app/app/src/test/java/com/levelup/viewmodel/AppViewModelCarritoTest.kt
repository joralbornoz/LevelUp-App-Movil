package com.levelup.viewmodel

import android.app.Application
import com.levelup.MainDispatcherRule
import com.levelup.data.Productos
import com.levelup.ui.viewmodel.AppViewModel
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class AppViewModelCarritoTest {

    @get:Rule
    val rule = MainDispatcherRule()

    private val app: Application = mockk(relaxed = true)


    @Test
    fun `total calcula la suma correcta del carrito`() {
        val vm = AppViewModel(app)

        // inyectamos productos
        val fieldProd = vm.javaClass.getDeclaredField("_productos")
        fieldProd.isAccessible = true
        val productosFlow = fieldProd.get(vm) as MutableStateFlow<List<Productos>>
        productosFlow.value = listOf(
            Productos("P1","Prod 1",1000,"Desc",10),
            Productos("P2","Prod 2",2000,"Desc",10)
        )

        // inyectamos carrito directamente, SIN llamar a agregarEnCarrito()
        val fieldCarrito = vm.javaClass.getDeclaredField("_carrito")
        fieldCarrito.isAccessible = true
        val carritoFlow = fieldCarrito.get(vm) as MutableStateFlow<List<String>>
        carritoFlow.value = listOf("P1","P2","P2") // 1×1000 + 2×2000 = 5000

        val total = vm.total()

        assertEquals(5000, total)
    }


    @Test
    fun `limpiarCarrito deja el carrito vacio`() = runTest {
        val vm = AppViewModel(app)

        val producto = Productos("PS5", "Play 5", 500000, "Consola", 5)

        val field = vm.javaClass.getDeclaredField("_productos")
        field.isAccessible = true
        val productosFlow = field.get(vm) as MutableStateFlow<List<Productos>>
        productosFlow.value = listOf(producto)

        vm.agregarEnCarrito("PS5")
        assertEquals(1, vm.carrito.value.size)

        vm.limpiarCarrito()

        assertEquals(0, vm.carrito.value.size)
    }
    @Test
    fun `agregarEnCarrito agrega un producto al carrito`() {
        val app = mockk<Application>(relaxed = true)
        val vm = AppViewModel(app)

        // --- Inyectar productos en el ViewModel ---
        val productosField = vm.javaClass.getDeclaredField("_productos")
        productosField.isAccessible = true
        val productosFlow = productosField.get(vm) as MutableStateFlow<List<Productos>>

        productosFlow.value = listOf(
            Productos("P1", "Producto 1", 1000, "desc", 5)
        )

        // --- Ejecutar lógica ---
        vm.agregarEnCarrito("P1")

        // --- Validación ---
        assertEquals(1, vm.carrito.value.size)
        assertEquals("P1", vm.carrito.value.first())
    }
    @Test
    fun `agregarEnCarrito devuelve error si stock no alcanza`() {
        val app = mockk<Application>(relaxed = true)
        val vm = AppViewModel(app)

        val productosField = vm.javaClass.getDeclaredField("_productos")
        productosField.isAccessible = true
        val flow = productosField.get(vm) as MutableStateFlow<List<Productos>>

        flow.value = listOf(
            Productos("P1", "Producto 1", 1000, "desc", 1) // stock = 1
        )

        // Primera vez entra
        vm.agregarEnCarrito("P1")

        // Segunda vez debe fallar
        vm.agregarEnCarrito("P1")

        val mensajeField = vm.javaClass.getDeclaredField("_mensajeCarrito")
        mensajeField.isAccessible = true
        val msgFlow = mensajeField.get(vm) as MutableStateFlow<String?>

        assertEquals("No puedes agregar más. Stock disponible: 1", msgFlow.value)
    }




}
