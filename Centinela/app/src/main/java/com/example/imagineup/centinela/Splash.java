package com.example.imagineup.centinela;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.01
 * @since 2015-11-9
 */

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public class Splash extends AppCompatActivity {

    //Constante definidora del tiempo de duración del splash
    private final int DURACION_MAX_SPLASH = 4000; //Espera 4 segundos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //plantilla xml del splash
        setContentView(R.layout.activity_splash);

        //al finalizar el tiempo la acción a realizar se coloca en el siguiente código

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //Código de la acción luego de terminado el tiempo del splash
                Intent mi_ventana = new Intent(Splash.this, LoginActivity.class);
                startActivity(mi_ventana);
                finish();
            }
        }, DURACION_MAX_SPLASH);
    }

}
