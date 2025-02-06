package com.example.practica4.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "UsuarioEntity")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var email: String? = "",
    var nombre: String? = "",
    var contrasena: String? = ""
) : Serializable // El Serializable es para poder pasarlo entre activitys
