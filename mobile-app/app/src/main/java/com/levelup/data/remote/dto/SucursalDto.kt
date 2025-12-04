package com.levelup.data.remote.dto

data class SucursalDto(
    val id: Long?,
    val nombre: String,
    val direccion: String,
    val ciudad: String,
    val telefono: String?,
    val horario: String?
)
