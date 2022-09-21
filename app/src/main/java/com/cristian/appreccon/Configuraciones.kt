package com.cristian.appreccon

import android.content.Context
import android.content.Intent
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

        val admin = RecconDataBase(this, "Reccon", null, 1)
        val bd = admin.writableDatabase
        val sin = bd.rawQuery(
            "select * from configuracion where alimentacion = 'Sin Alimentacion'",
            null
        )
        if (sin.moveToFirst()){
            binding.sina.text = sin.getDouble(1).toString()
        }
        val con = bd.rawQuery(
            "select * from configuracion where alimentacion = 'Sin Alimentacion'",
            null
        )
        if (con.moveToFirst()){
            binding.cona.text = con.getDouble(1).toString()
        }
        binding.btactualizar1.setOnClickListener{
            if (TextUtils.isEmpty(binding.sin.text.toString())){
                binding.sin.error = "este campo es obligatorio"
                binding.sin.requestFocus()
            }else{
                val preferencias = getSharedPreferences( "registrar", Context.MODE_PRIVATE)
                val editor = preferencias.edit()
                editor.putString("sin_alimentacion",binding.sin.text.toString())
                editor.apply()


                startActivity(Intent(this,Configuraciones::class.java))
            }
        }
        binding.btactualizar2.setOnClickListener{
            if (TextUtils.isEmpty(binding.con.text.toString())){
                binding.con.error = "este campo es obligatorio"
                binding.con.requestFocus()
            }else{
                val preferencias = getSharedPreferences( "registrar", Context.MODE_PRIVATE)
                val editor = preferencias.edit()
                editor.putString("con_alimentacion",binding.con.text.toString())
                editor.apply()
                startActivity(Intent(this,Configuraciones::class.java))
            }
        }

    }
}