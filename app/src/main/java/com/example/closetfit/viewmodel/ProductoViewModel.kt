package com.example.closetfit.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.closetfit.repository.AppDatabase
import kotlinx.coroutines.flow.Flow
import com.example.closetfit.model.Producto
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductoViewModel : ViewModel() {

    private val _allProductos = MutableStateFlow<List<Producto>>(emptyList())
    val allProductos: StateFlow<List<Producto>> = _allProductos
    fun cargarProductos(context: Context) {
        viewModelScope.launch {
            try {
                val json = context.assets.open("productos.json")
                    .bufferedReader()
                    .use { it.readText() }

                val gson = Gson()
                val productos = gson.fromJson(
                    json,
                    Array<Producto>::class.java
                ).toList()

                _allProductos.value = productos

            } catch (e: Exception) {
                e.printStackTrace()
                _allProductos.value = emptyList()
            }
        }
    }
}


