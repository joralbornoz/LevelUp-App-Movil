package com.levelup.data

data class Productos(
    val codigo: String,
    val nombre: String,
    val precio: Int,
    val descripcion: String,
    val stock: Int
)

val DataProductos = listOf(
    Productos(" ", " ", 0, " ", 0)
)
