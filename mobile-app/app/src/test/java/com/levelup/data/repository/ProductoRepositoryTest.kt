package com.levelup.data.repository

import com.levelup.data.Productos
import com.levelup.data.remote.api.ProductoApi
import com.levelup.data.remote.dto.ProductoDto
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductoRepositoryTest {

    private val apiMock = mockk<ProductoApi>()

    @Test
    fun `obtenerProductos mapea correctamente los datos de la API`() = runTest {
        // Arrange: simulamos respuesta de la API
        val dtoList = listOf(
            ProductoDto(
                codigo = "CTRL001",
                nombre = "Control Inalámbrico",
                precio = 20000,
                descripcion = "Control gamer",
                stock = 5
            )
        )

        coEvery { apiMock.getProductos() } returns dtoList

        val repo = ProductoRepository(apiMock)

        // Act
        val resultado: List<Productos> = repo.obtenerProductos()

        // Assert
        assertEquals(1, resultado.size)
        val p = resultado.first()
        assertEquals("CTRL001", p.codigo)
        assertEquals("Control Inalámbrico", p.nombre)
        assertEquals(20000, p.precio)
        assertEquals("Control gamer", p.descripcion)
        assertEquals(5, p.stock)

        coVerify(exactly = 1) { apiMock.getProductos() }
    }

    @Test
    fun `actualizarProducto envía dto correcto a la API y mapea la respuesta`() = runTest {
        // Arrange
        val producto = Productos(
            codigo = "CTRL001",
            nombre = "Control Inalámbrico",
            precio = 20000,
            descripcion = "Control gamer",
            stock = 3
        )

        val dtoRespuesta = ProductoDto(
            codigo = "CTRL001",
            nombre = "Control Inalámbrico",
            precio = 20000,
            descripcion = "Control gamer actualizado",
            stock = 2
        )

        coEvery { apiMock.actualizarProducto("CTRL001", any()) } returns dtoRespuesta

        val repo = ProductoRepository(apiMock)

        // Act
        val actualizado = repo.actualizarProducto(producto)

        // Assert
        assertEquals("CTRL001", actualizado.codigo)
        assertEquals("Control Inalámbrico", actualizado.nombre)
        assertEquals(20000, actualizado.precio)
        assertEquals("Control gamer actualizado", actualizado.descripcion)
        assertEquals(2, actualizado.stock)

        coVerify(exactly = 1) {
            apiMock.actualizarProducto("CTRL001", any())
        }
    }
}
