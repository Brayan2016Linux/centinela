package com.example.imagineup.centinela;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.02
 * @since 2016-03-08 2016-05-26
 */

public class EditBuy extends AppCompatActivity {


    //Widgets
    private EditText DetalleCompra;
    private EditText MontoTotalCompra;
    private Spinner SpinnerPeriodoCredito;
    private Spinner SpinnerModalidadCompra;
    private DatePicker CalendarioCompra;

    //variables por recuperar
    private String NombreTarjeta;
    private String idTransaction;
    private String DetalleCompraID;
    private String MontoCompra;
    private String periodoCompra;
    private String modalidadCompra;
    private String FechadeCompra;
    private String newDate;

    private String montoCompraAnterior;

    //Variable Usuario de control
    private String userEmail;

    //Bases de datos
    BuyDBHelper myBuyInfo;

    //Clase BuyInfo recuperadora
    BuyInfo mybuyActual;

    /*Estructura de myBalanceInfo
        * ID
        * Nombre Tarjeta
        * Saldo Deudor sin Intereses
        * Saldo Deudor tasa Cero
        * Disponible
        * Fecha de registro*/

    /*Estructura de myBuyInfo
        * ID
        * Detalle compra
        * Nombre Tarjeta
        * Monto de Compra
        * periodo de compra
        * modalidad de compra
        * fecha de compra
        * fecha de registro
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        //recuperación del correo del usuario de Actividad Login
        Bundle extras = getIntent().getExtras();
        NombreTarjeta = extras.getString(getResources().getString(R.string.cardInfoID));
        idTransaction = extras.getString(getResources().getString(R.string.idCompN));

        //userID = extras.getString(getResources().getString(R.string.userNumber));
        //Inicialización Base de datos
        myBuyInfo = new BuyDBHelper(getApplicationContext());
        mybuyActual = new BuyInfo();

        //Widgets de EditTexto
        DetalleCompra = (EditText) findViewById(R.id.detalleCompraInput);
        MontoTotalCompra = (EditText) findViewById(R.id.MontoCompra);

        //Código para la fecha actual
        Calendar mydatetoday = Calendar.getInstance();
        SimpleDateFormat mydateformat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String curDate = mydateformat.format(mydatetoday.getTime());
        Log.d("CURR_DATE:", curDate);

        //Código de Calendar View en caso de seleccionar:
        CalendarioCompra = (DatePicker) findViewById(R.id.fecha_Compra);

        //Captura de la información y llenado de widgets
        int idCompra = Integer.parseInt(idTransaction);
        mybuyActual = myBuyInfo.recuperarCompra(idCompra);

        //Captura:
        DetalleCompraID = mybuyActual.obtenerCompraID();
        MontoCompra = Double.toString(mybuyActual.obtenerMontoCredito());
        montoCompraAnterior = MontoCompra;
        FechadeCompra = mybuyActual.obtenerFechaCompra();
        periodoCompra = Integer.toString(mybuyActual.obtenerPeriodoCredito());
        modalidadCompra = mybuyActual.obtenerModalidad();

        //Inicialización de los Spinners
        CreacionSpinnerCompraActualizados(periodoCompra, modalidadCompra);

        //Seteo a la fecha
        /*
        int date[] = recuperacionFecha(FechadeCompra);

        int day = date[0];
        int month = date[1];
        int year = date[2];
        */
        Date mydate = recuperacionFechaDate(FechadeCompra);
        Calendar my_calendar = Calendar.getInstance();
        my_calendar.setTime(mydate);

        int day = my_calendar.get(Calendar.DATE);
        int month = my_calendar.get(Calendar.MONTH);
        int year = my_calendar.get(Calendar.YEAR);

        //para probar el algoritmo de la fecha
        Log.d(null, "Date recovered " + day + "-" + month + "-" + year);

        CalendarioCompra.updateDate(year, month, day);

        //Seteo de Campos
        DetalleCompra.setText(DetalleCompraID);

        //Formateo a dos Decimales
        DecimalFormat myformat = new DecimalFormat("0.00");
        MontoTotalCompra.setText(myformat.format(Double.parseDouble(MontoCompra)));

        //Boton de Guardar
        Button GuardarBTN = (Button) findViewById(R.id.buttonOK);
        GuardarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //Obtener los datos desde los widgets
                    DetalleCompraID = DetalleCompra.getText().toString();
                    MontoCompra = MontoTotalCompra.getText().toString();
                    periodoCompra = Integer.toString(SpinnerPeriodoCredito.getSelectedItemPosition());
                    modalidadCompra = Integer.toString(SpinnerModalidadCompra.getSelectedItemPosition());

                    //Conversión de Monto de Compras a formato de comas a punto
                    MontoCompra = formateoDecimal(MontoCompra);

                    //Captura de datos del DatePicker
                    int day = CalendarioCompra.getDayOfMonth();
                    int month = CalendarioCompra.getMonth();
                    int year = CalendarioCompra.getYear();
                    //newDate = formatoFecha(day, month, year);

                    //Fecha seleccionada
                    Calendar mydate = Calendar.getInstance();
                    mydate.set(year, month, day);
                    Long dateLong = mydate.getTimeInMillis();
                    newDate = dateLong.toString();

