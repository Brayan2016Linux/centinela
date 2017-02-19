package com.example.imagineup.centinela;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.01
 * @since 2015-12-07
 */


public class DialogActivity extends AppCompatActivity {

    private String emailControl;
    private String passwordControl;

    private UserDBHelper userDB;

    private EditText emailCntr;
    private Button okButton;
    private Button cancelButton;

    //Variables enteros
    private static int posicionCedula = 4;
    private static int posicionCorreo = 5;
    private static int posicionPassword = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_email);

        emailCntr = (EditText) findViewById(R.id.input_email);
        okButton = (Button) findViewById(R.id.emailok);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Comprobación de existencia
                String emailIn = emailCntr.getText().toString().toLowerCase();
                //Inicialización de base de datos
                userDB = new UserDBHelper(getApplicationContext());

                try {
                    List<String> mi_usuario = userDB.recuperarFila(emailIn);
                    emailControl = mi_usuario.get(posicionCorreo);
                    passwordControl = mi_usuario.get(posicionPassword);
                } catch (SQLiteException e) {
                    //Código en caso de tabla vacía
                } catch (IndexOutOfBoundsException me) {
                    //Captura excepción si el correo o el password son diferentes
                    emailCntr.setText("");
                }

                //Comparaciòn
                try {
                    boolean validacionfinal = comprobadorString(emailIn, emailControl);

                    if (validacionfinal != true) {
                        //Mensaje de texto
                        mensajeNoExisten();

                    } else {
                        emailCntr.setText("");
                        enviarPassword(emailControl, passwordControl);
                        mensajeEnviado();
                    }
                } catch (NullPointerException punterocero) {
                    //Código en caso de no existir password anteriores
                }

                //cerrar el dialogo
                finish();
            }
        });


        //Boton de cancelar
        cancelButton = (Button) findViewById(R.id.emailcancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cerrar el dialogo
                finish();
            }
        });
    }

    //enviar Password por SMS
    private void enviarPassword(String email, String pass) {
        ContentValues my_value = new ContentValues();
        String direccion = "+" + getResources().getString(R.string.app_name);
        String password = String.format("%s %s %s \n%s %s %s",getResources().getString(R.string.correoUsuario),": ",email,
                getResources().getString(R.string.contrasenaUsuario),": ",pass);
        my_value.put("address", direccion);
        my_value.put("body", password);
        my_value.put("read", "0"); //0 no leído, 1 leído
        //my_value.put("date", "<fecha de hoy>");
        getContentResolver().insert(Uri.parse("content://sms/inbox"), my_value);

        enviarMensajeCorreo(email, pass);

        envioNotificacion(getString(R.string.mensajeEnviado),R.drawable.small_icon);

    }

    //enviar Mensaje por Correo
    private void enviarMensajeCorreo (String email, String pass) {
        //no implementado
    }

    //enviar notificaciones a barra de estado
    private void envioNotificacion(String message, int icon) {
        //ID Notificación
        int notifyID = 1;

        //Inicialización del administrador de notificación
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this);

        //Set Icons, title, Text
        notifBuilder.setSmallIcon(icon);
        notifBuilder.setContentTitle(getResources().getString(R.string.app_name));
        notifBuilder.setContentText(message);

        /*
        //Acción al clickear el mensaje
        Intent ifclickonNotif = new Intent(Intent.ACTION_GET_CONTENT);

        TaskStackBuilder mystackBuilder = TaskStackBuilder.create(this);
        mystackBuilder.addNextIntent(ifclickonNotif);
        PendingIntent resultPendingIntent = mystackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notifBuilder.setContentIntent(resultPendingIntent);*/


        //presentación de la Notificación
        NotificationManager managerofNotify = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //permite el actualizar la notificación
        managerofNotify.notify(notifyID, notifBuilder.build());

    }


    //comprobador de strings
    private boolean comprobadorString(String primerInput, String segundoInput){
        boolean resultado;
        resultado = primerInput.contentEquals(segundoInput);
        return resultado;
    }

    //mensajes de invalidación o alertas
    private void mensajeNoExisten() {
        String mensaje = getResources().getString(R.string.mensajeInvalido);
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    //mensajes de invalidación o alertas
    private void mensajeEnviado() {
        String mensaje = getResources().getString(R.string.mensajeEnviado);
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

}
