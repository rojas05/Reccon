package com.cristian.appreccon

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class RecconDataBase (context: Context, name: String, factory:
SQLiteDatabase.CursorFactory?, Version: Int):
    SQLiteOpenHelper(context,name,factory,Version){
    override fun onCreate(p0: SQLiteDatabase) {
        with(p0){
            execSQL("create table configuracion(alimentacion text PRIMARY KEY, precio real)")
            execSQL("create table recolector(id INTEGER PRIMARY KEY AUTOINCREMENT, nombre text, configuracion text REFERENCES configuracion)")
            execSQL("create table recoleccion(id INTEGER PRIMARY KEY AUTOINCREMENT, kilos INTEGER,fecha text,recolector INTEGER REFERENCES recolector)")
        }
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
    }
}