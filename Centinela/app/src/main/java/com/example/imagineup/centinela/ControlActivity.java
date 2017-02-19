package com.example.imagineup.centinela;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.05
 * @since 2015-12-10 2016-03-01 2016-03-07 2016-05-22 2016-05-25
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class ControlActivity extends AppCompatActivity {

    private CardDBHelper myCardInfo;
    private String nombreTarjetaCNT;
    private ListView myListView;
    private String userEmail;
    private String userNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        //recuperación del correo y el número de usuario desde Actividad Login
        Bundle extras = getIntent().getExtras();
        userEmail = extras.getString(getResources().getString(R.string.userID));
        userNumber = extras.getString(getResources().getString(R.string.userNumber));

        crearListView();
        revisionTarjetasYNotificacion(userNumber);

    }

    //*****************************************************************************************


    //Agregar o Inflar la Action Bar
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        getMenuInflater().inflate(R.menu.menu_control, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Agrega Acción a cada uno de los items del menu ActionBar
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId(); //captura el id del menú seleccionado

        //para cada item un if en cada uno se coloca el código
        switch (id) {
            //Para borrar todos los registros
            case R.id.delete_icon:
                metodoDELETEALL();
                return true;

            //Para agregar un Registro
            case R.id.add_icon:
                metodoADD();
                return true;

            //Para mostrar la ventana de ayuda del sistema
            case R.id.action_help:
                metodoHELP();
                return true;

            //Para configurar el correo de recuperación de contraseña
            case R.id.action_edit:
                metodoEDITPROFILE(userEmail, userNumber);
                return true;

            //Para mostrar la información de la aplicación
            case R.id.action_info:
                metodoINFO();
                return true;

            //Para mostrar los settings de la aplicacion
            case R.id.action_set:
                metodoSETTINGS();
                return true;

            default:
            return super.onOptionsItemSelected(item);
        }
    }

    //******************************************************************************************

    //agregar un elemento a lista
    private void metodoADD(){
        //cuando un elemento agregar ha sido seleccionado
        abrirActividadRegistro(userEmail, userNumber);
    }

    //eliminar todos los registros
    private void metodoDELETEALL() {
        //borrado completo del listado
        cuadroDialogoBorradoDB(null, true, userNumber);

    }

    //editar un registro
    private void metodoEDITPROFILE(String usuario, String usernumber){
        editarPerfil(usuario, usernumber);
    }

    //acerca de
    private  void metodoINFO(){
        abrirAboutTO();
    }

    //método de ayuda
    private void metodoHELP(){
        abrirHelpTO();
    }

    //metodo de configuraciones
    private void metodoSETTINGS() {
        abrirUserSettings(userNumber);
    }

    //******************************************************************************************

    //Mensaje No implementado
    private void mensajeNoImplementado() {
        String mensaje = getResources().getString(R.string.mensajeNoImplementado);
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }


    //******************************************************************************************

    //Abre actividad correspondiente
    private void abrirActividadRegistro(String Usercontrol, String usernumber){
        Intent miActividad = new Intent(this, InfoCardActivity.class);
        miActividad.putExtra(getResources().getString(R.string.userID),Usercontrol);
        miActividad.putExtra(getResources().getString(R.string.userNumber), usernumber);
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

    //abrir actividad acerca de
    private void abrirHelpTO(){
        Intent miActividad = new Intent(this, HelpActivity.class);
        startActivity(miActividad);
    }

    //abrir actividad editar perfil
    private void editarPerfil(String usuario, String usernumber){
        Intent miActividad = new Intent(this, EditProfile.class);
        miActividad.putExtra(getResources().getString(R.string.userID), usuario);
        miActividad.putExtra(getResources().getString(R.string.userNumber),usernumber);
        startActivity(miActividad);
        cerrarActividad();
    }

    //abrir actividad editar Tarjeta
    private void editarTarjeta(String tarjeta, String emailUser) {
        Intent miActividad = new Intent (this, EditCard.class);
        miActividad.putExtra(getResources().getString(R.string.userID), emailUser);
        miActividad.putExtra(getResources().getString(R.string.cardInfoID), tarjeta);
        startActivity(miActividad);
        cerrarActividad();
    }

    //abrir dialogo para consulta de información tarjeta
    private void consultarTarjeta(String tarjeta) {
        Intent miActividad = new Intent (this, ReviewCard.class);
        miActividad.putExtra(getResources().getString(R.string.cardInfoID), tarjeta);
        startActivity(miActividad);
    }

    //Abrir actividad control de Pagos por tarjeta
    private void consultarHistorialPago(String tarjeta){
        Intent miActividad = new Intent (this, PayControlActivity.class);
        miActividad.putExtra(getResources().getString(R.string.cardInfoID), tarjeta);
        startActivity(miActividad);
    }

    //Abrir actividad control de Compras por tarjeta
    private void consultarHistorialCompra(String tarjeta){
        Intent miActividad = new Intent (this, BuyControlActivity.class);
        miActividad.putExtra(getResources().getString(R.string.cardInfoID), tarjeta);
        startActivity(miActividad);
    }

    //Abrir actividad control de Adelanto por tarjeta
    private void consultarHistorialAdelanto(String tarjeta){
        Intent miActividad = new Intent (this, CashAdvanceControlActivity.class);
        miActividad.putExtra(getResources().getString(R.string.cardInfoID), tarjeta);
        startActivity(miActividad);
    }

    //Abrir actividad de configuración de la aplicación
    private void abrirUserSettings(String usernumber){
        Intent miActividad = new Intent (this, SettingsActivity.class);
        miActividad.putExtra(getResources().getString(R.string.userNumber), usernumber);
        startActivity(miActividad);
    }

    //Abrir actividad de configuración de las comisiones bancarias
    private void consultarComision(String cardname){
        Intent miActividad = new Intent (this, BankFeesActivity.class);
        miActividad.putExtra(getResources().getString(R.string.cardInfoID), cardname);
        startActivity(miActividad);
    }

    //******************************************************************************************

    //Creación del ListView
    private void crearListView() {

        //obtener lista de la base de datos
        myCardInfo = new CardDBHelper(this);
        myCardInfo.getReadableDatabase();

        try {

            //Carga todos los registros de la base de datos a la ListView
            //Cursor myCursor = myCardInfo.fetchAllRegister();
            Cursor myCursor = myCardInfo.fetchARegisterbyUSERNUMBER(userNumber);

            if (myCursor != null) {

                //definición de columnas

                /*Estructura de myCardInfo
                * ID
                * Cédula o Pasaporte
                * Nombre Tarjeta
                * Emisor
                * Dias Corte
                * Dias Gracia
                * Fecha de registro */


                String[] columns = new String[]{
                        //CardDBHelper.COLUMN_IDUSER,
                        CardDBHelper.COLUMN_NOMBRE,
                        CardDBHelper.COLUMN_EMISOR,
                        CardDBHelper.COLUMN_DIASCORTE,
                        CardDBHelper.COLUMN_DIASGRACIA
                };

                //definición de los campos en el XML
                int[] destiny = new int[]{
                        R.id.title,
                        R.id.subtitle
                };

                //creación del adapter por fuerza columna id debe ser denominada "_id"
                SimpleCursorAdapter dataAdapter = new SimpleCursorAdapter(this, R.layout.card_info,
                        myCursor, columns, destiny, 0);

                //ListView creación y asignación del adapter
                myListView = (ListView) findViewById(R.id.myCardList);
                myListView.setAdapter(dataAdapter);

                //para registrar Views y cada item tenga un menu contextual
                registerForContextMenu(myListView);

                //Al seleccionar un item de la lista
                myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Cursor myCursor = (Cursor) myListView.getItemAtPosition(position);
                        //recupera el nombre de la tarjeta:
                        String nombreTarjeta = myCursor.getString(myCursor.getColumnIndexOrThrow(CardDBHelper.COLUMN_NOMBRE));
                        nombreTarjetaCNT = nombreTarjeta;
                        //Toast.makeText(getApplicationContext(), nombreTarjeta,Toast.LENGTH_SHORT).show();
                        cuadroDialogoMenuContextual(nombreTarjetaCNT);

                    }
                });
            }
            else
            {
                //Si el cursor está vacío
            }

            myCardInfo.close();

        }
        catch (SQLiteException e) {

        }


    }
    //******************************************************************************************
    private  void cuadroDialogoMenuContextual(final String NombreTarjeta) {
        //creación de un dialogo personalizado
        LayoutInflater fabrica = LayoutInflater.from(this);
        final View newDialogView = fabrica.inflate(R.layout.dialog_menu, null);

        final AlertDialog myDialog = new AlertDialog.Builder(this).create();
        myDialog.setView(newDialogView);

        //Editar
        newDialogView.findViewById(R.id.Edit_contextual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), Nombre,Toast.LENGTH_SHORT).show();
                editarTarjeta(NombreTarjeta, userEmail);
                myDialog.dismiss();
            }
        });

        //Historial Pagos
        newDialogView.findViewById(R.id.Hist_Pago_contextual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), NombreTarjeta, Toast.LENGTH_SHORT).show();
                consultarHistorialPago(NombreTarjeta);
            }
        });

        //Historial Compras
        newDialogView.findViewById(R.id.Hist_Compra_contextual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), NombreTarjeta, Toast.LENGTH_SHORT).show();
                consultarHistorialCompra(NombreTarjeta);
            }
        });

        //Historial Adelantos
        newDialogView.findViewById(R.id.Hist_Adelanto_contextual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), NombreTarjeta, Toast.LENGTH_SHORT).show();
                consultarHistorialAdelanto(NombreTarjeta);
            }
        });

        //Consultar
        newDialogView.findViewById(R.id.Review_contextual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), Nombre, Toast.LENGTH_SHORT).show();
                consultarTarjeta(NombreTarjeta);
                myDialog.dismiss();
            }
        });

        //Eliminar
        newDialogView.findViewById(R.id.Delete_contextual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), Nombre,Toast.LENGTH_SHORT).show();
                cuadroDialogoBorradoDB(NombreTarjeta, false, null);
                myDialog.dismiss();
            }
        });

        //Revisar Comisiones
        newDialogView.findViewById(R.id.Hist_Comision_contextual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), Nombre,Toast.LENGTH_SHORT).show();
                consultarComision(NombreTarjeta);
                myDialog.dismiss();
            }
        });

        myDialog.show();

    }


    //******************************************************************************************
    private void cuadroDialogoBorradoDB(final String nombreTarjeta, final boolean complete, final String usernumber){

        //creación de un dialogo personalizado
        LayoutInflater fabrica = LayoutInflater.from(this);
        final View borrarDialogView = fabrica.inflate(R.layout.dialog_delete, null);

        final AlertDialog deleteDialog = new AlertDialog.Builder(this).create();
        deleteDialog.setView(borrarDialogView);

        borrarDialogView.findViewById(R.id.buttonOK).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (complete == true) {
                    //myCardInfo.DeleteAllRegister();
                    myCardInfo.DeleteAllRegisterIdentified(usernumber);
                }
                else
                {
                    //Toast.makeText(getApplicationContext(), nombreTarjeta, Toast.LENGTH_SHORT).show();

                    //encuentra el entero del registro y lo pasa a la variable control
                    int enteroRegistro;
                    List<String> myCard = myCardInfo.recuperarFila(nombreTarjeta);
                    enteroRegistro = Integer.parseInt(myCard.get(0));

                    //elimina el registro y reorganiza la base de datos
                    myCardInfo.deleteEntry(enteroRegistro);
                    myCardInfo.VacuumSQLiteDB();

                    //Toast.makeText(getApplicationContext(), String.format ("%d",enteroRegistro), Toast.LENGTH_SHORT).show();

                }
                myCardInfo.close();

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

    //********************************************************************************************

    //Revision de todas las listas de tarjetas y devolver mensaje si hay una que se acerque el día de pago
    private void revisionTarjetasYNotificacion(String iduser){
        try {

            int diasantepago;
            boolean permisoNotificacion;
            CardDBHelper mycardDB = new CardDBHelper(getApplicationContext());

            CardInfo temporalCardInfo;
            List<CardInfo> my_userCards;
            my_userCards = mycardDB.recuperarTarjetas(iduser);

            if (my_userCards != null) {
                Log.e("Success: "," revisionTarjetasYNotificacion - ControlActivity");
                Iterator<CardInfo> my_cardIterator = my_userCards.iterator();

                diasantepago = devolverDiasAntesFechaPago(iduser);
                permisoNotificacion = notificacionesPermitidas(iduser);

                String NombreTarjeta;
                int diacorte;
                int diasgracia;
                int diaContado;
                int diaPagoMinimo;

                int diaNumero = devolverNumerodiaHoy();


                if (permisoNotificacion == true) {

                    while (my_cardIterator.hasNext()) {
                        temporalCardInfo = my_cardIterator.next();
                        //Nombre de la tarjeta
                        NombreTarjeta = temporalCardInfo.obtenerNombreID();

                        //(Dia de Corte + Dias de Gracias) mod 30 = Día de pago contado
                        //Día Contado + 8 Días
                        diacorte = temporalCardInfo.obtenerDiaCorte();
                        diasgracia = temporalCardInfo.obtenerDiaGracia();
                        diaContado = (diacorte + diasgracia) % 30;
                        diaPagoMinimo = (diaContado + 8) % 30;

                        if (diaNumero == (Math.abs(diaContado - diasantepago) % 30)) {
                            enviarAvisodePago(NombreTarjeta, Integer.toString(diaContado), Integer.toString(diaPagoMinimo));

                        }
                    }
                }

        }

        } catch (CursorIndexOutOfBoundsException ex) {
            Log.e("Cursor error: ", " revisionTarjetasYNotificacion - ControlActivity");
            Log.e("trace", ex.getMessage().toString());
        } finally {
            Log.e("GarbageMem: "," revisionTarjetasYNotificacion - ControlActivity");
        }

    }


    //Regresar el número de día de hoy
    private int devolverNumerodiaHoy(){

        Date mydate = new Date();
        Calendar mycalendar = Calendar.getInstance();
        mycalendar.setTime(mydate);

        int year = mycalendar.get(Calendar.YEAR);
        int month = mycalendar.get(Calendar.MONTH);
        int day = mycalendar.get(Calendar.DAY_OF_MONTH);

        return day;

    }

    //devolver el mes actual
    private String devolverMesActual() {
        String[] meses = {
                getResources().getString(R.string.mes01),
                getResources().getString(R.string.mes02),
                getResources().getString(R.string.mes03),
                getResources().getString(R.string.mes04),
                getResources().getString(R.string.mes05),
                getResources().getString(R.string.mes06),
                getResources().getString(R.string.mes07),
                getResources().getString(R.string.mes08),
                getResources().getString(R.string.mes09),
                getResources().getString(R.string.mes10),
                getResources().getString(R.string.mes11),
                getResources().getString(R.string.mes12)
        };

        Date mydate = new Date();
        Calendar mycalendar = Calendar.getInstance();
        mycalendar.setTime(mydate);

        int year = mycalendar.get(Calendar.YEAR);
        int month = mycalendar.get(Calendar.MONTH);
        int day = mycalendar.get(Calendar.DAY_OF_MONTH);

        return meses[month];
    }



    //Cargar los días Antes fecha de pago de un usuario
    private int devolverDiasAntesFechaPago(String iduser){
        //Creamos la base de datos
        SettingsDBHelper mysettings = new SettingsDBHelper(getApplicationContext());
        SettingsInfo mysettingsInfo = new SettingsInfo();

        if (mysettingsInfo != null) {

            try {
                mysettingsInfo = mysettings.fetchARowbyUserID(iduser);
                Log.e("Success: "," devolverDiasAnteFechaPago - ControlActivity");
                return mysettingsInfo.obtenerDiasAntePago();


            }catch (SQLiteException ex) {
                Log.e("ErrorSQL", " devolverDiasAnteFechaPago  - ControlActivity");
                Log.e("trace", ex.getMessage().toString());
            }catch (CursorIndexOutOfBoundsException ex){
                Log.e("ErrorCursor: "," ddevolverDiasAnteFechaPago  - ControlActivity");
                Log.e("trace", ex.getMessage().toString());
            } finally {
                Log.e("GarbageMem: "," devolverDiasAnteFechaPago  - ControlActivity");
            }
        }
        else
        {
            mysettingsInfo = new SettingsInfo(iduser);
        }

        mysettings.close();
        return mysettingsInfo.obtenerDiasAntePago();
    }


    //Determinar si están permitidas las notificaciones

    private boolean notificacionesPermitidas(String iduser) {
        //Creamos la base de datos
        SettingsDBHelper mysettings = new SettingsDBHelper(getApplicationContext());
        SettingsInfo mysettingsInfo = new SettingsInfo();

        try {
            mysettingsInfo = mysettings.fetchARowbyUserID(iduser);

            if (mysettingsInfo != null) {
                Log.e("Success: "," notificacionesPermitidas - ControlActivity");
                int valordb = mysettingsInfo.obtenerAutorizacionNotificacion();

                if (valordb == 0) {
                    return false;
                }
                else {
                    return true;
                }

            }

        }catch (SQLiteException ex) {
            Log.e("ErrorSQL: "," notificacionesPermitidas - ControlActivity");
            Log.e("trace", ex.getMessage().toString());
        } finally {
            Log.e("GarbageMem: "," notificacionesPermitidas - ControlActivity");
        }

        mysettings.close();
        return true;
    }

    //**************************************************************************************************************
    //enviar aviso de fecha de pago por mensaje
    private void enviarAvisodePago(String UserCard, String diaContado, String diaPagoMinimo) {

        String direccion = "+" + getResources().getString(R.string.app_name);
        String mesactual = devolverMesActual();
        String password = String.format("%s %s %s\n%s %s %s %s\n%s %s %s %s",getResources().getString(R.string.userCard),": ", UserCard,
                getResources().getString(R.string.avisoPagoContado),": " , mesactual, diaContado,
                getResources().getString(R.string.avisoPagoMinimo),":", mesactual, diaPagoMinimo);

        ContentValues my_value = new ContentValues();
        my_value.put("address", direccion);
        my_value.put("body", password);
        my_value.put("read", "0"); //0 no leído, 1 leído
        //my_value.put("date", "<fecha de hoy>");
        getContentResolver().insert(Uri.parse("content://sms/inbox"), my_value);
        envioNotificacion(getString(R.string.mensajeNotificacion), R.drawable.small_icon);

    }

    private void envioNotificacion(String message, int icon) {
        //ID Notificación
        int notifyID = 1;

        //Inicialización del administrador de notificación
        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(this);

        //Set Icons, title, Text
        notifBuilder.setSmallIcon(icon);
        notifBuilder.setContentTitle(getResources().getString(R.string.app_name));
        notifBuilder.setContentText(message);

        /*
        //Acción al clickear el mensaje
        Intent ifclickonNotif = new Intent(Intent.ACTION_GET_CONTENT);

        TaskStackBuilder mystackBuilder = TaskStackBuilder.create(this);
        mystackBuilder.addNextIntent(ifclickonNotif);
        PendingIntent resultPendingIntent = mystackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        notifBuilder.setContentIntent(resultPendingIntent);*/

        //presentación de la Notificación
        NotificationManager managerofNotify = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //permite el actualizar la notificación
        managerofNotify.notify(notifyID, notifBuilder.build());
    }

}



