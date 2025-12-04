package com.levelup.data.remote.dto

data class UsuarioDto(
    val id: Long?,
    val nombre: String,
    val email: String,
    val edad: Int?,
    val preferencias: String?,
    val direccion: String?,
    val rol: String?,
    val password: String
)
