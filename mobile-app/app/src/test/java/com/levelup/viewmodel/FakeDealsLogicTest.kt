package com.levelup.viewmodel

import com.levelup.data.external.Deal
import com.levelup.data.repository.interfaces.IDealsRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class FakeDealsLogicTest {

    @Test
    fun `cargar ofertas externas exitosamente`() = runTest {
        val repo = mockk<IDealsRepository>()

        val mockDeals = listOf(
            Deal(
                titulo = "Oferta Steam",
                precioOferta = "9.99",
                precioNormal = "39.99",
                rating = "8.5",
                imagen = "http://imagen1.jpg"
            ),
            Deal(
                titulo = "GTA V",
                precioOferta = "4.99",
                precioNormal = "29.99",
                rating = "9.0",
                imagen = "http://imagen2.jpg"
            )
        )

        coEvery { repo.obtenerDeals() } returns mockDeals

        val logic = FakeDealsLogic(repo)

        logic.cargarOfertas()

        assertEquals(2, logic.ofertas.value.size)
        assertEquals("Oferta Steam", logic.ofertas.value[0].titulo)
        assertNull(logic.error.value)
    }

    @Test
    fun `error al cargar ofertas externas`() = runTest {
        val repo = mockk<IDealsRepository>()

        coEvery { repo.obtenerDeals() } throws RuntimeException("API caída")

        val logic = FakeDealsLogic(repo)

        logic.cargarOfertas()

        // lista vacía en error
        assertEquals(emptyList<Deal>(), logic.ofertas.value)
        // mensaje genérico que pone tu lógica
        assertEquals("No se pudieron cargar las ofertas", logic.error.value)
    }

}
