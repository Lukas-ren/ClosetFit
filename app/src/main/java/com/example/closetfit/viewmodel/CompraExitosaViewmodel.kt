 package com.example.closetfit.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.closetfit.model.DetallePedido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.random.Random

class CompraExitosaViewModel : ViewModel() {
    private val _detallesPedido = MutableStateFlow<DetallePedido?>(null)
    val detallesPedido: StateFlow<DetallePedido?> = _detallesPedido
    private val _navegarInicio = MutableLiveData<Boolean>()
    val navegarInicio: LiveData<Boolean> = _navegarInicio
    private val _navegarTienda = MutableLiveData<Boolean>()
    val navegarTienda: LiveData<Boolean> = _navegarTienda

    fun setDetallesPedido(detalles: DetallePedido) {
        _detallesPedido.value = detalles
        Log.e("COMPRA_EXITOSA", "🧾 Artículos recibidos = ${detalles.numeroArticulos}")
        val cantidad = detalles.numeroArticulos
    }

    fun navegarAInicio() {
        _navegarInicio.value = true
    }

    fun navegarATienda() {
        _navegarTienda.value = true
    }

    fun navegacionCompletada() {
        _navegarInicio.value = false
        _navegarTienda.value = false
    }
}