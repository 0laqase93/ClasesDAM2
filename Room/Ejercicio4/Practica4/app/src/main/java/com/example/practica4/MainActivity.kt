package com.example.practica4

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val preferencias: SharedPreferences = getPreferences(Context.MODE_PRIVATE)
        usuario = intent.getSerializableExtra("usuario") as? UsuarioEntity
        // Si no ha encotrado el usuario lo crea a través de shared preferences
        if (usuario == null) {
            val id = preferencias.getLong("IdUsuarioMain", -1)
            val email = preferencias.getString("CorreoUsuarioMain", null)
            val nombre = preferencias.getString("NombreUsuarioMain", null)
            val contrasena = preferencias.getString("ContrasenaUsuarioMain", null)
            usuario = UsuarioEntity(id = id, email = email, nombre = nombre, contrasena = contrasena)
        }

        val botonFlotante = binding.addNew
        botonFlotante.setOnClickListener {
            Toast.makeText(this, "Creando noticia", Toast.LENGTH_SHORT).show()
            // Guardar usuario en shared preference para no perderlo al cambiar de activity
            with(preferencias.edit()) {
                // El !! es porque el preferences no acepta datos que puedan ser nulos
                putLong("IdUsuarioMain", usuario!!.id)
                putString("CorreoUsuarioMain", usuario!!.email)
                putString("NombreUsuarioMain", usuario!!.nombre)
                putString("ContrasenaUsuarioMain", usuario!!.contrasena)
            }.apply()
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
            val noticias = Aplicacion
                .baseDeDatos
                .noticiaDao()
                .obtenerTodasLasNoticias()
            adaptadorNoticias.establecerNoticias(noticias)
        }
    }

    override fun alHacerClic(noticiaEntity: NoticiaEntity) {
        val intent = Intent(this, ActualizarNoticiaActivity::class.java)
        intent.putExtra("Noticia", noticiaEntity)
        startActivity(intent)
    }

    override fun alDarleAFavorito(noticiaEntity: NoticiaEntity) {
        noticiaEntity.esFavorita = !noticiaEntity.esFavorita
        adaptadorNoticias.actualizar(noticiaEntity)
        lifecycleScope.launch(Dispatchers.IO) {
            // Esto asignará 0L en caso de que usuario sea null.
            val favoritoEntity = FavoritoEntity(usuario?.id ?: 0L, noticiaEntity.id)
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
