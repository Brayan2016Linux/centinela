package com.example.imagineup.centinela;

import android.content.Context;
import android.content.Intent;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;


/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.02
 * @since 2015-12-11 2016-03-04
 */

public class EditProfile extends AppCompatActivity {

    //Variable para capturar un Extra de LoginActivity
    private String userEmail;

    //Variable base de Datos Usuario
    private UserDBHelper myUserInfo;

    //variables a utilizar para almacenar base datos usuarios
    private String userName;
    private int userID;
    private String userLastName1;
    private String userLastName2;
    private String userIDCARD;
    private String userIDCARDControl;
    private String userPassword;
    private String userPassControl;
    private UserDBHelper userDB;

    //Definición de Widgets
    private EditText nombreUsuario;
    private EditText apellidoUsuario1;
    private EditText apellidoUsuario2;
    private EditText idCardUsuario;
    private EditText emailUsuario;
    private EditText passwordInput01;
    private EditText passwordInput02;

    //Constantes enteros
    private static int posicionUsuario = 1;
    private static int posicionCorreo = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //Inicialización de Widgets
        //Campo de texto del registro
        nombreUsuario = (EditText) findViewById(R.id.Name_Input);
        apellidoUsuario1 = (EditText) findViewById(R.id.LastName1_Input);
        apellidoUsuario2 = (EditText) findViewById(R.id.LastName2_Input);
        idCardUsuario = (EditText) findViewById(R.id.IDCARD_Input);
        emailUsuario = (EditText) findViewById(R.id.Email_Input);
        passwordInput01 = (EditText) findViewById(R.id.Password_Input1);
        passwordInput02 = (EditText) findViewById(R.id.Password_Input2);

        //Inicialización base de datos
        myUserInfo = new UserDBHelper(getApplicationContext());

        //Boton guardar
        Button boton_guardar = (Button) findViewById(R.id.botonGuardar);
        Button boton_cancelar = (Button) findViewById(R.id.botonCancelar);

        //recuperación del correo del usuario de Actividad Login
        Bundle extras = getIntent().getExtras();
        userEmail = extras.getString(getResources().getString(R.string.userID));
        userIDCARDControl = extras.getString(getResources().getString(R.string.userNumber));

        //encuentra el entero del registro y lo pasa a la variable control
        List<String> myUser = myUserInfo.recuperarFila(userEmail);

        /*Estructura de myUserInfo
        * ID
        * Nombre
        * Apellido1
        * Apellido2
        * Cedula
        * Correo
        * Password
        * Fecha_registro*/


        userID = Integer.parseInt(myUser.get(0));
        userName = myUser.get(1);
        userLastName1 = myUser.get(2);
        userLastName2 = myUser.get(3);
        userIDCARD = myUser.get(4);
        userPassword = myUser.get(6);

        //Llena los campos con la información recuperada de la base de datos
        nombreUsuario.setText(userName);
        emailUsuario.setText(userEmail);
        apellidoUsuario1.setText(userLastName1);
        apellidoUsuario2.setText(userLastName2);
        idCardUsuario.setText(userIDCARD);
        passwordInput01.setText(userPassword);
        passwordInput02.setText(userPassword);


        //Código del botón guardar
        boton_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //llenado de campos
                userName = nombreUsuario.getText().toString();
                userEmail = emailUsuario.getText().toString().toLowerCase();
                userLastName1 = apellidoUsuario1.getText().toString();
                userLastName2 = apellidoUsuario2.getText().toString();
                userIDCARD = idCardUsuario.getText().toString();
                userPassword = passwordInput01.getText().toString();
                userPassControl = passwordInput02.getText().toString();

                //Comprobación de campos

                boolean longitud = longitudString(userName) &&
                        longitudString(userLastName1) &&
                        longitudString(userLastName2) &&
                        longitudString(userIDCARD) &&
                        longitudString(userEmail) &&
                        longitudString(userPassword) &&
                        longitudString(userPassControl);

