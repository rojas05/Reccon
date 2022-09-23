package com.cristian.appreccon

import android.content.Context
import android.database.Cursor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CursorAdapter
import com.cristian.appreccon.databinding.ActivityRecolectoresBinding
import com.cristian.appreccon.databinding.ItemRecolectorBinding

class Recolectores : AppCompatActivity() {
    lateinit var binding: ActivityRecolectoresBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecolectoresBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        val admin = RecconDataBase(this, "Reccon", null, 1)
        val bd = admin.writableDatabase
        val cursor= bd.rawQuery("select id as _id, nombre, configuracion from recolector", null
        )
        val adapter= CursorAdapterlv(this, cursor)
        binding.lvVerRecolecctores.adapter= adapter
        bd.close()
    }

    inner class CursorAdapterlv(context: Context, cursor: Cursor):
        CursorAdapter(context, cursor, FLAG_REGISTER_CONTENT_OBSERVER){
        override fun newView(p0: Context?, p1: Cursor?, p2: ViewGroup?): View {
            val inflater= LayoutInflater.from(p0)
            return inflater.inflate(R.layout.item_recolector, p2, false)
        }
        override fun bindView(p0: View?, p1: Context?, p2: Cursor?) {
            val bindingItems= ItemRecolectorBinding.bind(p0!!)
            bindingItems.tvNombre.text= cursor!!.getString(1)
        }

    }
}