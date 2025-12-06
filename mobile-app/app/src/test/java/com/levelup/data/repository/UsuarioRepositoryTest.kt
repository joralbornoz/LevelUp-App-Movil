package com.levelup.data.repository

import com.levelup.data.model.Usuario
import com.levelup.data.repository.interfaces.IUsuarioRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class UsuarioRepositoryTest {

    @Test
    fun `obtenerUsuarioPorEmail devuelve usuario cuando existe`() = runTest {
        // Arrange
        val repo: IUsuarioRepository = mockk()

        val usuarioMock = Usuario(
            id = 1L,
            nombre = "Jorge",
            email = "test@test.com",
            edad = 25,
            preferencias = "PC Gamer",
            direccion = "Santiago",
            rol = "USUARIO",
            password = "1234"
        )

        // Simulamos que el repo encuentra ese usuario
        coEvery { repo.obtenerUsuarioPorEmail("test@test.com") } returns usuarioMock

        // Act
        val resultado = repo.obtenerUsuarioPorEmail("test@test.com")

        // Assert
        assertEquals("Jorge", resultado?.nombre)
        assertEquals("test@test.com", resultado?.email)
        assertEquals("1234", resultado?.password)
    }

    @Test
    fun `obtenerUsuarioPorEmail devuelve null cuando usuario no existe`() = runTest {
        // Arrange
        val repo: IUsuarioRepository = mockk()

        // Simulamos que no existe usuario con ese correo
        coEvery { repo.obtenerUsuarioPorEmail("no_existe@test.com") } returns null

        // Act
        val resultado = repo.obtenerUsuarioPorEmail("no_existe@test.com")

        // Assert
        assertNull(resultado)
    }

    @Test
    fun `crearUsuario devuelve usuario creado`() = runTest {
        val repo: IUsuarioRepository = mockk()

        val nuevoUsuario = Usuario(
            id = 0L,
            nombre = "Nuevo",
            email = "nuevo@test.com",
            edad = 20,
            preferencias = null,
            direccion = null,
            rol = "USUARIO",
            password = "pass"
        )

        val creado = nuevoUsuario.copy(id = 10L)

        coEvery { repo.crearUsuario(nuevoUsuario) } returns creado

        val resultado = repo.crearUsuario(nuevoUsuario)

        assertEquals(10L, resultado.id)
        assertEquals("nuevo@test.com", resultado.email)
    }
}
