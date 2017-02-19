package com.example.imagineup.centinela;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.02
 * @since 2015-11-9 2016-03-03
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class LoginActivity extends AppCompatActivity {

    //variables a utilizar para almacenar base datos usuarios
    private String userEmail;
    private String userPassword;
    private String userID;

    private String emailControl;
    private String passwordControl;

    private UserDBHelper myUserInfo;

    //Definición de Widgets
    private EditText emailUsuario;
    private EditText passwordInput01;
    private TextView olvidoPassword;

    private EditText emailCntr;
    public String emailIN;

    //Variables enteros

    private static int posicionCedula = 4;
    private static int posicionCorreo = 5;
    private static int posicionPassword = 6;



    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Código de recuperación de la base de datos vía web y llenado de base de datos interna

        /*Estructura de myUserInfo
        * ID
        * Nombre
        * Apellido1
        * Apellido2
        * Cedula
        * Correo
        * Password
        * Fecha_registro*/


        //Campo de texto del registro
        emailUsuario = (EditText) findViewById(R.id.Email_Input);
        passwordInput01 = (EditText) findViewById(R.id.Password_Input);

        //Boton guardar
        Button boton_sesion = (Button) findViewById(R.id.botonInicio);

        //Comprueba
        boton_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Llenado de campos
                userEmail = emailUsuario.getText().toString().toLowerCase();
                userPassword = passwordInput01.getText().toString();

                boolean longitudC = longitudString(userEmail) && longitudString(userPassword);

                if (longitudC != true)
                {
                    //Mensaje de texto de éxito
                    mensajeCompleteCampos();
                }
                else
                {
                    //Inicialización de base de datos
                    myUserInfo = new UserDBHelper(getApplicationContext());

                    try {
                        List<String> mi_usuario = myUserInfo.recuperarFila(userEmail);
                        userID = mi_usuario.get(posicionCedula);
                        emailControl = mi_usuario.get(posicionCorreo);
                        passwordControl = mi_usuario.get(posicionPassword);

                        /**
                         * Llenado de información a través de clase Usuario
                         * */
                        Usuario mi_usuarioN = myUserInfo.recuperarUsuarioCorreo(userEmail);
                        String mensaje = getResources().getString(R.string.bienvenida) + " ";
                        Toast.makeText(getApplicationContext(), mensaje + mi_usuarioN.obtenerNombreUsuario(),
                                Toast.LENGTH_SHORT).show();

                        //Log.d("Recuperados: "," Usuario: " + userID + " Correo: " + emailControl + " Password " + passwordControl);


                    } catch (SQLiteException e) {
                        //Código en caso de tabla vacía
                        mensajeUsuariosNoExisten();
                    }
                    catch (IndexOutOfBoundsException me)
                    {
                        //Captura excepción si el correo o el password son diferentes
                        mensajeInvalido();
                        resetCamposTexto();
                    }

                    //Comparaciòn
                    try {
                        boolean validacionfinal = comprobadorString(userEmail, emailControl) &&
                                comprobadorString(userPassword, passwordControl);

                        if (validacionfinal != true) {
                                //Mensaje de texto
                                mensajeInvalido();

                        } else {
                                resetCamposTexto();
                                lanzar_ActivityControl(v, userEmail, userID);
                        }
                        } catch (NullPointerException punterocero) {
                            //Código en caso de no existir password anteriores
                        }
                    }
                }

        });



        //Abre una actividad a register
        Button boton_crear = (Button) findViewById(R.id.botonCntNueva);
        boton_crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetCamposTexto();
                lanzar_ActivityRegistro(v);
            }
        });



        //Etiqueta clickeable para enviar contraseña al correo
        olvidoPassword = (TextView) findViewById(R.id.olvidarPassword);

        olvidoPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //código para enviar contraseña por correo
                cuadroDialogoRecuperacion(v);
            }
        });


    }

    //*******************************************************************************************

    //Para lanzar la actividad al presionar el botón de crear registro
    protected void lanzar_ActivityRegistro(View view){
        Intent miActividad = new Intent(this, RegisterActivity.class);
        startActivity(miActividad);
    }

    //Para lanzar la actividad al presionar el botón de crear registro
    protected void lanzar_ActivityControl(View view, String user_email, String userid){
        Intent miActividad = new Intent(this, ControlActivity.class);
        //pasa el parámetro user_email con identificador correousuario
        miActividad.putExtra(getResources().getString(R.string.userID), user_email);
        miActividad.putExtra(getResources().getString(R.string.userNumber), userid);
        startActivity(miActividad);
    }

    //*******************************************************************************************

    private void cuadroDialogoRecuperacion(View view){
        Intent miActividad = new Intent(this, DialogActivity.class);
        startActivity(miActividad);
    }

    //*******************************************************************************************


    //comprobador de strings
    private boolean comprobadorString(String primerInput, String segundoInput){
        boolean resultado;
        resultado = primerInput.contentEquals(segundoInput);
        return resultado;
    }

    //comprobador de longitud de string
    private boolean longitudString (String stringInput) {
        boolean resultado;
        if (stringInput.trim().length()>0)
            resultado = true;
        else
            resultado = false;

        return resultado;
    }

    //********************************************************************************************

    //reseteo de los campos de texto
    private  void resetCamposTexto(){
        emailUsuario.setText("");
        passwordInput01.setText("");
    }

    //*******************************************************************************************
    //mensajes de invalidación o alertas
    private void mensajeInvalido() {
        String mensaje = getResources().getString(R.string.mensajeInvalido);
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    private void mensajeNoImplementado() {
        String mensaje = getResources().getString(R.string.mensajeNoImplementado);
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    private void mensajeUsuariosNoExisten(){
        String mensaje = getResources().getString(R.string.crearCuentaNueva);
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    private void mensajeCompleteCampos() {
        String mensaje = getResources().getString(R.string.completeCamposVacios);
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
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
