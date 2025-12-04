package com.levelup.data.remote.dto

data class CompraDto(
    val id: Long?,
    val emailUsuario: String,
    val fecha: String?,
    val total: Int,
    val detalle: String
)
