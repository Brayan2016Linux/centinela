package com.example.imagineup.centinela;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.02
 * @since 2016-03-22 2016-05-26
 */

public class CashAdvanceActivity extends AppCompatActivity {


    //Widgets
    private EditText DetalleAdelanto;
    private EditText MontoAdelantoInput;
    private DatePicker CalendarioAdelanto;

    //variables

    private String NombreTarjeta;
    private String DetalleAdelantoID;
    private String MontoAdelanto;
    private String FechadeAdelanto;
    private String newDate;

    //Variable Usuario de control
    private String userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashadvance);

        //recuperación del correo del usuario de Actividad Login
        Bundle extras = getIntent().getExtras();
        NombreTarjeta = extras.getString(getResources().getString(R.string.cardInfoID));

        //userID = extras.getString(getResources().getString(R.string.userNumber));


        //Widgets de EditTexto
        DetalleAdelanto = (EditText) findViewById(R.id.detalleAdelantoINP);
        MontoAdelantoInput = (EditText) findViewById(R.id.MontoAdelantoINP);

        //Código para la fecha actual
        Calendar mydatetoday = Calendar.getInstance();
        SimpleDateFormat mydateformat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String curDate = mydateformat.format(mydatetoday.getTime());
        Log.d("CURR_DATE:", curDate);

        //Código de Calendar View en caso de seleccionar:
        CalendarioAdelanto = (DatePicker) findViewById(R.id.fecha_Adelanto);

        //Boton de Guardar
        Button GuardarBTN = (Button) findViewById(R.id.buttonOK);
        GuardarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //Obtener los datos desde los widgets
                    DetalleAdelantoID = DetalleAdelanto.getText().toString();
                    MontoAdelanto = MontoAdelantoInput.getText().toString();
                    MontoAdelanto = formateoDecimal(MontoAdelanto);

                    //Cambio de fecha por parte del usuario
                    int day = CalendarioAdelanto.getDayOfMonth();
                    int month = CalendarioAdelanto.getMonth();
                    int year = CalendarioAdelanto.getYear();
                    //newDate = formatoFecha(day,month, year);

                    //Fecha seleccionada
                    Calendar mydate = Calendar.getInstance();
                    mydate.set(year, month, day);
                    Long dateLong = mydate.getTimeInMillis();
                    newDate = dateLong.toString();

                    FechadeAdelanto = newDate;

                    boolean comprobador = longitudString(DetalleAdelantoID) &&
                            longitudString(MontoAdelanto) &&
                            longitudString(FechadeAdelanto);

                    if (comprobador) {
                        guardarDatos( NombreTarjeta,
                                DetalleAdelantoID,
                                MontoAdelanto,
                                FechadeAdelanto);

                        abrirActividadControl(NombreTarjeta);

                    } else {
                        mensajeCompleteCampos();
                    }

                }
                catch(NumberFormatException myexception){
                    mensajeDatosEquivocados();
                }
                catch (IllegalArgumentException myexc){
                    mensajeDatosEquivocados();
                }
            }

        });


        //Botón de Cancelar
        Button CancelBTN = (Button) findViewById(R.id.button_CNL);
        CancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirActividadControl(NombreTarjeta);
            }
        });


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

    //Mensaje para llenado de campos vacíos
    private void mensajeCompleteCampos() {
        String mensaje = getResources().getString(R.string.completeCamposVacios);
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    //Mensaje de operación exitosa
    private void mensajeOperacionExitosa() {
        String mensaje = getResources().getString(R.string.operacionExitosa);
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    //Mensaje datos equivocados
    private void mensajeDatosEquivocados(){
        Toast.makeText(getApplicationContext(),getResources().getText(R.string.mensajeExcepcionTipo),Toast.LENGTH_SHORT).show();
    }


    //cierra la actividad actual
    private void finalizarActividad(){
        //cierre de actividad
        this.finish();
    }

    //**********************************************************************************************
    private String formatoFecha(int day, int month, int year) {
        String newdate;

        //Fecha seleccionada
        //Dia
        if (day/10 < 1){
            newdate = "0" + day;
        }
        else {
            newdate = "" + day;
        }
        //mes y año
        if (month/10 < 1 ) {
            newdate = newdate + "-0" + month + "-" + year;
            Log.d("NewDate", newdate);
        }
        else
        {
            newdate = newdate + "-" + month + "-" + year;
            Log.d("NewDate", newdate);
        }

        return newdate;
    }
    //**********************************************************************************************

    //formateo de captura de comas a punto decimal
    private String formateoDecimal(String numeroDecimal){
        String formatoDevuelto;
        StringBuffer myBuffer = new StringBuffer();

        for (int i = 0; i < numeroDecimal.length(); i++){
            if (numeroDecimal.charAt(i) == ','){
                myBuffer.append('.');
            }
            else {
                myBuffer.append(numeroDecimal.charAt(i));
            }
        }
        formatoDevuelto = myBuffer.toString();

        return formatoDevuelto;
    }

    //Guardar datos en la base de datos
    private void guardarDatos(String nombretarjeta,
                              String detalleadelantoID,
                              String montoadelanto,
                              String fechadeadelanto){

        //conversión a double
        double montoadelantoTotal = Double.parseDouble(montoadelanto);

        //Inicializador base de datos
        CashAdvanceDBHelper myCashAdvance = new CashAdvanceDBHelper(getApplicationContext());
        BalanceDBHelper myCardBalance = new BalanceDBHelper(getApplicationContext());

        myCashAdvance.insertarADELANTO(nombretarjeta, detalleadelantoID, montoadelantoTotal, fechadeadelanto);
        myCashAdvance.close();

        mensajeOperacionExitosa();
        finalizarActividad();

    }


    //Abre actividad correspondiente al Control de Pagos, Compras, Adelantos Actualizado
    private void abrirActividadControl(String nombreTarjeta){
        Intent miActividad = new Intent(this, CashAdvanceControlActivity.class);
        miActividad.putExtra(getResources().getString(R.string.cardInfoID), nombreTarjeta);
        startActivity(miActividad);

        finalizarActividad();
    }


}