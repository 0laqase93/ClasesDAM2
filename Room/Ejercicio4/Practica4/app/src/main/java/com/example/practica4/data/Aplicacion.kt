package com.example.practica4.data

import android.app.Application
import androidx.room.Room

class Aplicacion: Application() {
    companion object {
        lateinit var baseDeDatos: BaseDeDatos
    }

    override fun onCreate() {
        super.onCreate()

        baseDeDatos = Room.databaseBuilder(
            this,
            BaseDeDatos::class.java,
            "BaseDeDatos")
            .fallbackToDestructiveMigration()
            .build()
    }
}