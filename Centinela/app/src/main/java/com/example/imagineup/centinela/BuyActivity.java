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

public class BuyActivity extends AppCompatActivity {


    //Widgets
    private EditText DetalleCompra;
    private EditText MontoTotalCompra;
    private Spinner SpinnerPeriodoCredito;
    private Spinner SpinnerModalidadCompra;
    private DatePicker CalendarioCompra;

    //variables a recuperar
    private String NombreTarjeta;
    private String DetalleCompraID;
    private String MontoCompra;
    private String periodoCompra;
    private String modalidadCompra;
    private String FechadeCompra;
    private String newDate;

    //Variable Usuario de control
    private String userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        //recuperación del correo del usuario de Actividad Login
        Bundle extras = getIntent().getExtras();
        NombreTarjeta = extras.getString(getResources().getString(R.string.cardInfoID));

        //userID = extras.getString(getResources().getString(R.string.userNumber));

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

        //Inicialización de los Spinners
        CreacionSpinnerCompra();

        //Boton de Guardar
        Button GuardarBTN = (Button) findViewById(R.id.buttonOK);
        GuardarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //Obtener los datos desde los widgets
                    DetalleCompraID = DetalleCompra.getText().toString();
                    MontoCompra = MontoTotalCompra.getText().toString();
                    MontoCompra = formateoDecimal(MontoCompra);
                    periodoCompra = Integer.toString(SpinnerPeriodoCredito.getSelectedItemPosition());
                    modalidadCompra = Integer.toString(SpinnerModalidadCompra.getSelectedItemPosition());

                    //Cambio de fecha por parte del usuario
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
                        guardarDatosCompra(NombreTarjeta,
                                DetalleCompraID,
                                MontoCompra,
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

    //Llenado de los Spinners
    private void CreacionSpinnerCompra() {

        SpinnerPeriodoCredito = (Spinner) findViewById(R.id.Spinner_periodoCredito);
        ArrayAdapter spinner_adapter_PC = ArrayAdapter.createFromResource(this, R.array.spinner_periodos, android.R.layout.simple_list_item_1);
        spinner_adapter_PC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerPeriodoCredito.setAdapter(spinner_adapter_PC);

        SpinnerModalidadCompra = (Spinner) findViewById(R.id.Spinner_modalidadCompra);
        ArrayAdapter spinner_adapter_MC = ArrayAdapter.createFromResource(this, R.array.spinner_modalidad, android.R.layout.simple_list_item_1);
        spinner_adapter_MC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SpinnerModalidadCompra.setAdapter(spinner_adapter_MC);

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
    private void guardarDatosCompra(String nombretarjeta,
                              String detallecompraID,
                              String montocompra,
                              String periodocompra,
                              String modalidadcompra,
                              String fechadecompra) {

        //conversión a double
        double montocompraTotal = Double.parseDouble(montocompra);
        int periodoID = Integer.parseInt(periodocompra);
        String periodoSID = periodocompra;

        //Inicializador base de datos
        BuyDBHelper myBuy = new BuyDBHelper(getApplicationContext());
        BalanceDBHelper myCardBalance = new BalanceDBHelper(getApplicationContext());

        myBuy.insertarCOMPRA(detallecompraID,
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
