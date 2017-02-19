package com.example.imagineup.centinela;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.01
 * @since 2016-03-07
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


public class BuyControlActivity extends AppCompatActivity {

    private BuyDBHelper myBuyInfo;
    private String nombreTarjetaCNT;
    private ListView myListViewBuy;
    private TextView myTitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_control);

        //recuperación del correo y el número de usuario desde Actividad Login
        Bundle extras = getIntent().getExtras();
        nombreTarjetaCNT = extras.getString(getResources().getString(R.string.cardInfoID));

        myTitleView = (TextView) findViewById(R.id.etiquetatarjetaTitulo);
        myTitleView.setText(nombreTarjetaCNT);

        crearListViewBuy(nombreTarjetaCNT);

    }

    //*****************************************************************************************


    //Agregar o Inflar la Action Bar
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_compras, menu);
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
        abrirActividadCompra(NombreTarjeta);
    }

    //eliminar todos los registros
    private void metodoDELETEALL(String NombreTarjeta) {
        //borrado completo del listado
        cuadroDialogoBorradoCompraDB(NombreTarjeta, true, null);

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


    //Abre actividad correspondiente para registro de Compra
    private void abrirActividadCompra(String TarjetaControl){
        Intent miActividad = new Intent(this, BuyActivity.class);
        miActividad.putExtra(getResources().getString(R.string.cardInfoID), TarjetaControl);
        startActivity(miActividad);
        cerrarActividad();
    }

    //cierra la actividad
    private void cerrarActividad(){
        this.finish();
    }


    //abrir actividad editar una compra
    private void editarCompra(String idCompra, String nombreTarjeta){
        Intent miActividad = new Intent(this, EditBuy.class);
        miActividad.putExtra(getResources().getString(R.string.idCompN), idCompra);
        miActividad.putExtra(getResources().getString(R.string.cardInfoID),nombreTarjeta);
        startActivity(miActividad);
        cerrarActividad();
    }


    //******************************************************************************************

    //Creación del ListView Compra
    private void crearListViewBuy(String cardName) {

        //obtener lista de la base de datos
        myBuyInfo = new BuyDBHelper(this);
        myBuyInfo.getReadableDatabase();

        try {

            //Carga todos los registros de la base de datos a la ListView
            //Cursor myCursor = myCardInfo.fetchAllRegister();
            Cursor myCursor = myBuyInfo.fetchARegisterbyCardName(cardName);

            if (myCursor != null) {

                //definición de columnas

                /*Estructura de myBuyInfo
                * ID
                * Nombre Tarjeta
                * Detalle CompraID
                * Monto
                * Fecha de Compra
                * Fecha de Compra con Formato
                * PeríodoCrédito
                * Modalidad
                * Fecha de registro*/


                String[] columns = new String[]{
                        //BuyDBHelper.COLUMN_ID,
                        BuyDBHelper.COLUMN_DETALLECOMP,
                        //BuyDBHelper.COLUMN_TARJETA,
                        BuyDBHelper.COLUMN_MONTO,
                        //BuyDBHelper.COLUMN_FECHACOMPRA,
                        BuyDBHelper.COLUMN_FECHACOMPRAFORM,
                        BuyDBHelper.COLUMN_MODALIDAD,
                        BuyDBHelper.COLUMN_PERIODO

                };

                //definición de los campos en el XML
                int[] destiny = new int[]{
                        R.id.detalleCompraInfo,
                        R.id.detalleCompraMonto,
                        R.id.detalleFecha_Compra,
                        R.id.detalleMod_Compra,
                        R.id.detallePer_Compra

                };

                //creación del adapter por fuerza columna id debe ser denominada "_id"
                SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.buy_info,
                        myCursor, columns, destiny, 0);

                //ListView creación y asignación del adapter
                myListViewBuy = (ListView) findViewById(R.id.my_Buy_List);
                myListViewBuy.setAdapter(dataAdapter);

                //para registrar Views y cada item tenga un menu contextual
                registerForContextMenu(myListViewBuy);

                //Al seleccionar un item de la lista
                myListViewBuy.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Cursor myCursor = (Cursor) myListViewBuy.getItemAtPosition(position);
                        //recupera el nombre de la tarjeta:
                        String nombreTarjeta = myCursor.getString(myCursor.getColumnIndexOrThrow(BuyDBHelper.COLUMN_TARJETA));
                        String idCompra = myCursor.getString(myCursor.getColumnIndexOrThrow(BuyDBHelper.COLUMN_ID));
                        nombreTarjetaCNT = nombreTarjeta;
                        //Toast.makeText(getApplicationContext(), nombreTarjeta,Toast.LENGTH_SHORT).show();
                        cuadroDialogoMenuContextualCompra(idCompra, nombreTarjetaCNT);

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
    private  void cuadroDialogoMenuContextualCompra(final String idCompra, final String NombreTarjeta) {
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
                editarCompra(idCompra, NombreTarjeta);
                myDialog.dismiss();
            }
        });

        //Eliminar
        newDialogView.findViewById(R.id.Delete_contextual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), Nombre,Toast.LENGTH_SHORT).show();
                cuadroDialogoBorradoCompraDB(NombreTarjeta, false, idCompra);
                myDialog.dismiss();
            }
        });

        myDialog.show();
    }


    //******************************************************************************************
    private void cuadroDialogoBorradoCompraDB(final String nombreTarjeta, final boolean complete, final String idTransaction){

        //creación de un dialogo personalizado
        LayoutInflater fabrica = LayoutInflater.from(this);
        final View borrarDialogView = fabrica.inflate(R.layout.dialog_delete, null);

        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(borrarDialogView);

        borrarDialogView.findViewById(R.id.buttonOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (complete == true) {
                    //Borra todos los registros de compra
                    myBuyInfo.DeleteAllRegisterIdentified(nombreTarjeta);
                }
                else
                {
                        //Toast.makeText(getApplicationContext(), nombreTarjeta, Toast.LENGTH_SHORT).show();

                        //encuentra el entero del registro y lo pasa a la variable control
                        int enteroRegistro;
                        enteroRegistro = Integer.parseInt(idTransaction);

                        //elimina el registro y reorganiza la base de datos
                        myBuyInfo.deleteEntry(enteroRegistro);
                        myBuyInfo.VacuumSQLiteDB();

                }
                myBuyInfo.close();

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




