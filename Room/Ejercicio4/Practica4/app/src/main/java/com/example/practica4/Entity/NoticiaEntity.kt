package com.example.practica4.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "NoticiaEntity")
data class NoticiaEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var titulo: String = "",
    var descripcion: String = "",
    var fecha: String = "",
    var esFavorita: Boolean = false
)
