package com.example.imagineup.centinela;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.02
 * @since 2016-03-07 2016-03-08
 */

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;


public class PayControlActivity extends AppCompatActivity {

    private PayDBHelper myPayInfo;
    private BuyDBHelper myBuyInfo;
    private String nombreTarjetaCNT;
    private ListView myListViewPay;
    private TextView myTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_control);

        //recuperación del correo y el número de usuario desde Actividad Login
        Bundle extras = getIntent().getExtras();
        nombreTarjetaCNT = extras.getString(getResources().getString(R.string.cardInfoID));

        myTitleView = (TextView) findViewById(R.id.etiquetatarjetaTitulo);
        myTitleView.setText(nombreTarjetaCNT);

        crearListViewPay(nombreTarjetaCNT);

    }

    //*****************************************************************************************


    //Agregar o Inflar la Action Bar
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pagos, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Agrega Acción a cada uno de los items del menu ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId(); //captura el id del menú seleccionado

        //para cada item un if en cada uno se coloca el código
        switch (id) {
            //Abre menu contextual para elegir borrar pago o compras
            case R.id.delete_all:
                metodoDELETEALL(nombreTarjetaCNT);
                return true;

            //Abre menún contextual para elegir agregar pago o compra
            case R.id.add_all:
                metodoADD(nombreTarjetaCNT);
                return true;

            //Devuelve a la actividad principal
            case R.id.close_all:
                metodoCLOSE();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //******************************************************************************************

    //agregar un elemento a lista
    private void metodoADD(String NombreTarjeta){
        //cuando un elemento agregar ha sido seleccionado
        abrirActividadPago(NombreTarjeta);
    }

    //eliminar todos los registros
    private void metodoDELETEALL(String NombreTarjeta) {
        //borrado completo del listado
        cuadroDialogoBorradoPagoDB(NombreTarjeta, true, null);

    }

    //cierra la actividad y devuelve el control a la actividad principal
    private void metodoCLOSE() {
        cerrarActividad();
    }


    //******************************************************************************************

    //Mensaje No implementado
    private void mensajeNoImplementado() {
        String mensaje = getResources().getString(R.string.mensajeNoImplementado);
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }


    //******************************************************************************************

    //Abre actividad correspondiente para registro de Pago
    private void abrirActividadPago(String TarjetaControl){
        Intent miActividad = new Intent(this, PaymentActivity.class);
        miActividad.putExtra(getResources().getString(R.string.cardInfoID), TarjetaControl);
        startActivity(miActividad);
        cerrarActividad();
    }


    //cierra la actividad
    private void cerrarActividad(){
        this.finish();
    }

    //abrir actividad acerca de
    private void abrirAboutTO(){
        Intent miActividad = new Intent(this, AboutTo.class);
        startActivity(miActividad);
    }

    //abrir actividad editar un pago
    private void editarPago(String idPago, String nombreTarjeta){
        Intent miActividad = new Intent(this, EditPayment.class);
        miActividad.putExtra(getResources().getString(R.string.idPagoN), idPago);
        miActividad.putExtra(getResources().getString(R.string.cardInfoID),nombreTarjeta);
        startActivity(miActividad);
        cerrarActividad();
    }


    //******************************************************************************************

    //Creación del ListView Pagos

    private void crearListViewPay(String cardName) {

        //obtener lista de la base de datos
        myPayInfo = new PayDBHelper(this);
        myPayInfo.getReadableDatabase();

        try {

            //Carga todos los registros de la base de datos a la ListView
            //Cursor myCursor = myCardInfo.fetchAllRegister();
            Cursor myCursor = myPayInfo.fetchARegisterbyCardName(cardName);

            if (myCursor != null) {

                //definición de columnas

                /*Estructura de myPayInfo
                * ID
                * Nombre Tarjeta
                * Detalle PagoID
                * Monto
                * Fecha de Pago
                * Fecha de registro*/


                String[] columns = new String[]{
                        //PayDBHelper.COLUMN_ID,
                        PayDBHelper.COLUMN_DETALLEPAGO,
                        //PayDBHelper.COLUMN_TARJETA,
                        PayDBHelper.COLUMN_MONTO,
                        PayDBHelper.COLUMN_FECHAPAGOFORM
                };

                //definición de los campos en el XML
                int[] destiny = new int[]{
                        R.id.detallePagoInfo,
                        R.id.detallePagoMonto,
                        R.id.detalleFecha_Pago
                };

                //creación del adapter por fuerza columna id debe ser denominada "_id"
                SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.pay_info,
                        myCursor, columns, destiny, 0);

                //ListView creación y asignación del adapter
                myListViewPay = (ListView) findViewById(R.id.myPay_List);
                myListViewPay.setAdapter(dataAdapter);

                //para registrar Views y cada item tenga un menu contextual
                registerForContextMenu(myListViewPay);

                //Al seleccionar un item de la lista
                myListViewPay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Cursor myCursor = (Cursor) myListViewPay.getItemAtPosition(position);
                        //recupera el nombre de la tarjeta:
                        String nombreTarjeta = myCursor.getString(myCursor.getColumnIndexOrThrow(PayDBHelper.COLUMN_TARJETA));
                        String idPago = myCursor.getString(myCursor.getColumnIndexOrThrow(PayDBHelper.COLUMN_ID));
                        nombreTarjetaCNT = nombreTarjeta;
                        //Toast.makeText(getApplicationContext(), nombreTarjeta,Toast.LENGTH_SHORT).show();
                        cuadroDialogoMenuContextualPago(idPago, nombreTarjetaCNT);

                    }
                });
            }
            else
            {
                //Si el cursor está vacío
            }

        }
        catch (SQLiteException e) {

        }

    }

    //******************************************************************************************
    private  void cuadroDialogoMenuContextualPago(final String idPago, final String NombreTarjeta) {
        //creación de un dialogo personalizado
        LayoutInflater fabrica = LayoutInflater.from(this);
        final View newDialogView = fabrica.inflate(R.layout.dialog_paybuy, null);

        final AlertDialog myDialog = new AlertDialog.Builder(this).create();
        myDialog.setView(newDialogView);

        //Editar
        newDialogView.findViewById(R.id.Edit_contextual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(), Nombre,Toast.LENGTH_SHORT).show();
                editarPago(idPago, NombreTarjeta);
                myDialog.dismiss();
            }
        });


        //Eliminar
        newDialogView.findViewById(R.id.Delete_contextual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), Nombre,Toast.LENGTH_SHORT).show();
                cuadroDialogoBorradoPagoDB(NombreTarjeta, false, idPago);
                revertirBalanceSaldos(NombreTarjeta, idPago);
                myDialog.dismiss();
            }
        });

        myDialog.show();
    }

    //******************************************************************************************
    //Actualiza los balances de Saldos y revierte al borrar un pago
    private void revertirBalanceSaldos(String nombreTarjeta, String idPago) {
        //código de actualización
        PayDBHelper myPayment = new PayDBHelper(getApplicationContext());
        BalanceDBHelper myBalanceInfo = new BalanceDBHelper(getApplicationContext());
        PayInfo myActualPayment = new PayInfo();
        CardBalanceInfo myBalance = new CardBalanceInfo();

        myActualPayment = myPayment.recuperarPago(Integer.parseInt(idPago));
        myBalance = myBalanceInfo.recuperarNombreTarjeta(nombreTarjeta);

        /*Estructura de myBalanceInfo
        * ID
        * Nombre de Tarjeta
        * Saldo deudor INT
        * Saldo deudor CERO
        * Monto disponible
        * Fecha_registro*/

        int idtransaccion;
        double SaldoDeudorInt;
        double SaldoDeudorCero;
        double MontoDisponible;
        double montoPagoActual;
        double newSaldoDeudorInt;
        double newSaldoDeudorCero;
        double newSaldoDisponible;

        montoPagoActual = myActualPayment.obtenerMontoPago();

        idtransaccion = myBalance.obtenerIDTransaction();
        SaldoDeudorInt = myBalance.obtenerSaldoDeudorINT();
        SaldoDeudorCero = myBalance.obtenerSaldoDeudorCEROINT();
        MontoDisponible = myBalance.obtenerMontoDisponible();


        //Falta crear la fórmula de distribución de pago
        //Para pruebas se dividirá equitativamente entre tres

        newSaldoDeudorCero = SaldoDeudorCero + montoPagoActual/3;
        newSaldoDeudorInt = SaldoDeudorInt + montoPagoActual/3;
        newSaldoDisponible = MontoDisponible - montoPagoActual/3;


        //actualización de la base de datos Saldo Disponible
        myBalanceInfo.actualizarSALDO(idtransaccion, nombreTarjeta, newSaldoDeudorInt, newSaldoDeudorCero, newSaldoDisponible);
        myBalanceInfo.close();


    }


    //******************************************************************************************
    private void cuadroDialogoBorradoPagoDB(final String nombreTarjeta, final boolean complete, final String idTransaction){

        //creación de un dialogo personalizado
        LayoutInflater fabrica = LayoutInflater.from(this);
        final View borrarDialogView = fabrica.inflate(R.layout.dialog_delete, null);

        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(borrarDialogView);

        borrarDialogView.findViewById(R.id.buttonOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (complete == true) {
                        myPayInfo.DeleteAllRegisterIdentified(nombreTarjeta);
                }
                else
                {
                    //encuentra el entero del registro y lo pasa a la variable control
                    int enteroRegistro;
                    enteroRegistro = Integer.parseInt(idTransaction);
                    //elimina el registro y reorganiza la base de datos
                    myPayInfo.deleteEntry(enteroRegistro);
                    myPayInfo.VacuumSQLiteDB();

                }

                myPayInfo.close();

                //refresca ventana
                startActivity(getIntent());
                finish();

                //cierra el cuadro de dialogo
                deleteDialog.dismiss();
            }
        });
        borrarDialogView.findViewById(R.id.buttonCNL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //cierra el cuadro de dialogo
                deleteDialog.dismiss();
            }
        });

        deleteDialog.show();
    }
}
