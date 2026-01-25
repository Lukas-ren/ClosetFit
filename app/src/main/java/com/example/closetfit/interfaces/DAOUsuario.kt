package com.example.closetfit.interfaces

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.closetfit.model.Usuario
import kotlinx.coroutines.flow.Flow

@Dao
interface DAOUsuario {

    @Insert
    suspend fun insert(usuario: Usuario)


    @Query("SELECT * FROM usuarios WHERE nombre = :username and password = :password")
    suspend fun login(username: String, password: String): Usuario?

    @Query("delete from usuarios where nombre = :username")
    suspend fun deleteUser(username:String)

    @Query("select * from usuarios")
    fun obtenerUsuarios(): Flow<List<Usuario>>

    @Query("update usuarios set email = :correo, password = :password where nombre = :username")
    suspend fun updateUsuario(username:String, correo:String, password:String)


}