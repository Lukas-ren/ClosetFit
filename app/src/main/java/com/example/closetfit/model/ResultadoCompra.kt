package com.example.closetfit.model

sealed class ResultadoCompra {

    object Exitosa : ResultadoCompra()

    data class Rechazada(
        val motivo: String
    ) : ResultadoCompra()
}