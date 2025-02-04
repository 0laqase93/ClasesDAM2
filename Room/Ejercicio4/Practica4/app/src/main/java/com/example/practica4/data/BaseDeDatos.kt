package com.example.practica4.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.practica4.Dao.*
import com.example.practica4.Entity.*

@Database(entities = [NoticiaEntity::class, UsuarioEntity::class, FavoritoEntity::class], version = 1)
abstract class BaseDeDatos: RoomDatabase() {
    abstract fun noticiaDao(): NoticiaDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun favoritoDao(): FavoritoDao
}
