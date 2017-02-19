package com.example.imagineup.centinela;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.01
 * @since 2016-05-22
 */

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CashAdvanceControlActivity extends AppCompatActivity {

    private CashAdvanceDBHelper myCashAdvanceInfo;
    private String nombreTarjetaCNT;
    private ListView myListViewCashAdvance;
    private TextView myTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cashadvance_control);

        //recuperación del correo y el número de usuario desde Actividad Login
        Bundle extras = getIntent().getExtras();
        nombreTarjetaCNT = extras.getString(getResources().getString(R.string.cardInfoID));

        myTitleView = (TextView) findViewById(R.id.etiquetatarjetaTitulo);
        myTitleView.setText(nombreTarjetaCNT);

        crearListViewCashAdvance(nombreTarjetaCNT);

    }

    //*****************************************************************************************


    //Agregar o Inflar la Action Bar
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adelanto, menu);
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
        abrirActividadAdelanto(NombreTarjeta);
    }

    //eliminar todos los registros
    private void metodoDELETEALL(String NombreTarjeta) {
        //borrado completo del listado
        cuadroDialogoBorradoAdelantoDB(NombreTarjeta, true, null);

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

    //Abre actividad correspondiente para registro de Adelanto
    private void abrirActividadAdelanto(String TarjetaControl){
        Intent miActividad = new Intent(this, CashAdvanceActivity.class);
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
    private void editarAdelanto(String idAdelanto, String nombreTarjeta){
        Intent miActividad = new Intent(this, EditCashAdvance.class);
        miActividad.putExtra(getResources().getString(R.string.idAdelN), idAdelanto);
        miActividad.putExtra(getResources().getString(R.string.cardInfoID),nombreTarjeta);
        startActivity(miActividad);
        cerrarActividad();
    }


    //******************************************************************************************

    //Creación del ListView CashAdvance

    private void crearListViewCashAdvance(String cardName) {

        //obtener lista de la base de datos
        myCashAdvanceInfo = new CashAdvanceDBHelper(this);
        myCashAdvanceInfo.getReadableDatabase();

        try {

            //Carga todos los registros de la base de datos a la ListView
            //Cursor myCursor = myCardInfo.fetchAllRegister();
            Cursor myCursor = myCashAdvanceInfo.fetchARegisterbyCardName(cardName);

            if (myCursor != null) {

                //definición de columnas

                /*Estructura de myCashAdvanceInfo
                * ID
                * Nombre Tarjeta
                * Detalle AdelantoID
                * Monto Adelanto
                * Fecha de Adelanto
                * Fecha de registro*/


                String[] columns = new String[]{
                        //CashAdvanceDBHelper.COLUMN_ID,
                        CashAdvanceDBHelper.COLUMN_IDADELANTO,
                        //CashAdvanceDBHelper.COLUMN_TARJETA,
                        CashAdvanceDBHelper.COLUMN_ADELANTO_MONTO,
                        CashAdvanceDBHelper.COLUMN_FECHA_ADELANTOFORM
                };

                //definición de los campos en el XML
                int[] destiny = new int[]{
                        R.id.detalleAdelantoInfo,
                        R.id.detalleAdelantoMonto,
                        R.id.detalleFecha_Adelanto
                };

                //creación del adapter por fuerza columna id debe ser denominada "_id"
                SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.cashadvance_info,
                        myCursor, columns, destiny, 0);

                //ListView creación y asignación del adapter
                myListViewCashAdvance = (ListView) findViewById(R.id.my_AdvanceList);
                myListViewCashAdvance.setAdapter(dataAdapter);

                //para registrar Views y cada item tenga un menu contextual
                registerForContextMenu(myListViewCashAdvance);

                //Al seleccionar un item de la lista
                myListViewCashAdvance.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Cursor myCursor = (Cursor) myListViewCashAdvance.getItemAtPosition(position);
                        //recupera el nombre de la tarjeta:
                        String nombreTarjeta = myCursor.getString(myCursor.getColumnIndexOrThrow(CashAdvanceDBHelper.COLUMN_TARJETA));
                        String idAdelanto = myCursor.getString(myCursor.getColumnIndexOrThrow(CashAdvanceDBHelper.COLUMN_ID));
                        nombreTarjetaCNT = nombreTarjeta;
                        //Toast.makeText(getApplicationContext(), nombreTarjeta,Toast.LENGTH_SHORT).show();
                        cuadroDialogoMenuContextualAdelanto(idAdelanto, nombreTarjetaCNT);

                    }
                });
            }
            else
            {
                //Si el cursor está vacío
                Log.d("Cursor", "null");
            }

        }
        catch (SQLiteException e) {

        }

    }

    //******************************************************************************************
    private  void cuadroDialogoMenuContextualAdelanto(final String idAdelanto, final String NombreTarjeta) {
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
                editarAdelanto(idAdelanto, NombreTarjeta);
                myDialog.dismiss();
            }
        });


        //Eliminar
        newDialogView.findViewById(R.id.Delete_contextual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), Nombre,Toast.LENGTH_SHORT).show();
                cuadroDialogoBorradoAdelantoDB(NombreTarjeta, false, idAdelanto);
                revertirBalanceSaldos(NombreTarjeta, idAdelanto);
                myDialog.dismiss();
            }
        });

        myDialog.show();
    }

    //******************************************************************************************
    //Actualiza los balances de Saldos y revierte al borrar un pago
    private void revertirBalanceSaldos(String nombreTarjeta, String idAdelanto) {
        //código de actualización
        CashAdvanceDBHelper myCashAdvance = new CashAdvanceDBHelper(getApplicationContext());
        BalanceDBHelper myBalanceInfo = new BalanceDBHelper(getApplicationContext());
        CashAdvanceInfo myActualCashAdvance = new CashAdvanceInfo();
        CardBalanceInfo myBalance = new CardBalanceInfo();

        myActualCashAdvance = myCashAdvance.recuperarAdelanto(Integer.parseInt(idAdelanto));
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
        double montoAdelantoActual;
        double newSaldoDeudorInt;
        double newSaldoDeudorCero;
        double newSaldoDisponible;

        montoAdelantoActual = myActualCashAdvance.obtenerMontoAdelanto();

        idtransaccion = myBalance.obtenerIDTransaction();
        SaldoDeudorInt = myBalance.obtenerSaldoDeudorINT();
        SaldoDeudorCero = myBalance.obtenerSaldoDeudorCEROINT();
        MontoDisponible = myBalance.obtenerMontoDisponible();


        //Falta crear la fórmula de distribución de pago
        //Para pruebas se dividirá equitativamente entre tres

        newSaldoDeudorCero = SaldoDeudorCero;
        newSaldoDeudorInt = SaldoDeudorInt + montoAdelantoActual;
        newSaldoDisponible = MontoDisponible - montoAdelantoActual;


        //actualización de la base de datos Saldo Disponible
        myBalanceInfo.actualizarSALDO(idtransaccion, nombreTarjeta, newSaldoDeudorInt, newSaldoDeudorCero, newSaldoDisponible);
        myBalanceInfo.close();


    }


    //******************************************************************************************
    private void cuadroDialogoBorradoAdelantoDB(final String nombreTarjeta, final boolean complete, final String idTransaction){

        //creación de un dialogo personalizado
        LayoutInflater fabrica = LayoutInflater.from(this);
        final View borrarDialogView = fabrica.inflate(R.layout.dialog_delete, null);

        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(borrarDialogView);

        borrarDialogView.findViewById(R.id.buttonOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (complete == true) {
                    myCashAdvanceInfo.DeleteAllRegisterIdentified(nombreTarjeta);
                }
                else
                {
                    //encuentra el entero del registro y lo pasa a la variable control
                    int enteroRegistro;
                    enteroRegistro = Integer.parseInt(idTransaction);
                    //elimina el registro y reorganiza la base de datos
                    myCashAdvanceInfo.deleteEntry(enteroRegistro);
                    myCashAdvanceInfo.VacuumSQLiteDB();

                }

                myCashAdvanceInfo.close();

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
