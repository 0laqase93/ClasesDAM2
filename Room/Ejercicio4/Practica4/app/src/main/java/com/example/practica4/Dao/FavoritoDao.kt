package com.example.practica4.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.practica4.Entity.FavoritoEntity

@Dao
interface FavoritoDao {
    @Query("SELECT * FROM FavoritoEntity WHERE usuarioId = :userId")
    suspend fun obtenerTodosLosFavoritos(userId: Int): MutableList<FavoritoEntity>
    @Insert
    suspend fun agregarFavorito(favoritoEntity: FavoritoEntity)
    @Update
    suspend fun actualizarFavorito(favoritoEntity: FavoritoEntity)
    @Delete
    suspend fun borrarFavorito(favoritoEntity: FavoritoEntity)
}