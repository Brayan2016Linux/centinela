package com.example.imagineup.centinela;

import android.content.Context;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;


/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.01
 * @since 2015-11-9
 */


public class RegisterActivity extends AppCompatActivity {

    //variables a utilizar para almacenar base datos usuarios
    private String userName;
    private String userLastName1;
    private String userLastName2;
    private String userIDCARD;
    private String userEmail;
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
    private static int posicionUsuario = 2;
    private static int posicionCedula = 4;
    private static int posicionCorreo = 5;
    private static int posicionPassword = 6;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Campo de texto del registro
        nombreUsuario = (EditText) findViewById(R.id.Name_Input);
        apellidoUsuario1 = (EditText) findViewById(R.id.LastName1_Input);
        apellidoUsuario2 = (EditText) findViewById(R.id.LastName2_Input);
        idCardUsuario = (EditText) findViewById(R.id.IDCARD_Input);
        emailUsuario = (EditText) findViewById(R.id.Email_Input);
        passwordInput01 = (EditText) findViewById(R.id.Password_Input1);
        passwordInput02 = (EditText) findViewById(R.id.Password_Input2);

        //Boton guardar
        Button boton_guardar = (Button) findViewById(R.id.botonGuardar);
        Button boton_cancelar = (Button) findViewById(R.id.botonCancelar);

        //Código del botón guardar
        boton_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //llenado de campos
                userName = nombreUsuario.getText().toString();
                userLastName1 = apellidoUsuario1.getText().toString();
                userLastName2 = apellidoUsuario2.getText().toString();
                userIDCARD = idCardUsuario.getText().toString();
                userEmail = emailUsuario.getText().toString().toLowerCase();
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
                        userDB = new UserDBHelper(getApplicationContext());


                        try {
                            //Comprobación de correo
                            List<String> comprobacion = userDB.recuperarFila(userEmail);
                            String emailcomprobacion = comprobacion.get(posicionCorreo);
                            String usuariocomprobacion = comprobacion.get(posicionUsuario);
                            boolean comprobacionEmail = comprobadorString(userEmail, emailcomprobacion);
                            boolean comprobacionName = comprobadorString(userName, usuariocomprobacion);


                            if ((comprobacionEmail == true)||(comprobacionName==true)) {
                                mensajeCorreoUsuarioExiste();
                                resetCamposTexto();
                            } else {

                                //Ocultar el teclado con la última introducción
                                InputMethodManager inputMag = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                inputMag.hideSoftInputFromWindow(passwordInput02.getWindowToken(), 0);


                                //Ingresa el usuario
                                guardarDatosDB(userName, userLastName1, userLastName2, userIDCARD, userEmail, userPassword, userDB);
                                userDB.close();
                                //Código de actualización base de datos web

                                } //Fin de comprobacion correo
                            } catch (SQLiteException e) {
                                //Código en caso de tabla vacía


                            } catch (IndexOutOfBoundsException me)
                            {
                                //Captura excepción si no existen datos anteriores
                                //Ingresa el usuario
                                guardarDatosDB(userName, userLastName1, userLastName2, userIDCARD, userEmail, userPassword, userDB);
                                userDB.close();
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
                finalizarActividad();
            }
        });


    }

    //cierra la actividad actual
    private void finalizarActividad(){
        //reseteo de los campos llenados
        resetCamposTexto();
        //cierre de actividad
        this.finish();
    }

    //operaciòn de guardado final
    private void guardarDatosDB(String user, String apellido1, String apellido2, String idcard, String email, String password, UserDBHelper usdb){
        usdb.insertarUSUARIO(user, apellido1, apellido2, idcard, email, password);
        usdb.close();
        mensajeOperacionExitosa();
        resetCamposTexto();
        //finaliza actividad
        finalizarActividad();
    }


    //reseteo de los campos de texto
    private  void resetCamposTexto(){
        nombreUsuario.setText("");
        apellidoUsuario1.setText("");
        apellidoUsuario2.setText("");
        idCardUsuario.setText("");
        emailUsuario.setText("");
        passwordInput01.setText("");
        passwordInput02.setText("");
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

    //mensajes de invalidación o alertas
    private void mensajeInvalido() {
        String mensaje = getResources().getString(R.string.mensajeInvalido);
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    private void mensajeNoImplementado() {
        String mensaje = getResources().getString(R.string.mensajeNoImplementado);
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    private void mensajeCorreoUsuarioExiste() {
        String mensaje = getResources().getString(R.string.mensajeNoExisten);
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

    private void mensajePasswordDif(){
        String mensaje = getResources().getString(R.string.passwordNoCoinciden);
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    private void mensajeOperacionExitosa() {
        String mensaje = getResources().getString(R.string.operacionExitosa);
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

}
