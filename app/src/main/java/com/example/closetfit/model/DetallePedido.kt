package com.example.closetfit.model

data class DetallePedido(
    val idPedido: String="",
    val totalCompra: Int=0,
    val numeroArticulos: Int=0,
    val direccionEnvio: String="",
    val metodoPago: String=""
)