package com.example.closetfit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CompraRechazadaViewModel : ViewModel() {
    private val _motivoRechazo = MutableLiveData<String>()
    val motivoRechazo: LiveData<String> = _motivoRechazo

    fun setMotivo(motivo: String) {
        _motivoRechazo.value = motivo
    }
}