package com.cristian.appreccon

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.cristian.appreccon.databinding.ActivityModuloPincipalBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ModuloPincipal : AppCompatActivity() {
    lateinit var binding: ActivityModuloPincipalBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityModuloPincipalBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val hora= LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("HH"))

        if (hora < 12.toString()){
            binding.tvSaludo.setText("Buenos Dias")
        }else if(hora >= 12.toString() && hora < 18.toString()){
            binding.tvSaludo.setText("Buenas Tardes")
        }else{
            binding.tvSaludo.setText("Buenas Noches")
        }

        val preferencias = getSharedPreferences( "registrar", Context.MODE_PRIVATE)
        val estado = preferencias.getString("estado","")
        val estadoRecolectores = preferencias.getString("estadoRecolector","")

        if (estado == ""){
            configuracion()
        }
        binding.btrecolecion.setOnClickListener{
            if(estadoRecolectores == ""){
                registrarRecolector()
            }else{
                startActivity(Intent(this,Recolectores::class.java))
            }
        }
        binding.btinformes.setOnClickListener {
            Toast.makeText(this, "Funcion aun no disponible " +
                    " estamos trabajando ;)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun registrarRecolector() {
        val alerta = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.ingresar_recolector,null)
        alerta.setView(view)
        val dialog = alerta.create()
        dialog.show()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        var recolector = view.findViewById<TextInputEditText>(R.id.tiRecolector)
        val preferencias = getSharedPreferences( "registrar", Context.MODE_PRIVATE)
        val editor = preferencias.edit()

        view.findViewById<Button>(R.id.btguardar).setOnClickListener {
         if (TextUtils.isEmpty(recolector.text.toString())){
             recolector.error = "porfavor ingrese un recolector"
             recolector.requestFocus()
         }else{
             val admin = RecconDataBase(this,"Reccon", null, 1)
             val bd = admin.writableDatabase
             val registro = ContentValues()

             registro.put("nombre",recolector.text.toString())
             bd.insert("recolector", null, registro)

             editor.putString("estadoRecolector","1")
             editor.apply()
             Toast.makeText(this, recolector.text.toString()+" fue guardado exitosamente", Toast.LENGTH_SHORT).show()
             recolector.setText("")
         }
        }

        view.findViewById<Button>(R.id.btfinalizar).setOnClickListener {
            val estadoRecolector = preferencias .getString("estadoRecolector","")
            if (recolector.text.toString() != ""){
                Toast.makeText(this, "precione el boton GUARDAR", Toast.LENGTH_SHORT).show()
            }else{
                if (estadoRecolector == ""){
                    dialog.cancel()
                    startActivity(Intent(this,ModuloPincipal::class.java))
                    finish()
                }else{
                    dialog.cancel()
                    startActivity(Intent(this,Recolectores::class.java))
                    finish()
                }
            }
        }
    }

    fun configuracion(){
        val alerta = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.alert,null)
        alerta.setView(view)
        val dialog = alerta.create()
        dialog.show()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        var ali =  view.findViewById<TextInputEditText>(R.id.tiAlimentacion)
        var noAli =  view.findViewById<TextInputEditText>(R.id.tiNoAlimentacio)
        val preferencias = getSharedPreferences( "registrar", Context.MODE_PRIVATE)
        val editor = preferencias.edit()

        view.findViewById<Button>(R.id.guardar).setOnClickListener {
            if (TextUtils.isEmpty(ali.text.toString()) && TextUtils.isEmpty(noAli.text.toString())){
                noAli.error = "este campo es obligatorio"
                ali.error = "este campo es obligatorio"
                ali.requestFocus()
                noAli.requestFocus()
            }else{
                val admin = RecconDataBase(this,"Reccon", null, 1)
                val bd = admin.writableDatabase
                val registro = ContentValues()


                registro.put("alimentacion","Con Alimentacion")
                registro.put("precio",ali.text.toString().toDouble())
                registro.put("estado","Activo")
                bd.insert("configuracion", null, registro)

                registro.put("alimentacion","Sin Alimentacion")
                registro.put("precio",noAli.text.toString().toDouble())
                registro.put("estado","Activo")
                bd.insert("configuracion", null, registro)

                editor.putString("estado","1")
                editor.apply()
                dialog.cancel()
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.ajustes ->{
                startActivity(Intent(this,Configuraciones::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}