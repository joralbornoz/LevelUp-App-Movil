package com.levelup.data.repository.interfaces

import com.levelup.data.model.Usuario

interface IUsuarioRepository {
    suspend fun obtenerUsuarioPorEmail(email: String): Usuario?
    suspend fun crearUsuario(usuario: Usuario): Usuario
    suspend fun actualizarUsuario(id: Long, usuario: Usuario): Usuario
}
