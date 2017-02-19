package com.example.imagineup.centinela;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.01
 * @since 2016-05-25
 */

public class SettingsDBHelper extends SQLiteOpenHelper {

    //Constantes principales de la clase
    private static final String DATABASE_NAME ="CONFIGURACION.db";
    private static final int DATABASE_VERSION = 1;

    //Constantes de la tabla
    private static final String TABLE_NAME = "DATOS_CONFIGURACION";

    //Nombre de las columnas
    public static final String COLUMN_ID ="_id";
    public static final String COLUMN_DIASANTEPAGO="DiasAntes";
    public static final String COLUMN_USUARIO ="Usuario";
    public static final String COLUMN_NOTIF ="Notificacion";
    public static final String COLUMN_FECHA="fecha";

    //Constructor de la clase
    public SettingsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creación de las entradas de la tabla
    public void onCreate(SQLiteDatabase database)
    {
        String CREATE_ITEM_TABLE="CREATE TABLE " + TABLE_NAME +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_DIASANTEPAGO + " TEXT, "
                + COLUMN_NOTIF + " TEXT, "
                + COLUMN_USUARIO + " TEXT, "
                + COLUMN_FECHA + " TEXT)";

        database.execSQL(CREATE_ITEM_TABLE);
    }

    //Actualizador de la base de datos
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        //Elimina la tabla antigua si esta existe
        db.execSQL("DROP TABLE IF EXIST " + TABLE_NAME);

        //Crea una tabla nueva otra vez
        onCreate(db);
    }


    //Gestión de la base de datos PAGO número de registro(ID) y fecha son automáticos
    public void insertarSETTINGS(int diasantepago, int notificacion, String usuarioid)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String fecha_actual = establecerFecha();
        //int IDPAGO = numberOfRows()+1; Se hace en forma automática

        if(db != null)
        {
            ContentValues valoresConfiguracion = new ContentValues();

            //valoresPago.put(COLUMN_ID, IDPAGO);
            valoresConfiguracion.put(COLUMN_DIASANTEPAGO, diasantepago);
            valoresConfiguracion.put(COLUMN_NOTIF, notificacion);
            valoresConfiguracion.put(COLUMN_USUARIO, usuarioid);
            valoresConfiguracion.put(COLUMN_FECHA, fecha_actual);

            db.insert(TABLE_NAME, null, valoresConfiguracion);
            db.close();
        }
    }

    //Actualizar un registro
    public void actualizarCONFIGURACION(int idCONFIGURACION, int diasantepago, int notificacion, String usuarioid)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String fecha_actual = establecerFechaSinFormato();

        ContentValues valoresConfiguracion  = new ContentValues();

        valoresConfiguracion.put(COLUMN_ID, idCONFIGURACION);
        valoresConfiguracion.put(COLUMN_DIASANTEPAGO, diasantepago);
        valoresConfiguracion.put(COLUMN_NOTIF, notificacion);
        valoresConfiguracion.put(COLUMN_USUARIO, usuarioid);
        valoresConfiguracion.put(COLUMN_FECHA, fecha_actual);

        db.update(TABLE_NAME, valoresConfiguracion, COLUMN_ID + "=?", new String[]{Integer.toString(idCONFIGURACION)});
        db.close();


    }


    /**
     * deleteEntry funciona para borrar una entrada dado su número de identificación
     * @param id
     * @return
     */

    public Integer deleteEntry (Integer id)
    {
        //Hacer la base de datos escribible
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{Integer.toString(id)});
    }

    public void VacuumSQLiteDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        //Aplica el VACUUM
        db.execSQL("VACUUM");
        db.close();
    }

    //Devolver el número de filas
    public int numberOfRows() {
        //Hacer la base de datos legible
        SQLiteDatabase db = this.getReadableDatabase();
        //Sacar el número de filas
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }

    //Devolver la fecha y hora de registro automático
    private String establecerFecha(){
        SimpleDateFormat formatofecha = new SimpleDateFormat("ddMMyyyy_HHmmss");
        String fecharegistro = formatofecha.format(new Date());
        return fecharegistro;
    }

    //Establecer la fecha en string pero sin formato
    private String establecerFechaSinFormato() {
        Date mi_fecha = new Date();
        Long fecha_numero = mi_fecha.getTime();
        String fecharegistro = fecha_numero.toString();
        return fecharegistro;
    }

    //Gestión de la Información de la base de datos
    public SettingsInfo recuperarConfiguracion(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperados = {COLUMN_ID, COLUMN_DIASANTEPAGO, COLUMN_NOTIF, COLUMN_USUARIO, COLUMN_FECHA};

        Cursor mi_cursor = db.query(TABLE_NAME, valores_recuperados, COLUMN_ID + "=?" ,
                new String[]{Integer.toString(id)}, null, null, null, null);

        if (mi_cursor != null)
        {
            mi_cursor.moveToFirst();
        }

        SettingsInfo mi_settingsInfo = new SettingsInfo(mi_cursor.getInt(0), //Identificador
                mi_cursor.getInt(1), //Dias antes de pago
                mi_cursor.getInt(2), //Autorizacion de notificacion True = 1, False = 0;
                mi_cursor.getString(3), //Recupera el nombre del usuario
                mi_cursor.getString(4) // Recupera la fecha de creacion
        );


        db.close();
        mi_cursor.close();

        return mi_settingsInfo;
    }


    //Funcion un registro por nombre de usuario
    public SettingsInfo fetchARowbyUserID(String IDUser) {
        SQLiteDatabase db = getReadableDatabase();

        String[] valores_recuperados = {COLUMN_ID, COLUMN_DIASANTEPAGO, COLUMN_NOTIF, COLUMN_USUARIO, COLUMN_FECHA};

        Cursor mi_cursor = db.query(true, TABLE_NAME, valores_recuperados,  COLUMN_USUARIO + " like ?" , new String[]{IDUser + "%"}, null, null, null, null, null);

        if (mi_cursor != null) {
            mi_cursor.moveToFirst();
        }
        SettingsInfo mi_settingsInfo = new SettingsInfo(mi_cursor.getInt(0), //Identificador
                mi_cursor.getInt(1), //Dias antes de pago
                mi_cursor.getInt(2), //Autorizacion de notificacion True = 1, False = 0;
                mi_cursor.getString(3), //Recupera el nombre del usuario
                mi_cursor.getString(4) // Recupera la fecha de creacion
        );


        db.close();
        mi_cursor.close();

        return mi_settingsInfo;
    }



    //Funcion Cursor por nombre de usuario
    public Cursor fetchARegisterbyUserID(String IDUser) {
        SQLiteDatabase db = getReadableDatabase();

        String[] valores_recuperados = {COLUMN_ID, COLUMN_DIASANTEPAGO, COLUMN_NOTIF, COLUMN_USUARIO, COLUMN_FECHA};

        Cursor mi_cursor = db.query(true, TABLE_NAME, valores_recuperados,  COLUMN_USUARIO + " like ?" , new String[]{IDUser + "%"}, null, null, null, null, null);

        if (mi_cursor != null) {
            mi_cursor.moveToFirst();
        }
        return mi_cursor;
    }


}
