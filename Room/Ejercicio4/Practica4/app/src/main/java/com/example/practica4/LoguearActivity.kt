package com.example.practica4

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Toast
import android.widget.ViewSwitcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.practica4.Entity.UsuarioEntity
import com.example.practica4.data.Aplicacion
import com.example.practica4.databinding.ActivityLoginBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoguearActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cambiadorVistas: ViewSwitcher = binding.viewSwitcher
        val preferencias: SharedPreferences = getPreferences(Context.MODE_PRIVATE)

        cargarLogicaInicioSesion(preferencias, cambiadorVistas)
        cargarLogicaRegistro(preferencias, cambiadorVistas)

        cargarAnimacionTransicion(cambiadorVistas)
    }

    private fun LoguearActivity.cargarLogicaInicioSesion(preferencias: SharedPreferences, cambiadorVistas: ViewSwitcher) {
        val etEmail = binding.etEmail
        val etContrasena = binding.etPassword
        val cbRecordar = binding.cbRememberMe
        val emailPref = preferencias.getString(getString(R.string.userEmail), null)
        val contrasenaPref = preferencias.getString(getString(R.string.userPassword), null)
        val recordarPref = preferencias.getBoolean(getString(R.string.remember), false)
        val btnIniciarSesion = binding.btnLogin
        val tvRegistro = binding.tvRegister

        if (recordarPref) {
            cbRecordar.isChecked = true
        }

        if (emailPref != null && contrasenaPref != null) {
            etEmail.setText(emailPref)
            etContrasena.setText(contrasenaPref)
        } else {
            Toast.makeText(this, "Usuario no registrado", Toast.LENGTH_SHORT).show()
        }

        tvRegistro.setOnClickListener {
            cambiadorVistas.showNext()
        }

        btnIniciarSesion.setOnClickListener {
            if (emailPref != null && contrasenaPref != null) {
                if (etEmail.text.toString() == emailPref && etContrasena.text.toString() == contrasenaPref) {
                    if (cbRecordar.isChecked) {
                        with(preferencias.edit()) {
                            putString(getString(R.string.userEmail), etEmail.text.toString())
                            putString(getString(R.string.userPassword), etContrasena.text.toString())
                            apply()
                        }
                    } else {
                        preferencias.edit().clear().apply()
                    }

                    lifecycleScope.launch {
                        val usuario = leerUsuario(email = etEmail.text.toString(), contrasena = etContrasena.text.toString())
                        if (usuario != null) {
                            Toast.makeText(this@LoguearActivity, "Bienvenido ${etEmail.text}", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@LoguearActivity, MainActivity::class.java)
                            intent.putExtra("Usuario", usuario)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@LoguearActivity, "Usuario no registrado", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Usuario no registrado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun LoguearActivity.cargarLogicaRegistro(preferencias: SharedPreferences, cambiadorVistas: ViewSwitcher) {
        val btnRegistroIniciarSesion = binding.btnRegisterLogin

        btnRegistroIniciarSesion.setOnClickListener {
            val nombre = binding.etRegisterName.text.toString()
            val email = binding.etRegisterEmail.text.toString()
            val contrasena = binding.etRegisterPassword.text.toString()
            val nombrePref = preferencias.getString(getString(R.string.userName), null)
            val emailPref = preferencias.getString(getString(R.string.userEmail), null)
            val contrasenaPref = preferencias.getString(getString(R.string.userPassword), null)

            if (nombre.isNotEmpty() && email.isNotEmpty() && contrasena.isNotEmpty()) {
                if (nombre == nombrePref && email == emailPref && contrasena == contrasenaPref) {
                    Toast.makeText(this, "Usuario duplicado", Toast.LENGTH_SHORT).show()
                } else {
                    with(preferencias.edit()) {
                        putString(getString(R.string.userName), nombre)
                        putString(getString(R.string.userEmail), email)
                        putString(getString(R.string.userPassword), contrasena)
                        putBoolean(getString(R.string.remember), true)
                        apply()
                    }
                    guardarUsuario(email = email, nombre = nombre, contrasena = contrasena)
                    cargarLogicaInicioSesion(preferencias, cambiadorVistas)
                    cambiadorVistas.showPrevious()
                }
            } else {
                Toast.makeText(this, "Datos inválidos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun LoguearActivity.cargarAnimacionTransicion(cambiadorVistas: ViewSwitcher) {
        val animacionEntrada = AnimationUtils.loadAnimation(this, R.anim.slide_in_right)
        val animacionSalida = AnimationUtils.loadAnimation(this, R.anim.slide_out_left)
        cambiadorVistas.inAnimation = animacionEntrada
        cambiadorVistas.outAnimation = animacionSalida
    }

    private fun guardarUsuario(email: String, nombre: String, contrasena: String) {
        val nuevoUsuario = UsuarioEntity(email = email, nombre = nombre, contrasena = contrasena)
        lifecycleScope.launch(Dispatchers.IO) {
            Aplicacion
                .baseDeDatos
                .usuarioDao()
                .agregarUsuario(nuevoUsuario)
        }
    }

    private suspend fun leerUsuario(email: String, contrasena: String): UsuarioEntity? {
        return withContext(Dispatchers.IO) {
            Aplicacion.baseDeDatos.usuarioDao().obtenerUsuario(email, contrasena)
        }
    }
}
