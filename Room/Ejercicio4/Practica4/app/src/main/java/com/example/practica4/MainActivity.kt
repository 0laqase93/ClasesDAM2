package com.example.practica4

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practica4.Entity.NoticiaEntity
import com.example.practica4.data.Aplicacion
import com.example.practica4.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(), OnClickListener {
    private lateinit var binding: ActivityMainBinding

    private lateinit var layoutLineal: LinearLayoutManager
    private lateinit var adaptadorNoticias: AdaptadorNoticias

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val botonFlotante = binding.addNew
        botonFlotante.setOnClickListener {
            Toast.makeText(this, "Creando noticia", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AgregarNoticiaActivity::class.java)
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
            val noticias = Aplicacion.baseDeDatos.noticiaDao().obtenerTodasLasNoticias()

            withContext(Dispatchers.Main) {
                adaptadorNoticias.establecerNoticias(noticias)
            }
        }
    }

    override fun alHacerClic(noticiaEntity: NoticiaEntity) {

    }

    override fun alDarleAFavorito(noticiaEntity: NoticiaEntity) {
        noticiaEntity.esFavorita = !noticiaEntity.esFavorita
    }

    override fun alEliminar(noticiaEntity: NoticiaEntity) {
        adaptadorNoticias.eliminar(noticiaEntity)
        lifecycleScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                Aplicacion.baseDeDatos.noticiaDao().borrarNoticia(noticiaEntity)
            }
        }
    }
}
