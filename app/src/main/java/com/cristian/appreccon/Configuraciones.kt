package com.cristian.appreccon

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.cristian.appreccon.databinding.ActivityConfiguracionesBinding

class Configuraciones : AppCompatActivity() {
    lateinit var binding: ActivityConfiguracionesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityConfiguracionesBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val preferencias = getSharedPreferences( "registrar", Context.MODE_PRIVATE)
        val C = preferencias .getString("con_alimentacion","")
        val N = preferencias .getString("sin_alimentacion","")

        binding.cona.text = "$"+C
        binding.sina.text = "$"+N

    }
}