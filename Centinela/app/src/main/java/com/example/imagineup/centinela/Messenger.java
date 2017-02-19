package com.example.imagineup.centinela;


import android.widget.Toast;
import android.content.Context;

/**
 *
 * Creado por Brayan Rodríguez Delgado
 * Para evitar duplicados en mensajes
 * y facilitar su mantenimiento.
 *
 * Fecha: 21-05-2016
 * Versión: 0.0.1
 *
 */

public class Messenger {

    public Messenger() {
        //Constructor de la clase sin parámetros
    }

    public void MessageConnected(Context context) {
        Toast.makeText(context, "Connected", Toast.LENGTH_LONG).show();
    }

}