package com.cristian.appreccon

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class RecconDataBase (context: Context, name: String, factory:
SQLiteDatabase.CursorFactory?, Version: Int):
    SQLiteOpenHelper(context,name,factory,Version){
    override fun onCreate(p0: SQLiteDatabase) {
        with(p0){
            execSQL("create table recolector(id_recolector INTEGER PRIMARY KEY AUTOINCREMENT, nombre text)")
            execSQL("create table configuracion(id_configuracion INTEGER PRIMARY KEY AUTOINCREMENT, alimentacion text, precio real,estado text)")
            execSQL("create table recoleccion(id_recoleccion INTEGER PRIMARY KEY AUTOINCREMENT, cantidad real,fecha text,recolector INTEGER REFERENCES recolector, configuracion INTEGER REFERENCES configuracion)")
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }
}