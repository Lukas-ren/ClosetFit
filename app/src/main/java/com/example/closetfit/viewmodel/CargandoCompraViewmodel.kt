package com.example.closetfit.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.closetfit.model.ResultadoCompra
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CargandoCompraViewModel : ViewModel() {

    private val _estadoCompra = MutableStateFlow<ResultadoCompra?>(null)
    val estadoCompra: StateFlow<ResultadoCompra?> = _estadoCompra
    private var procesado = false
    fun procesarCompra(carritoVacio: Boolean) {
        if (procesado) return
        procesado = true
        viewModelScope.launch {
            delay(4000)

            _estadoCompra.value =
                if (carritoVacio) {
                    ResultadoCompra.Rechazada("El carrito está vacío")
                } else {
                    ResultadoCompra.Exitosa
                }
        }
    }
    fun limpiarEstado() {
        _estadoCompra.value = null
        procesado = false
    }
}


