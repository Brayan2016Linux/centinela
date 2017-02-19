package com.example.imagineup.centinela;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.01
 * @since 2016-05-26
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class SettingsActivity extends AppCompatActivity {

    private String userNumber;
    private SettingsInfo mysettings;
    private SettingsInfo mytemporalSettings;

    private int diasantepago;
    private int notificacion_booleana;
    private int idtransaction;

    //inicialización de los widgets:
    Spinner Spinner_diasAntesPago;
    CheckBox Notificaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //recuperación del correo y el número de usuario desde Actividad Login
        Bundle extras = getIntent().getExtras();
        userNumber = extras.getString(getResources().getString(R.string.userNumber));

        mysettings = devolverSettingsUsuario(userNumber);
        Notificaciones = (CheckBox) findViewById(R.id.NotificacionesCheckbox);

        if (mysettings != null) {
            Log.e("Settings: ", "recuperado exitosamente");
            idtransaction = mysettings.obtenerIDTransaction();
            diasantepago = mysettings.obtenerDiasAntePago();
            notificacion_booleana = mysettings.obtenerAutorizacionNotificacion();

            //Cargar los settings en la actividad
            crearSpinner(diasantepago);

        }
        else
        {
            Log.e("Settings: ", "no existen se creara uno");
            diasantepago = 3;
            notificacion_booleana = 1;

            //Guardar settings default del usuario
            guardarBaseDatosSettings(userNumber, diasantepago, notificacion_booleana);

            crearSpinner(diasantepago);
        }

        if (notificacion_booleana == 1) {
            Notificaciones.setChecked(true);
        }



        //Edición
        //Boton de Guardar
        Button GuardarBTN = (Button) findViewById(R.id.buttonOK);
        GuardarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int dias_ante_pago = recuperarValorSpinner();
                int notificacion = recuperarValorNotificacion(Notificaciones);

                if (mysettings != null)
                {
                    actualizarBaseDatosSettings(idtransaction, userNumber, dias_ante_pago, notificacion);
                }
                else {
                    guardarBaseDatosSettings(userNumber, dias_ante_pago, notificacion);
                }

                cerrarActividad();
            }

        });


        //Botón de Cancelar
        Button CancelBTN = (Button) findViewById(R.id.button_CNL);
        CancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarActividad();
            }
        });

    }

    //**************************************************************************************
    //cierra la actividad
    private void cerrarActividad(){
        this.finish();
    }

    //**************************************************************************************
    //Crea el Spinner de los días antes de pago
    private void crearSpinner (int diasantepago) {
        Spinner_diasAntesPago = (Spinner) findViewById(R.id.spinner_diasAntePago);
        ArrayAdapter spinner_adapter_DAP = ArrayAdapter.createFromResource(this, R.array.spinner_days, android.R.layout.simple_list_item_1);
        spinner_adapter_DAP.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_diasAntesPago.setAdapter(spinner_adapter_DAP);
        Spinner_diasAntesPago.setSelection(diasantepago - 1);
    }

    //recuperar valor spinner
    private int recuperarValorSpinner() {
        int myvaluedays;
        myvaluedays = Spinner_diasAntesPago.getSelectedItemPosition() + 1;
        return myvaluedays;
    }

    //recuperar valor Notificacion
    private int recuperarValorNotificacion(CheckBox mynotification) {

            if (mynotification.isChecked() == true) {
                return 1;
            } else {
                return 0;
            }
    }

    //************************************************************************************
    //Guardar en la base de datos
    private void guardarBaseDatosSettings (String userNumber, int diasantepago, int notificacion_booleana) {

        SettingsDBHelper mynewSettings = new SettingsDBHelper(getApplicationContext());
        mynewSettings.insertarSETTINGS(diasantepago, notificacion_booleana, userNumber);
        mynewSettings.close();

    }


    //Actualizar en la base de datos
    private void actualizarBaseDatosSettings (int idconfiguracion, String userNumber, int diasantepago, int notificacion_booleana) {

        SettingsDBHelper mynewSettings = new SettingsDBHelper(getApplicationContext());
        mynewSettings.actualizarCONFIGURACION(idconfiguracion, diasantepago, notificacion_booleana, userNumber);
        mynewSettings.close();

    }


    //*************************************************************************************
    //Crea un objeto SettingsInfo para devolver los setting almacenados en la base de datos
    private SettingsInfo devolverSettingsUsuario(String numerousuario) {

        SettingsInfo mytemporalSet;
        SettingsDBHelper settingsDB = new SettingsDBHelper(getApplicationContext());

        try {
            mytemporalSet = settingsDB.fetchARowbyUserID(numerousuario);
            mytemporalSettings = mytemporalSet;
        }catch (SQLiteException ex) {
            Log.e("SQLException ", " guardarBaseDatosSettings - SettingsActivity");
            Log.e("trace: ", ex.getMessage().toString());
        } catch (CursorIndexOutOfBoundsException ex) {
            Log.e("Cursor Exception ", " guardarBaseDatosSettings - SettingsActivity");
            Log.e("trace: ", ex.getMessage().toString());
        }

        settingsDB.close();
        return mytemporalSettings;

    }

}
