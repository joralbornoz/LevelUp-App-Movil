package com.levelup.data.model

data class Usuario(
    val id: Long,
    val nombre: String,
    val email: String,
    val edad: Int? = null,
    val preferencias: String? = null,
    val direccion: String? = null,
    val rol: String? = null,
    val password: String
)