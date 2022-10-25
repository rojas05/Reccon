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
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.cursoradapter.widget.CursorAdapter
import com.cristian.appreccon.databinding.ActivityDetalleRecolectorBinding
import com.cristian.appreccon.databinding.ItemAnterioresBinding
import com.google.android.material.textfield.TextInputEditText
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class DetalleRecolector : AppCompatActivity() {
    lateinit var binding: ActivityDetalleRecolectorBinding
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetalleRecolectorBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val bundle = intent.extras
        val ID = bundle?.getInt("Id")
        val Nombre = bundle?.getString("nombre")
        title = Nombre
        val admin = RecconDataBase(this, "Reccon", null, 1)
        val bd = admin.writableDatabase
        val registro = ContentValues()
        val Configuracion = bd.rawQuery(
            "select id_recoleccion as _id,cantidad,configuracion,fecha from recoleccion where recolector = '${ID}' order by id_recoleccion desc ",
            null
        )
        if (Configuracion.moveToFirst()) {
            binding.tvCantidadD.setText(Configuracion.getDouble(1).toString() + " Kg")
        }
        val idRecoleccion = Configuracion.getInt(0)

        binding.btActualizar.setOnClickListener {
            val busqueda = bd.rawQuery(
                "select alimentacion,configuracion,cantidad,recolector,fecha,id_recoleccion from configuracion,recoleccion where recolector = '${ID}' order by id_recoleccion desc ",
                null
            )
            if (busqueda.moveToFirst()) {
                val alerta = AlertDialog.Builder(this)
                val view = layoutInflater.inflate(R.layout.alert_recoleccion, null)
                alerta.setView(view)
                val dialog = alerta.create()
                dialog.show()
                dialog.setCancelable(true)
                dialog.setCanceledOnTouchOutside(false)
                val tex = view.findViewById<TextView>(R.id.tvNombreR)
                tex.setText(Nombre)
                val btGuardar = view.findViewById<Button>(R.id.btguardarRe)
                val btNostrar = view.findViewById<Button>(R.id.btMostrar)
                val cantidad = view.findViewById<TextInputEditText>(R.id.tiCantidad)
                val checkBox = view.findViewById<CheckBox>(R.id.cbAlimentacion)
                val alimentacion = busqueda.getString(1)
                val catidadKg = busqueda.getDouble(2)
                cantidad.setText(catidadKg.toString())

                val bus = bd.rawQuery(
                    "select alimentacion from configuracion where id_configuracion = '${alimentacion}'",
                    null
                )
                if (bus.moveToFirst()) {
                if (bus.getString(0) == "Con Alimentacion") {
                    checkBox.isChecked = true
                }
                    cantidad.setText(catidadKg.toString())
                    btGuardar.setOnClickListener {
                    if (TextUtils.isEmpty(cantidad.text)) {
                        cantidad.error = "ingrese los kilos recolectados"
                        cantidad.requestFocus()
                    } else {
                        var alimen = toString()
                        if (checkBox.isChecked) {
                            alimen = "Con Alimentacion"
                        } else {
                            alimen = "Sin Alimentacion"
                        }
                        val Alimenacion = bd.rawQuery(
                            "select id_configuracion from configuracion where alimentacion = '${alimen}' and estado = 'Activo'",
                            null
                        )
                        if (Alimenacion.moveToFirst()) {
                            val fecha = LocalDateTime.now()
                                .format(DateTimeFormatter.ofPattern("MMM dd - yyyy"))
                            registro.put("fecha",fecha.toString())
                            registro.put("cantidad", cantidad.text.toString().toDouble())
                            registro.put("configuracion", Alimenacion.getInt(0))
                            val cant = bd.update("recoleccion", registro, "id_recoleccion=${idRecoleccion}", null)
                            if (cant == 1){
                                Toast.makeText(this, "actualizado correctamente", Toast.LENGTH_SHORT).show()
                                dialog.cancel()
                                val prueva = bd.rawQuery(
                                    "select * from recoleccion where recolector = '${ID}'",
                                    null
                                )
                                if (prueva.moveToFirst()) {
                                    val intento = Intent(this, DetalleRecolector::class.java)
                                    intento.putExtra("Id", ID)
                                    intento.putExtra("nombre", Nombre)
                                    startActivity(intento)
                                    dialog.cancel()
                                } else {
                                    Toast.makeText(this, "nada para mostar", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
                btNostrar.setOnClickListener {
                    val cantidad = view.findViewById<TextInputEditText>(R.id.tiCantidad)
                    if (TextUtils.isEmpty(cantidad.text)){
                        val prueva = bd.rawQuery(
                            "select * from recoleccion where recolector = '${ID}'",
                            null
                        )
                        if (prueva.moveToFirst()){
                            val intento = Intent(this@DetalleRecolector, DetalleRecolector::class.java)
                            intento.putExtra("Id", ID)
                            intento.putExtra("nombre",Nombre)
                            startActivity(intento)
                            dialog.cancel()
                        }else{
                            Toast.makeText(this@DetalleRecolector, "nada para mostar", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText(this@DetalleRecolector, "prcione el boton GUARDAR", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
        val adaptador = cursorA(this,Configuracion)
        binding.lvAnterior.adapter = adaptador

    }
    inner class cursorA(context: Context, cursor: Cursor) : CursorAdapter(context,cursor,
        FLAG_REGISTER_CONTENT_OBSERVER){
        override fun newView(context: Context?, cursor: Cursor?, parent: ViewGroup?): View {
            val inflate = LayoutInflater.from(context)
            return inflate.inflate(R.layout.item_anteriores,parent,false)
        }

        override fun bindView(view: View?, context: Context?, cursor: Cursor?) {
            val bindingItems = ItemAnterioresBinding.bind(view!!)
            bindingItems.tvCantidad.text = cursor!!.getDouble(1).toString()+" Kg"
            bindingItems.tvConfiguracion.text= cursor.getString(3)
        }
    }
}