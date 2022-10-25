package com.cristian.appreccon

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.cristian.appreccon.databinding.ActivityRecolectoresBinding
import com.cristian.appreccon.databinding.ItemRecolectorBinding
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Recolectores : AppCompatActivity() {
    lateinit var binding: ActivityRecolectoresBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecolectoresBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        title = "Recolectores"

        val admin = RecconDataBase(this, "Reccon", null, 1)
        val bd = admin.writableDatabase
        val cursor = bd.rawQuery(
            "select id_recolector as _id, nombre from recolector", null
        )
        val adapter = CursorAdapterlv(this, cursor)
        binding.lvVerRecolecctores.adapter = adapter
        bd.close()

        binding.btPlusRe.setOnClickListener {
        registrarRecolector()
        }
    }

    inner class CursorAdapterlv(context: Context, cursor: Cursor) :
        CursorAdapter(context, cursor, FLAG_REGISTER_CONTENT_OBSERVER) {
        override fun newView(p0: Context?, p1: Cursor?, p2: ViewGroup?): View {
            val inflater = LayoutInflater.from(p0)
            return inflater.inflate(R.layout.item_recolector, p2, false)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun bindView(p0: View?, p1: Context?, p2: Cursor?) {
            val bindingItems = ItemRecolectorBinding.bind(p0!!)
            bindingItems.tvNombre.text = cursor!!.getString(1)
            val idRecolector = cursor.getInt(0)
            val NombreRecolector = cursor.getString(1)

            bindingItems.btRegitrar.setOnClickListener {
                val alerta = AlertDialog.Builder(this@Recolectores)
                val view = layoutInflater.inflate(R.layout.alert_recoleccion, null)
                alerta.setView(view)
                val dialog = alerta.create()
                dialog.show()
                dialog.setCancelable(true)
                dialog.setCanceledOnTouchOutside(false)
                val tex = view.findViewById<TextView>(R.id.tvNombreR)
                tex.setText(NombreRecolector)
                val btGuardar = view.findViewById<Button>(R.id.btguardarRe)
                val btNostrar = view.findViewById<Button>(R.id.btMostrar)

                btGuardar.setOnClickListener {
                    val cantidad = view.findViewById<TextInputEditText>(R.id.tiCantidad)
                    val checkBox = view.findViewById<CheckBox>(R.id.cbAlimentacion)
                    var alimentacion = toString()
                    if (checkBox.isChecked) {
                        alimentacion = "Con Alimentacion"
                    } else {
                        alimentacion = "Sin Alimentacion"
                    }
                    if (TextUtils.isEmpty(cantidad.text)) {
                        cantidad.error = "ingrese los kilos recolectados"
                        cantidad.requestFocus()
                    } else {
                        val admin = RecconDataBase(this@Recolectores, "Reccon", null, 1)
                        val bd = admin.writableDatabase
                        val registro = ContentValues()
                        val Configuracion = bd.rawQuery(
                            "select id_configuracion from configuracion where alimentacion = '${alimentacion}' and estado = 'Activo'",
                            null
                        )
                        if (Configuracion.moveToFirst()) {
                            val fecha = LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern("MMM dd - yyyy"))
                            registro.put("cantidad", cantidad.text.toString().toDouble())
                            registro.put("fecha", fecha.toString())
                            registro.put("recolector", idRecolector)
                            registro.put("configuracion", Configuracion.getInt(0))
                            bd.insert("recoleccion", null, registro)
                            cantidad.setText("")
                            Toast.makeText(this@Recolectores, "registro guardado", Toast.LENGTH_SHORT).show()
                            dialog.cancel()
                        }
                    }
                }
                val admin = RecconDataBase(this@Recolectores, "Reccon", null, 1)
                val bd = admin.writableDatabase

                btNostrar.setOnClickListener {
                    val cantidad = view.findViewById<TextInputEditText>(R.id.tiCantidad)
                    if (TextUtils.isEmpty(cantidad.text)){
                        val prueva = bd.rawQuery(
                            "select * from recoleccion where recolector = '${idRecolector}'",
                            null
                        )
                        if (prueva.moveToFirst()){
                            val intento = Intent(this@Recolectores, DetalleRecolector::class.java)
                            intento.putExtra("Id", idRecolector)
                            intento.putExtra("nombre",NombreRecolector)
                            startActivity(intento)
                            dialog.cancel()
                        }else{
                            Toast.makeText(this@Recolectores, "nada para mostar", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this@Recolectores, "prcione el boton GUARDAR", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            bindingItems.btEliminar.setOnClickListener {
                val alerta = AlertDialog.Builder(this@Recolectores)
                val view = layoutInflater.inflate(R.layout.eliminar, null)
                alerta.setView(view)
                val dialog = alerta.create()
                dialog.show()
                dialog.setCancelable(true)
                dialog.setCanceledOnTouchOutside(true)
                view.findViewById<TextView>(R.id.tvMensaje).setText("¿esta seguro de eliminar el recolector ${NombreRecolector}?")
                val admin = RecconDataBase(this@Recolectores, "Reccon", null, 1)
                val bd = admin.writableDatabase
                view.findViewById<Button>(R.id.btSi).setOnClickListener{
                    val Configuracion = bd.rawQuery(
                        "select * from recoleccion where recolector = '${idRecolector}'",
                        null
                    )
                    if (Configuracion.moveToFirst()){
                        Toast.makeText(this@Recolectores, "El recolector ${NombreRecolector} no se puede eliminar", Toast.LENGTH_SHORT).show()
                        dialog.cancel()
                    }else{
                        val cant = bd.delete("recolector", "id_recolector=${idRecolector}", null)
                        startActivity(Intent(this@Recolectores,Recolectores::class.java))
                        dialog.cancel()
                        if (cant == 1){
                            Toast.makeText(this@Recolectores, "el recolector se elimino correctamente", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                view.findViewById<Button>(R.id.btNo).setOnClickListener {
                    dialog.cancel()
                }

            }
        }
    }

    private fun registrarRecolector() {
        val alerta = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.ingresar_recolector, null)
        alerta.setView(view)
        val dialog = alerta.create()
        dialog.show()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        val recolector = view.findViewById<TextInputEditText>(R.id.tiRecolector)
        view.findViewById<Button>(R.id.btguardar).setOnClickListener {
            if (TextUtils.isEmpty(recolector.text.toString())) {
                recolector.error = "porfavor ingrese un recolector"
                recolector.requestFocus()
            } else {
                val admin = RecconDataBase(this, "Reccon", null, 1)
                val bd = admin.writableDatabase
                val registro = ContentValues()

                registro.put("nombre", recolector.text.toString())
                bd.insert("recolector", null, registro)

                Toast.makeText(
                    this,
                    recolector.text.toString() + " fue guardado exitosamente",
                    Toast.LENGTH_SHORT
                ).show()
                recolector.setText("")
            }
        }
        view.findViewById<Button>(R.id.btfinalizar).setOnClickListener {
            if(TextUtils.isEmpty(recolector.text.toString())){
                dialog.cancel()
                startActivity(Intent(this,Recolectores::class.java))
                finish()
            }else{
                Toast.makeText(this, "Precione el boton guardar", Toast.LENGTH_SHORT).show()
            }
        }
    }
}