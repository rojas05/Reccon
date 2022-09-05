package com.cristian.appreccon

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.cristian.appreccon.databinding.ActivityModuloPincipalBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ModuloPincipal : AppCompatActivity() {
    lateinit var binding: ActivityModuloPincipalBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityModuloPincipalBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val preferencias = getSharedPreferences( "registrar", Context.MODE_PRIVATE)
        val estado = preferencias .getString("estado","")

        if (estado == ""){
            configuracion()
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
                editor.putString("con_alimentacion",ali.text.toString())
                editor.putString("sin_alimentacion",noAli.text.toString())
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