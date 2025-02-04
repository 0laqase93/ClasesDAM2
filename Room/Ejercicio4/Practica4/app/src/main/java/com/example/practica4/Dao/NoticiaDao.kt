package com.example.practica4.Dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.practica4.Entity.NoticiaEntity

@Dao
interface NoticiaDao {
    @Query("SELECT * FROM NoticiaEntity")
    suspend fun obtenerTodasLasNoticias(): MutableList<NoticiaEntity>
    @Insert
    suspend fun agregarNoticia(noticiaEntity: NoticiaEntity)
    @Update
    suspend fun actualizarNoticia(noticiaEntity: NoticiaEntity)
    @Delete
    suspend fun borrarNoticia(noticiaEntity: NoticiaEntity)
}
