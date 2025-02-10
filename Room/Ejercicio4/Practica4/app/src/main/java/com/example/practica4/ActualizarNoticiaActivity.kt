package com.example.practica4

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.example.practica4.Entity.NoticiaEntity
import com.example.practica4.Entity.UsuarioEntity
import com.example.practica4.data.Aplicacion
import com.example.practica4.databinding.ActivityActualizarNoticiaBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ActualizarNoticiaActivity : AppCompatActivity() {
    private lateinit var binding: ActivityActualizarNoticiaBinding

    private  var usuario: UsuarioEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // Forzamos el modo claro para toda la aplicación.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)

        usuario = intent.getSerializableExtra("Usuario") as? UsuarioEntity

        binding = ActivityActualizarNoticiaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val noticiaObtenida = intent.getSerializableExtra("Noticia") as NoticiaEntity

        val titulo: String = noticiaObtenida.titulo
        val descripcion: String = noticiaObtenida.descripcion
        val fecha: String = noticiaObtenida.fecha

        val et_titulo = binding.insertarTitulo
        val et_descripcion = binding.insertarResumen
        val et_fecha = binding.insertarFecha
        val btn_actualizar = binding.btnGuardar

        et_titulo.setText(titulo)
        et_descripcion.setText(descripcion)
        et_fecha.setText(fecha)

        btn_actualizar.setOnClickListener {
            val id = noticiaObtenida.id
            val esFavorita = noticiaObtenida.esFavorita
            val imagenUrl = noticiaObtenida.imagenUrl
            val noticiaUrl = noticiaObtenida.noticiaUrl
            val noticiaActualizada = NoticiaEntity(
                id = id,
                titulo = et_titulo.text.toString(),
                descripcion = et_descripcion.text.toString(),
                fecha = et_fecha.text.toString(),
                esFavorita = esFavorita,
                imagenUrl = imagenUrl,
                noticiaUrl = noticiaUrl)
            guardarNoticia(noticiaActualizada)
            intent = Intent(this, MainActivity::class.java)
            intent.putExtra("Usuario", usuario)
            startActivity(intent)
        }
    }

    private fun guardarNoticia(noticiaActualizada: NoticiaEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            Aplicacion
                .baseDeDatos
                .noticiaDao()
                .actualizarNoticia(noticiaActualizada)
        }
    }
}