package com.example.practica4

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ViewSwitcher
import androidx.appcompat.app.AppCompatActivity
import com.example.practica4.databinding.ActivityLoginBinding

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
        val etCorreo = findViewById<EditText>(R.id.etEmail)
        val etContrasena = findViewById<EditText>(R.id.etPassword)
        val cbRecordar = findViewById<CheckBox>(R.id.cbRememberMe)
        val correoPref = preferencias.getString(getString(R.string.userEmail), null)
        val contrasenaPref = preferencias.getString(getString(R.string.userPassword), null)
        val recordarPref = preferencias.getBoolean(getString(R.string.remember), false)
        val btnIniciarSesion = findViewById<Button>(R.id.btnLogin)
        val tvRegistro = findViewById<TextView>(R.id.tvRegister)

        if (recordarPref) {
            cbRecordar.isChecked = true
        }

        if (correoPref != null && contrasenaPref != null) {
            etCorreo.setText(correoPref)
            etContrasena.setText(contrasenaPref)
        } else {
            Toast.makeText(this, "Usuario no registrado", Toast.LENGTH_SHORT).show()
        }

        tvRegistro.setOnClickListener {
            cambiadorVistas.showNext()
        }

        btnIniciarSesion.setOnClickListener {
            if (correoPref != null && contrasenaPref != null) {
                if (etCorreo.text.toString() == correoPref && etContrasena.text.toString() == contrasenaPref) {
                    if (cbRecordar.isChecked) {
                        with(preferencias.edit()) {
                            putString(getString(R.string.userEmail), etCorreo.text.toString())
                            putString(getString(R.string.userPassword), etContrasena.text.toString())
                            apply()
                        }
                    } else {
                        preferencias.edit().clear().apply()
                    }
                    Toast.makeText(this, "Bienvenido ${etCorreo.text}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Credenciales inválidas", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Usuario no registrado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun LoguearActivity.cargarLogicaRegistro(preferencias: SharedPreferences, cambiadorVistas: ViewSwitcher) {
        val btnRegistroIniciarSesion = findViewById<Button>(R.id.btnRegisterLogin)

        btnRegistroIniciarSesion.setOnClickListener {
            val nombre = findViewById<EditText>(R.id.etRegisterName).text.toString()
            val correo = findViewById<EditText>(R.id.etRegisterEmail).text.toString()
            val contrasena = findViewById<EditText>(R.id.etRegisterPassword).text.toString()
            val nombrePref = preferencias.getString(getString(R.string.userName), null)
            val correoPref = preferencias.getString(getString(R.string.userEmail), null)
            val contrasenaPref = preferencias.getString(getString(R.string.userPassword), null)

            if (nombre.isNotEmpty() && correo.isNotEmpty() && contrasena.isNotEmpty()) {
                if (nombre == nombrePref && correo == correoPref && contrasena == contrasenaPref) {
                    Toast.makeText(this, "Usuario duplicado", Toast.LENGTH_SHORT).show()
                } else {
                    with(preferencias.edit()) {
                        putString(getString(R.string.userName), nombre)
                        putString(getString(R.string.userEmail), correo)
                        putString(getString(R.string.userPassword), contrasena)
                        putBoolean(getString(R.string.remember), true)
                        apply()
                    }
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
}
