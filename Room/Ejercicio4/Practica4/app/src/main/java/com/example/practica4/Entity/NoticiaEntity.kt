package com.example.practica4.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "NoticiaEntity")
data class NoticiaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var titulo: String = "",
    var descripcion: String = "",
    var fecha: String = "",
    var esFavorita: Boolean = false,
    val imagenUrl: String = "",
    val noticiaUrl: String = ""
): Serializable // El Serializable es para poder pasarlo entre activitys
