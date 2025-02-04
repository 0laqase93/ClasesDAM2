package com.example.practica4

import com.example.practica4.Entity.NoticiaEntity

interface OnClickListener {
    fun alHacerClic(noticiaEntity: NoticiaEntity)

    fun alDarleAFavorito(noticiaEntity: NoticiaEntity)

    fun alEliminar(noticiaEntity: NoticiaEntity)
}
