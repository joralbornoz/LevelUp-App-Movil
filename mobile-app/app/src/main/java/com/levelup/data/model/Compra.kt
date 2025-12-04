package com.levelup.data

data class Compra(
    val id: Long,
    val emailUsuario: String,
    val fecha: String,
    val total: Int,
    val detalle: String
)
