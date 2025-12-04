package com.levelup.data

data class Sucursal(
    val id: Long,
    val nombre: String,
    val direccion: String,
    val ciudad: String = "",
    val horario: String,
    val telefono: String
)

