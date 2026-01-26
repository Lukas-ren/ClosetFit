package com.example.closetfit.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.closetfit.repository.AppDatabase
import kotlinx.coroutines.flow.Flow
import com.example.closetfit.model.Producto

class ProductoViewModel(application: Application) : AndroidViewModel(application) {
    private val productoDao = AppDatabase.getDatabase(application).productoDao()

    val allProducts: Flow<List<Producto>> = productoDao.getAll()
}
