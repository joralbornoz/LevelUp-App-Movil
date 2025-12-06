package com.levelup.data.repository

import com.levelup.data.Productos
import com.levelup.data.repository.interfaces.IProductoRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class ProductoRepositoryTest {

    private val repo = mockk<IProductoRepository>()

    @Test
    fun `obtener productos devuelve lista`() = runTest {

        val p1 = mockk<Productos>()
        val p2 = mockk<Productos>()

        every { p1.codigo } returns "PS5"
        every { p2.codigo } returns "XBOX-X"

        coEvery { repo.obtenerProductos() } returns listOf(p1, p2)

        val resultado = repo.obtenerProductos()

        assertEquals(2, resultado.size)
        assertEquals("PS5", resultado[0].codigo)

        coVerify { repo.obtenerProductos() }
    }
}
