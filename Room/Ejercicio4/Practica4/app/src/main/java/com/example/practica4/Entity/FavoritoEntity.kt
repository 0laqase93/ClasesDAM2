package com.example.practica4.Entity

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "FavoritoEntity",
    primaryKeys = ["usuarioId", "noticiaId"],
    foreignKeys = [
        ForeignKey(entity = UsuarioEntity::class, parentColumns = ["id"], childColumns = ["usuarioId"]),
        ForeignKey(entity = NoticiaEntity::class, parentColumns = ["id"], childColumns = ["noticiaId"])
    ]
)
data class FavoritoEntity(
    val usuarioId: Int,
    val noticiaId: Int
)