                    FechadeCompra = newDate;


                    boolean comprobador = longitudString(DetalleCompraID) &&
                            longitudString(MontoCompra) &&
                            longitudString(FechadeCompra);

                    if (comprobador) {
                        guardarDatosCompraActualizados(idTransaction,
                                NombreTarjeta,
                                DetalleCompraID,
                                MontoCompra,
                                montoCompraAnterior,
                                periodoCompra,
                                modalidadCompra,
                                FechadeCompra);

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

    //posición Modalidad
    private int devolucionPosicionModalidad (String modalidad) {
        int posicionModalidad;

        if (modalidad.equals(getResources().getString(R.string.interes_INT))){
            posicionModalidad = 0;

        } else
        {
            posicionModalidad = 1;
        }

        return  posicionModalidad;
    }

    //posicion periodo
    private int devolucionPosicionPeriodoCredito (String periodoCredito){
        int posicionPeriodo;

        switch (Integer.parseInt(periodoCredito)) {
            case 2:
                posicionPeriodo = 1;
                break;

            case 3:
                posicionPeriodo = 2;
                break;

            case 4:
                posicionPeriodo = 3;
                break;

            case 5:
                posicionPeriodo = 4;
                break;

            case 6:
                posicionPeriodo = 5;
                break;

            case 12:
                posicionPeriodo = 6;
                break;

            case 24:
                posicionPeriodo = 7;
                break;

            case 36:
                posicionPeriodo = 8;
                break;

            default:
                posicionPeriodo = 0;
                break;

        }

        return posicionPeriodo;
    }

    //**********************************************************************************************

    //Llenado de los Spinners
    private void CreacionSpinnerCompraActualizados(String periodocred, String modalidadcompra) {

        int periodo = devolucionPosicionPeriodoCredito(periodocred);
        SpinnerPeriodoCredito = (Spinner) findViewById(R.id.Spinner_periodoCredito);
        ArrayAdapter spinner_adapter_PC = ArrayAdapter.createFromResource(this, R.array.spinner_periodos, android.R.layout.simple_list_item_1);
        spinner_adapter_PC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerPeriodoCredito.setAdapter(spinner_adapter_PC);
        SpinnerPeriodoCredito.setSelection(periodo);

        int modalidad = devolucionPosicionModalidad(modalidadcompra);
        SpinnerModalidadCompra = (Spinner) findViewById(R.id.Spinner_modalidadCompra);
        ArrayAdapter spinner_adapter_MC = ArrayAdapter.createFromResource(this, R.array.spinner_modalidad, android.R.layout.simple_list_item_1);
        spinner_adapter_MC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerModalidadCompra.setAdapter(spinner_adapter_MC);
        SpinnerModalidadCompra.setSelection(modalidad);

    }

    //Devolución del String Modalidad
    private String devolucionModalidad(String posicionModalidad) {
        String modalidad;

        if (posicionModalidad.equals("0")){
            modalidad = getResources().getString(R.string.interes_INT);
        }
        else {
            modalidad = getResources().getString(R.string.interes_CERO);
        }

        return modalidad;
    }

    //Devolución del String Período
    private int devolucionPeriodo (int posicionPeriodo) {
        int periodo;

        switch (posicionPeriodo) {
            case 0:
                periodo = 1;
                break;

            case 1:
                periodo = 2;
                break;

            case 2:
                periodo = 3;
                break;

            case 3:
                periodo = 4;
                break;

            case 4:
                periodo = 5;
                break;

            case 5:
                periodo = 6;
                break;

            case 6:
                periodo = 12;
                break;

            case 7:
                periodo = 24;
                break;

            case 8:
                periodo = 36;
                break;

            default:
                periodo = 0;
                break;
        }

        return periodo;
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
    private void guardarDatosCompraActualizados(String idtransaction,
                                    String nombretarjeta,
                                    String detallecompraID,
                                    String montocompraactual,
                                    String montocompraanterior,
                                    String periodocompra,
                                    String modalidadcompra,
                                    String fechadecompra) {

        //conversión a double
        int idCompra = Integer.parseInt(idtransaction);
        double montocompraTotal = Double.parseDouble(montocompraactual);
        int periodoID = Integer.parseInt(periodocompra);

        //Inicializador base de datos
        BuyDBHelper myBuy = new BuyDBHelper(getApplicationContext());

        //actualización de la base de datos
        myBuy.actualizarCOMPRA(idCompra,
                               detallecompraID,
                               nombretarjeta,
                               montocompraTotal,
                               devolucionPeriodo(periodoID),
                               devolucionModalidad(modalidadcompra),
                               fechadecompra);
        myBuy.close();

        mensajeOperacionExitosa();
        finalizarActividad();

    }


    //Abre actividad correspondiente al Control de Pagos y Compras Actualizado
    private void abrirActividadControl(String nombreTarjeta) {
        Intent miActividad = new Intent(this, BuyControlActivity.class);
        miActividad.putExtra(getResources().getString(R.string.cardInfoID), nombreTarjeta);
        startActivity(miActividad);

        finalizarActividad();
    }


}
