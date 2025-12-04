package com.levelup.data.remote.dto

data class ProductoDto(
    val codigo: String,
    val nombre: String,
    val precio: Int,
    val descripcion: String?,
    val stock: Int
)
