package com.cristian.appreccon

import android.content.ContentValues
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

        title = "Configuracion"

        val admin = RecconDataBase(this, "Reccon", null, 1)
        val bd = admin.writableDatabase
        val registro = ContentValues()
        val sin = bd.rawQuery(
            "select * from configuracion where alimentacion = 'Sin Alimentacion' and estado ='Activo'",
            null
        )
        if (sin.moveToFirst()){
            binding.sina.text = sin.getDouble(2).toString()

        }
        val con = bd.rawQuery(
            "select * from configuracion where alimentacion = 'Con Alimentacion' and estado ='Activo'",
            null
        )
        if (con.moveToFirst()){
            binding.cona.text = con.getDouble(2).toString()

        }
        binding.btactualizar1.setOnClickListener{
            if (TextUtils.isEmpty(binding.sin.text.toString())){
                binding.sin.error = "este campo es obligatorio"
                binding.sin.requestFocus()
            }else{
                val id_sinAlimentacion = sin.getInt(0)
                registro.put("estado","Antiguo")
                val cant = bd.update("configuracion", registro, "id_configuracion=${id_sinAlimentacion}", null)
                if (cant == 1){
                    Toast.makeText(this, "se Actualizo el precio", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,Configuraciones::class.java))
                    finish()
                    registro.put("alimentacion","Sin Alimentacion")
                    registro.put("precio",binding.sin.text.toString().toDouble())
                    registro.put("estado","Activo")
                    bd.insert("configuracion", null, registro)
                }else{
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                }
            }
        }
        binding.btactualizar2.setOnClickListener{
            if (TextUtils.isEmpty(binding.con.text.toString())){
                binding.con.error = "este campo es obligatorio"
                binding.con.requestFocus()
            }else{
                val id_conAlimentacion = con.getInt(0)
                registro.put("estado","Antiguo")
                val cant = bd.update("configuracion", registro, "id_configuracion=${id_conAlimentacion}", null)
                if (cant == 1){
                    Toast.makeText(this, "se Actualizo el precio", Toast.LENGTH_SHORT).show()
                    registro.put("alimentacion","Con Alimentacion")
                    registro.put("precio",binding.con.text.toString().toDouble())
                    registro.put("estado","Activo")
                    bd.insert("configuracion", null, registro)
                    startActivity(Intent(this,Configuraciones::class.java))
                    finish()
                }else{
                    Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}