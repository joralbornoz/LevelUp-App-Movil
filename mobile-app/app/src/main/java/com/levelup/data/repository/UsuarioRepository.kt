package com.levelup.data.repository

import com.levelup.data.model.Usuario
import com.levelup.data.remote.RetrofitClient
import com.levelup.data.remote.dto.UsuarioDto

class UsuarioRepository {

    private val api = RetrofitClient.usuarioApi

    private fun mapDtoToUi(dto: UsuarioDto): Usuario {
        return Usuario(
            id = dto.id ?: 0L,
            nombre = dto.nombre,
            email = dto.email,
            edad = dto.edad,
            preferencias = dto.preferencias,
            direccion = dto.direccion,
            rol = dto.rol,
            password = dto.password ?: ""
        )
    }

    private fun mapUiToDto(u: Usuario): UsuarioDto {
        return UsuarioDto(
            id = u.id,
            nombre = u.nombre,
            email = u.email,
            edad = u.edad,
            preferencias = u.preferencias,
            direccion = u.direccion,
            rol = u.rol,
            password = u.password
        )
    }

    suspend fun obtenerUsuarioPorEmail(email: String): Usuario? {
        val dto = api.getUsuarioPorEmail(email)
        return mapDtoToUi(dto)
    }

    suspend fun crearUsuario(usuario: Usuario): Usuario {
        val dto = mapUiToDto(usuario.copy(id = 0L))
        val creado = api.crearUsuario(dto)
        return mapDtoToUi(creado)
    }

    // 👇 NUEVO: actualizar en backend
    suspend fun actualizarUsuario(id: Long, usuario: Usuario): Usuario {
        val dto = mapUiToDto(usuario)
        val actualizado = api.actualizarUsuario(id, dto)
        return mapDtoToUi(actualizado)
    }
    // ✅ ADMIN: listar usuarios
    suspend fun obtenerUsuarios(): List<Usuario> {
        return api.obtenerUsuarios().map { mapDtoToUi(it) }
    }

    // ✅ ADMIN: eliminar usuario (opcional, si tendrás botón eliminar)
    suspend fun eliminarUsuario(id: Long) {
        api.eliminarUsuario(id)
    }

}
