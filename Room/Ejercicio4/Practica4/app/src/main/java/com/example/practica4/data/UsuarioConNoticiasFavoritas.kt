package com.example.practica4.data

import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.Junction
import com.example.practica4.Entity.FavoritoEntity
import com.example.practica4.Entity.NoticiaEntity
import com.example.practica4.Entity.UsuarioEntity

data class UsuarioConNoticiasFavoritas(
    @Embedded val usuario: UsuarioEntity,
    @Relation(
        parentColumn = "id", // Clave primaria de UsuarioEntity
        entityColumn = "id", // Clave primaria de NoticiaEntity
        associateBy = Junction(
            value = FavoritoEntity::class,
            parentColumn = "usuarioId", // Campo en FavoritoEntity que referencia a UsuarioEntity
            entityColumn = "noticiaId"  // Campo en FavoritoEntity que referencia a NoticiaEntity
        )
    )
    val noticiasFavoritas: List<NoticiaEntity>
)

