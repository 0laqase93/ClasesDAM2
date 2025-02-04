package com.example.practica4

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.practica4.Entity.NoticiaEntity
import com.example.practica4.data.Aplicacion
import com.example.practica4.databinding.ActivityAddNewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AgregarNoticiaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val botonGuardar: Button = binding.btnGuardar
        botonGuardar.setOnClickListener {
            val titulo: String = binding.insertarTitulo.text.toString()
            val descripcion: String = binding.insertarResumen.text.toString()
            val fecha: String = binding.insertarFecha.text.toString()

            val noticia = NoticiaEntity(titulo = titulo, descripcion = descripcion, fecha = fecha)
            guardarDatos(noticia)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            Toast.makeText(this, "Noticia guardada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun guardarDatos(noticiaEntity: NoticiaEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            Aplicacion
                .baseDeDatos
                .noticiaDao()
                .agregarNoticia(noticiaEntity)
        }
    }
}
