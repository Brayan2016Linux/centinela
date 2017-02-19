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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.02
 * @since 2016-05-22 2016-05-26
 */

public class EditCashAdvance extends AppCompatActivity {

    //Widgets
    private EditText DetalleAdelanto;
    private EditText MontoAdelantoInput;
    private DatePicker CalendarioAdelanto;

    //variables por recuperar
    private String NombreTarjeta;
    private String DetalleAdelantoID;
    private String MontoAdelanto;
    private String FechadeAdelanto;
    private String newDate;
    private String idTransaction;

    //Variable Usuario de control
    private String userEmail;

    //Bases de datos
    CashAdvanceDBHelper myCashAdvanceInfo;

    //Clase BuyInfo recuperadora
    CashAdvanceInfo myCashAdvanceActual;

    /*Estructura de myBalanceInfo
        * ID
        * Nombre Tarjeta
        * Saldo Deudor sin Intereses
        * Saldo Deudor tasa Cero
        * Disponible
        * Fecha de registro
   */

    /*Estructura de myCashAdvanceInfo
                * ID
                * Nombre Tarjeta
                * Detalle AdelantoID
                * Monto Adelanto
                * Fecha de Adelanto
                * Fecha de registro
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashadvance);

        //recuperación del correo del usuario de Actividad Login
        Bundle extras = getIntent().getExtras();
        NombreTarjeta = extras.getString(getResources().getString(R.string.cardInfoID));
        idTransaction = extras.getString(getResources().getString(R.string.idAdelN));

        //userID = extras.getString(getResources().getString(R.string.userNumber));
        //Inicialización Base de datos
        myCashAdvanceInfo = new CashAdvanceDBHelper(getApplicationContext());
        myCashAdvanceActual = new CashAdvanceInfo();

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

        //Captura de la información y llenado de widgets
        int idAdelantoNumber = Integer.parseInt(idTransaction);
        myCashAdvanceActual = myCashAdvanceInfo.recuperarAdelanto(idAdelantoNumber);
        myCashAdvanceInfo.close();

        //Captura:
        DetalleAdelantoID = myCashAdvanceActual.obtenerAdelantoDetalle();
        MontoAdelanto = Double.toString(myCashAdvanceActual.obtenerMontoAdelanto());
        FechadeAdelanto = myCashAdvanceActual.obtenerFechaAdelanto();

        //Seteo a la fecha
        /*
        int date[] = recuperacionFecha(FechadeAdelanto);

        int day = date[0];
        int month = date[1];
        int year = date[2];
        */
        Date mydate = recuperacionFechaDate(FechadeAdelanto);
        Calendar my_calendar = Calendar.getInstance();
        my_calendar.setTime(mydate);

        int day = my_calendar.get(Calendar.DATE);
        int month = my_calendar.get(Calendar.MONTH);
        int year = my_calendar.get(Calendar.YEAR);

        Log.d(null, "Date recovered " + day + "-" + month + "-" + year);

        CalendarioAdelanto.updateDate(year, month, day);

        //Seteo de campos
        DetalleAdelanto.setText(DetalleAdelantoID);

        //Formateo a dos Decimales
        DecimalFormat myformat = new DecimalFormat("0.00");
        MontoAdelantoInput.setText(myformat.format(Double.parseDouble(MontoAdelanto)));


        //Boton de Guardar
        Button GuardarBTN = (Button) findViewById(R.id.buttonOK);
        GuardarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //Obtener los datos desde los widgets
                    DetalleAdelantoID = DetalleAdelanto.getText().toString();
                    MontoAdelanto = MontoAdelantoInput.getText().toString();

                    //formateo de comas a punto decimal
                    MontoAdelanto = formateoDecimal(MontoAdelanto);

                    //Captura de datos del DatePicker
                    int day = CalendarioAdelanto.getDayOfMonth();
                    int month = CalendarioAdelanto.getMonth();
                    int year = CalendarioAdelanto.getYear();
                    //newDate = formatoFecha(day, month, year);

                    //Captura de fecha
                    Calendar mydate = Calendar.getInstance();
                    mydate.set(year, month, day);
                    Long dateLong = mydate.getTimeInMillis();
                    newDate = dateLong.toString();

                    FechadeAdelanto = newDate;


                    boolean comprobador = longitudString(DetalleAdelantoID) &&
                            longitudString(MontoAdelanto) &&
                            longitudString(FechadeAdelanto);

                    if (comprobador) {
                        guardarDatosAdelantoActualizados(idTransaction,
                                NombreTarjeta,
                                DetalleAdelantoID,
                                MontoAdelanto,
                                FechadeAdelanto);

                        abrirActividadControl(NombreTarjeta);

                    } else {
                        mensajeCompleteCampos();
                    }


                } catch (NumberFormatException myexception) {
                    mensajeDatosEquivocados();
                } catch (IllegalArgumentException myexc) {
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
    private boolean longitudString(String stringInput) {
        boolean resultado;
        if (stringInput.trim().length() > 0)
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
    private void mensajeDatosEquivocados() {
        Toast.makeText(getApplicationContext(), getResources().getText(R.string.mensajeExcepcionTipo), Toast.LENGTH_SHORT).show();
    }


    //cierra la actividad actual
    private void finalizarActividad() {
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
    //recuperación de la fecha en números dado un String de fecha
    private int[] recuperacionFecha(String Fecha){
        int date[]={0,0,0};

        StringBuffer aniobuffer = new StringBuffer(4);
        StringBuffer mesbuffer = new StringBuffer (2);
        StringBuffer diabuffer = new StringBuffer (2);

        for (int i = 6; i < Fecha.length(); i++){
            aniobuffer.append(Fecha.charAt(i));
        }

        mesbuffer.append(Fecha.charAt(3));
        mesbuffer.append(Fecha.charAt(4));

        diabuffer.append(Fecha.charAt(0));
        diabuffer.append(Fecha.charAt(1));

        date[2] = Integer.parseInt(aniobuffer.toString());
        date[1] = Integer.parseInt(mesbuffer.toString());
        date[0] = Integer.parseInt(diabuffer.toString());


        return date;
    }

    //**********************************************************************************************
    //recuperación de fecha en Date
    private Date recuperacionFechaDate(String FechaLong) {
        Long my_date_long = Long.parseLong(FechaLong);

        Date mydate = new Date(my_date_long);

        return mydate;
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
    private void guardarDatosAdelantoActualizados(String idtransaction,
                                              String nombretarjeta,
                                              String detalleadelantoID,
                                              String montoadelantoactual,
                                              String fechadeadelanto) {

        //conversión a double
        int idadelantonumero = Integer.parseInt(idtransaction);
        double montoadelantoTotal = Double.parseDouble(montoadelantoactual);

        //Inicializador base de datos
        CashAdvanceDBHelper myCashAdvance = new CashAdvanceDBHelper(getApplicationContext());

        //actualización de la base de datos
        myCashAdvance.actualizarADELANTO(idadelantonumero, nombretarjeta, detalleadelantoID,  montoadelantoTotal, fechadeadelanto);
        myCashAdvance.close();

        mensajeOperacionExitosa();
        finalizarActividad();

    }

    //Abre actividad correspondiente al Control de Pagos y Compras Actualizado
    private void abrirActividadControl(String nombreTarjeta) {
        Intent miActividad = new Intent(this, CashAdvanceControlActivity.class);
        miActividad.putExtra(getResources().getString(R.string.cardInfoID), nombreTarjeta);
        startActivity(miActividad);

        finalizarActividad();
    }


}
