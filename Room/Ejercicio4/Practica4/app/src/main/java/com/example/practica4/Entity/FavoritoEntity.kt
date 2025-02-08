package com.example.practica4.Entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE

@Entity(
    tableName = "FavoritoEntity",
    primaryKeys = ["usuarioId", "noticiaId"],
    foreignKeys = [
        ForeignKey(entity = UsuarioEntity::class, parentColumns = ["id"], childColumns = ["usuarioId"]),
                //onDelete = CASCADE), // Esto borra los favoritos si se borra la noticia
        ForeignKey(entity = NoticiaEntity::class, parentColumns = ["id"], childColumns = ["noticiaId"]),
               // onDelete = CASCADE) // Esto borra los favoritos si se borra la noticia
    ]
)
data class FavoritoEntity(
    val usuarioId: Long,
    val noticiaId: Long
)