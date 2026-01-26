package com.example.closetfit.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.closetfit.interfaces.DAOUsuario
import com.example.closetfit.interfaces.ProductoDao
import com.example.closetfit.model.Producto
import com.example.closetfit.model.Usuario
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.InputStreamReader

@Database(entities = [Usuario::class, Producto::class], version = 2) // Incremented version
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): DAOUsuario
    abstract fun productoDao(): ProductoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                val productDao = getDatabase(context).productoDao()
                                if (productDao.count() == 0) { // Check if the table is empty
                                    val products = loadProductsFromAsset(context)
                                    productDao.insertAll(products)
                                }
                            }
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private fun loadProductsFromAsset(context: Context): List<Producto> {
            val inputStream = context.assets.open("productos.json")
            val reader = InputStreamReader(inputStream)
            val productType = object : TypeToken<List<Producto>>() {}.type
            return Gson().fromJson(reader, productType)
        }
    }
}