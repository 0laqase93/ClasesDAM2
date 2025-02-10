package com.example.practica4

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practica4.Entity.FavoritoEntity
import com.example.practica4.Entity.NoticiaEntity
import com.example.practica4.Entity.UsuarioEntity
import com.example.practica4.data.Aplicacion
import com.example.practica4.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityMainBinding

    private lateinit var layoutLineal: LinearLayoutManager
    private lateinit var adaptadorNoticias: AdaptadorNoticias

    private  var usuario: UsuarioEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        // Forzamos el modo claro para toda la aplicación.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        usuario = intent.getSerializableExtra("Usuario") as? UsuarioEntity

        val botonFlotante = binding.addNew
        botonFlotante.setOnClickListener {
            Toast.makeText(this, "Creando noticia", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AgregarNoticiaActivity::class.java)
            intent.putExtra("Usuario", usuario)
            startActivity(intent)
        }

        configurarRecyclerView()
    }

    private fun configurarRecyclerView() {
        adaptadorNoticias = AdaptadorNoticias(mutableListOf(), this)
        layoutLineal = LinearLayoutManager(this)

        obtenerNoticias()

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = layoutLineal
            adapter = adaptadorNoticias
        }
    }

    private fun obtenerNoticias() {
        lifecycleScope.launch(Dispatchers.IO) {
            val noticias = Aplicacion
                .baseDeDatos
                .noticiaDao()
                .obtenerTodasLasNoticias()

            val favoritos = usuario?.let {
                Aplicacion
                    .baseDeDatos
                    .favoritoDao()
                    .obtenerTodosLosFavoritos(it.id)
            }

            noticias.forEach { noticia ->
                if (favoritos != null) {
                    noticia.esFavorita = favoritos.any { it.noticiaId == noticia.id }
                }
            }

            withContext(Dispatchers.Main) {
                adaptadorNoticias.establecerNoticias(noticias)
            }
        }
    }

    override fun alHacerClic(noticiaEntity: NoticiaEntity) {
        val intent = Intent(this, ActualizarNoticiaActivity::class.java)
        intent.putExtra("Noticia", noticiaEntity)
        intent.putExtra("Usuario", usuario)
        startActivity(intent)
    }

    override fun alDarleAFavorito(noticiaEntity: NoticiaEntity) {
        noticiaEntity.esFavorita = !noticiaEntity.esFavorita
        adaptadorNoticias.actualizar(noticiaEntity)
        lifecycleScope.launch(Dispatchers.IO) {
            // Esto asignará -1 en caso de que usuario sea null. (No debería)
            val favoritoEntity = FavoritoEntity(usuario?.id ?: -1, noticiaEntity.id)
            if (noticiaEntity.esFavorita) {
                Aplicacion
                    .baseDeDatos
                    .favoritoDao()
                    .agregarFavorito(favoritoEntity)
            } else {
                Aplicacion
                    .baseDeDatos
                    .favoritoDao()
                    .borrarFavorito(favoritoEntity)
            }
            Aplicacion
                .baseDeDatos
                .noticiaDao()
                .actualizarNoticia(noticiaEntity)
        }
    }

    override fun alEliminar(noticiaEntity: NoticiaEntity) {
        adaptadorNoticias.eliminar(noticiaEntity)
        lifecycleScope.launch(Dispatchers.IO) {
            Aplicacion
                .baseDeDatos
                .noticiaDao()
                .borrarNoticia(noticiaEntity)
        }
    }
}
