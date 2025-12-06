package com.levelup.viewmodel

import com.levelup.data.model.Usuario
import com.levelup.data.repository.interfaces.IUsuarioRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test
import android.app.Application
import com.levelup.ui.viewmodel.AppViewModel
import kotlinx.coroutines.flow.MutableStateFlow


class AppViewModelLoginTest {

    @Test
    fun `login exitoso actualiza usuarioBackend`() = runTest {
        val repo = mockk<IUsuarioRepository>()

        val usuarioMock = Usuario(
            id = 1L,
            nombre = "Jorge",
            email = "test@test.com",
            edad = 25,
            preferencias = null,
            direccion = null,
            rol = "USUARIO",
            password = "1234"
        )

        coEvery { repo.obtenerUsuarioPorEmail("test@test.com") } returns usuarioMock

        val logic = FakeLoginLogic(repo)

        logic.login("test@test.com", "1234")

        assertEquals("Jorge", logic.usuarioBackend.value?.nombre)
        assertNull(logic.error.value)
    }

    @Test
    fun `login falla cuando usuario no existe`() = runTest {
        val repo = mockk<IUsuarioRepository>()
        coEvery { repo.obtenerUsuarioPorEmail("no@test.com") } returns null

        val logic = FakeLoginLogic(repo)

        logic.login("no@test.com", "1234")

        assertNull(logic.usuarioBackend.value)
        assertEquals("Usuario no encontrado", logic.error.value)
    }

    @Test
    fun `login falla por contrasena incorrecta`() = runTest {
        val repo = mockk<IUsuarioRepository>()

        val usuarioMock = Usuario(
            id = 1L,
            nombre = "Jorge",
            email = "test@test.com",
            edad = 25,
            preferencias = null,
            direccion = null,
            rol = "USUARIO",
            password = "1234"
        )

        coEvery { repo.obtenerUsuarioPorEmail("test@test.com") } returns usuarioMock

        val logic = FakeLoginLogic(repo)

        logic.login("test@test.com", "0000")

        assertNull(logic.usuarioBackend.value)
        assertEquals("Contraseña incorrecta", logic.error.value)
    }
    @Test
    fun `login falla cuando email esta vacio`() = runTest {
        val repo = mockk<IUsuarioRepository>()  // no hace falta configurar nada

        val logic = FakeLoginLogic(repo)

        logic.login("", "1234")

        assertNull(logic.usuarioBackend.value)
        assertEquals("Email requerido", logic.error.value)
    }
}
