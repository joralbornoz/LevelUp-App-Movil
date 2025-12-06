package com.levelup.viewmodel

import com.levelup.data.model.Usuario
import com.levelup.data.repository.interfaces.IUsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeLoginLogic(
    private val usuarioRepo: IUsuarioRepository
) {

    private val _usuarioBackend = MutableStateFlow<Usuario?>(null)
    val usuarioBackend: StateFlow<Usuario?> = _usuarioBackend

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // OJO: es suspend, NO usa viewModelScope ni Dispatchers.Main
    suspend fun login(email: String, password: String) {
        if (email.isBlank()) {
            _usuarioBackend.value = null
            _error.value = "Email requerido"
            return
        }

        val user = usuarioRepo.obtenerUsuarioPorEmail(email)

        if (user == null) {
            _usuarioBackend.value = null
            _error.value = "Usuario no encontrado"
        } else if (user.password == password) {
            _usuarioBackend.value = user
            _error.value = null
        } else {
            _usuarioBackend.value = null
            _error.value = "Contraseña incorrecta"
        }
    }

}
