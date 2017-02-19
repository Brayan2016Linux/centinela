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
 * @version 0.03
 * @since 2016-03-02 2016-21-05 2016-06-02
 */

public class InterestDBHelper extends SQLiteOpenHelper {

    //Constantes principales de la clase
    private static final String DATABASE_NAME ="INTERESES.db";
    private static final int DATABASE_VERSION = 1;

    //Constantes de la tabla
    private static final String TABLE_NAME = "DATOS_INTERESES";

    //Nombre de las columnas
    private static final String COLUMN_ID ="id";
    private static final String COLUMN_TARJETA ="tarjetaID";
    private static final String COLUMN_INT_FIJO="INTfijo";
    private static final String COLUMN_INT_MOR="INTMoratorio";
    private static final String COLUMN_FECHA="fecha";

    //Constructor de la clase
    public InterestDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creación de las entradas de la tabla
    public void onCreate(SQLiteDatabase database)
    {
        String CREATE_ITEM_TABLE="CREATE TABLE " + TABLE_NAME +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_TARJETA + " TEXT, "
                + COLUMN_INT_FIJO + " TEXT, "
                + COLUMN_INT_MOR + " TEXT, "
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


    //Gestión de la base de datos Intereses número de registro(ID) y fecha son automáticos
    public void insertarIntereses(String tarjetaID, double interesFijo, double interesMoratorio)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String fecha_actual = establecerFechaSinFormato();
        //int IDSALDO = numberOfRows()+1; se hace en forma automática

        if(db != null)
        {
            ContentValues valoresInteres = new ContentValues();
            //valoresIntereses.put(COLUMN_ID, IDSALDO);
            valoresInteres.put(COLUMN_TARJETA, tarjetaID);
            valoresInteres.put(COLUMN_INT_FIJO, interesFijo);
            valoresInteres.put(COLUMN_INT_MOR, interesMoratorio);
            valoresInteres.put(COLUMN_FECHA, fecha_actual);

            db.insert(TABLE_NAME, null, valoresInteres);
            db.close();
        }
    }

    //Actualizar un registro
    public void actualizarInteres(int idINTERES, String tarjetaID, double interesFijo, double interesMoratorio)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String fecha_actual = establecerFechaSinFormato();

        ContentValues valoresIntereses = new ContentValues();

        valoresIntereses.put(COLUMN_ID, idINTERES);
        valoresIntereses.put(COLUMN_TARJETA, tarjetaID);
        valoresIntereses.put(COLUMN_INT_FIJO, interesFijo);
        valoresIntereses.put(COLUMN_INT_MOR, interesMoratorio);
        valoresIntereses.put(COLUMN_FECHA, fecha_actual);

        db.update(TABLE_NAME, valoresIntereses, COLUMN_ID + "=?", new String[]{Integer.toString(idINTERES)});
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
    public InterestInfo recuperarIntereses(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperados = {COLUMN_ID, COLUMN_TARJETA, COLUMN_INT_FIJO,
                COLUMN_INT_MOR, COLUMN_FECHA};

        Cursor mi_cursor = db.query(TABLE_NAME, valores_recuperados, COLUMN_ID + "=?" ,
                new String[]{Integer.toString(id)}, null, null, null, null);

        if (mi_cursor != null)
        {
            mi_cursor.moveToFirst();
        }

        InterestInfo mi_interestInfo = new InterestInfo(mi_cursor.getInt(0),
                mi_cursor.getString(1),
                mi_cursor.getDouble(2),
                mi_cursor.getDouble(3),
                mi_cursor.getString(4)
        );


        db.close();
        mi_cursor.close();

        return mi_interestInfo;
    }

    //Recuperar Interes por nombre de Tarjeta a una clase InterestInfo
    public InterestInfo recuperarNombreTarjeta(String tarjetaID) {

        SQLiteDatabase db = getReadableDatabase();

        String[] valores_recuperados = {COLUMN_ID, COLUMN_TARJETA, COLUMN_INT_FIJO,
                COLUMN_INT_MOR, COLUMN_FECHA};

        Cursor mi_cursor = db.query(TABLE_NAME, valores_recuperados, COLUMN_TARJETA + "=?",
                new String[]{tarjetaID}, null, null, null , null);

        if (mi_cursor != null)
        {
            mi_cursor.moveToFirst();
        }

        InterestInfo mi_interestInfo = new InterestInfo(mi_cursor.getInt(0),
                mi_cursor.getString(1),
                mi_cursor.getDouble(2),
                mi_cursor.getDouble(3),
                mi_cursor.getString(4)
        );


        db.close();
        mi_cursor.close();

        return mi_interestInfo;

    }



    //Recuperación de todos los registros en una lista tipo InterestInfo
    public List<InterestInfo> recuperarIntereses() {
        SQLiteDatabase db = getReadableDatabase();
        List<InterestInfo> lista_intereses= new ArrayList<>();
        String[] valores_recuperados = {COLUMN_ID, COLUMN_TARJETA, COLUMN_INT_FIJO,
                COLUMN_INT_MOR, COLUMN_FECHA};

        Cursor mi_cursor = db.query(TABLE_NAME, valores_recuperados, null,
                null, null, null, null, null);

        if (mi_cursor != null) {
            mi_cursor.moveToFirst();
        }

        do {
            InterestInfo mi_interestInfo = new InterestInfo(mi_cursor.getInt(0),
                    mi_cursor.getString(1),
                    mi_cursor.getDouble(2),
                    mi_cursor.getDouble(3),
                    mi_cursor.getString(4)
            );
            lista_intereses.add(mi_interestInfo);
        } while (mi_cursor.moveToNext());
        db.close();
        mi_cursor.close();

        return lista_intereses;
    }

    //Recuperación de una fila por nombre en una lista String
    public List<String> recuperarFila(String nombreTarjeta){
        //Recuperación de la base de datos
        SQLiteDatabase db = this.getReadableDatabase();

        //Grabar en una lista todos los registros
        List<String> lista_registro = new ArrayList<String>();
        String[] valores_recuperados = {COLUMN_ID, COLUMN_TARJETA, COLUMN_INT_FIJO,
                COLUMN_INT_MOR, COLUMN_FECHA};

        //Recuperación de los datos en un cursor
        Cursor c = db.query(TABLE_NAME, valores_recuperados,COLUMN_TARJETA + "=?" , new String[]{nombreTarjeta}, null, null, null);

        //Moverse al inicio del registro
        if ( c != null) {
            c.moveToFirst();
        }

        try {
            do {
                lista_registro.add(Integer.toString(c.getInt(0))); //Saldo ID
                lista_registro.add(c.getString(1)); //Nombre Tarjeta
                lista_registro.add(Double.toString(c.getDouble(2))); //Intereses fijos de la tarjeta
                lista_registro.add(Double.toString(c.getDouble(3))); //Intereses moratorios de la tarjeta
                lista_registro.add(c.getString(4)); //Fecha creación registro
            } while (c.moveToNext());
        }
        catch (CursorIndexOutOfBoundsException myexception)
        {
            //Código faltante
        }

        return lista_registro;
    }

}