                if (longitud != true) {
                    campoVacioAdvertencia(userName, getResources().getString(R.string.nombreUsuario));
                    campoVacioAdvertencia(userLastName1,getResources().getString(R.string.apellidoText1));
                    campoVacioAdvertencia(userLastName2,getResources().getString(R.string.apellidoText2));
                    campoVacioAdvertencia(userIDCARD,getResources().getString(R.string.identificacionU));
                    campoVacioAdvertencia(userEmail,getResources().getString(R.string.correoUsuario) );
                    campoVacioAdvertencia(userPassword, getResources().getString(R.string.contrasenaUsuario));
                    campoVacioAdvertencia(userPassControl, getResources().getString(R.string.contrasenaConfirma));
                } else {
                    //Comprobación Password
                    boolean compPass = comprobadorString(userPassword, userPassControl);
                    if (compPass != true) {
                        mensajePasswordDif();
                        passwordInput01.setText("");
                        passwordInput02.setText("");

                    } else {

                        //Inicializador de la base de datos
                        myUserInfo = new UserDBHelper(getApplicationContext());


                        try {

                            //Ocultar el teclado con la última introducción
                            InputMethodManager inputMag = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMag.hideSoftInputFromWindow(passwordInput02.getWindowToken(), 0);


                            //Ingresa el usuario
                            actualizarDatosDB(userID,
                                    userName,
                                    userLastName1,
                                    userLastName2,
                                    userIDCARD,
                                    userEmail,
                                    userPassword,
                                    myUserInfo);

                            myUserInfo.close();

                            //Código de actualización base de datos web

                        } catch (SQLiteException e) {
                            //Código en caso de tabla vacía


                        } catch (IndexOutOfBoundsException me)
                        {
                            //Captura excepción si no existen datos anteriores
                            //Ingresa el usuario
                            actualizarDatosDB(userID, userName, userLastName1, userLastName2, userIDCARD, userEmail, userPassword, myUserInfo);
                            myUserInfo.close();
                            //Código de actualización base de datos web
                        }
                    }

                }//Fin comprobación password

            } //Fin del comprobación longitud

        });

        //Código del botón cancelar
        boton_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirActividadTarjetas(userEmail, userIDCARD);
            }
        });


    }

    //operaciòn de guardado final
    private void actualizarDatosDB(int userid,
                                   String username,
                                   String apellido1,
                                   String apellido2,
                                   String numerousuario,
                                   String email,
                                   String password,
                                   UserDBHelper usdb){

         /*Estructura de myUserInfo
        * ID
        * Nombre
        * Apellido1
        * Apellido2
        * Cedula
        * Correo
        * Password
        * Fecha_registro*/

        usdb.actualizarUSUARIO(userid, username, apellido1, apellido2, numerousuario, email, password);
        usdb.close();
        mensajeOperacionExitosa();

        if (!numerousuario.equals(userIDCARDControl))
        {
            //Actualizar base de datos de tarjetas
            actualizarReferenciasTarjeta(numerousuario, userIDCARDControl);

            //finaliza actividad y abre nuevamente la actividad de control
            abrirActividadTarjetas(userEmail, numerousuario);

        }
        else {
            //finaliza actividad y abre nuevamente la actividad de control
            abrirActividadTarjetas(userEmail, userIDCARDControl);
        }
    }

    //Actualización de base de datos de tarjetas si es necesario
    private void actualizarReferenciasTarjeta(String numerousuario, String usercontrol){
        //Inicializamos la base de datos de tarjetas
        CardDBHelper mycardDB = new CardDBHelper(getApplicationContext());

        //Objeto CardInfo para grabar momentáneamente los datos recuperados;
        CardInfo mycardData;

        //Conteo de número de filas para el ciclo
        int numeroFilas;
        numeroFilas = mycardDB.numberOfRows();

        Log.d("Tenemos: ", numeroFilas + " registros");

        //variables temporales:
        int idTarjeta;
        String NombreTarjeta;
        String Emisor;
        String userID;
        int DiasCorte;
        int DiasGracia;

        for (int i = 1; i <= numeroFilas; i ++){

            /*Estructura de myCardInfo
            * ID
            * Cédula o Pasaporte
            * Nombre Tarjeta
            * Emisor
            * Dias Corte
            * Dias Gracia
            * Fecha de registro */

            //recuperar tarjeta i-ésima
            try {
                mycardData = mycardDB.recuperarTarjeta(Integer.toString(i));
                idTarjeta = mycardData.obtenerIDCard();
                NombreTarjeta = mycardData.obtenerNombreID();
                Emisor = mycardData.obtenerEmisorID();
                userID = mycardData.obtenerIDUser();
                DiasCorte = mycardData.obtenerDiaCorte();
                DiasGracia = mycardData.obtenerDiaGracia();
                mycardDB.close();


                Log.d("Recuperados: ", "iT: " + idTarjeta + " NT: " + NombreTarjeta + " E: " + Emisor + " U: " +
                        userID + " DC: " + DiasCorte + " DG: " + DiasGracia);
                Log.d("Numero Usuario: ", "BD: " + userID + " CH: " + numerousuario + " Control: " + usercontrol);


                //actualizar tarjeta i-èsima si hay correspondencia y no pertenece a ningún otro usuario
                if (userID.equals(usercontrol)) {
                    CardDBHelper mycardactual = new CardDBHelper(getApplicationContext());
                    mycardactual.actualizarTarjeta(idTarjeta, numerousuario, NombreTarjeta, Emisor, DiasCorte, DiasGracia);
                    mycardactual.close();
                }

            }
            catch (CursorIndexOutOfBoundsException e)
            {
               //ïndice vacío
                Log.e("Error 0 ","Índice vacío");
            }


        }


    }

    //reseteo de los campos de texto
    private  void resetCamposTexto(){
        nombreUsuario.setText("");
        emailUsuario.setText("");
        passwordInput01.setText("");
        passwordInput02.setText("");

    }

    private void mensajeOperacionExitosa() {
        String mensaje = getResources().getString(R.string.operacionExitosa);
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    private void mensajePasswordDif(){
        String mensaje = getResources().getString(R.string.passwordNoCoinciden);
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    //cierra la actividad actual
    private void finalizarActividad(){
        //reseteo de los campos llenados
        resetCamposTexto();
        //cierre de actividad
        this.finish();
    }

    //Abre actividad correspondiente
    private void abrirActividadTarjetas(String userEmail, String usernumber){
        Intent miActividad = new Intent(this, ControlActivity.class);
        miActividad.putExtra(getResources().getString(R.string.userID), userEmail);
        miActividad.putExtra(getResources().getString(R.string.userNumber), usernumber);
        startActivity(miActividad);
        finalizarActividad();
    }

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

    //Mensaje de advertencia si campo está vacío
    private void campoVacioAdvertencia(String campo, String mensaje){
        String mensaje_completo = getResources().getString(R.string.completeElCampo) + " " + mensaje;

        if (longitudString(campo) != true)
        {
            Toast.makeText(getApplicationContext(), mensaje_completo,
                    Toast.LENGTH_SHORT).show();
        }
    }

}
