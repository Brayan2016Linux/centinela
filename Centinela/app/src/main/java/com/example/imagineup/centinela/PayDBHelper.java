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
 * @version 0.05
 * @since 2015-12-08 2016-02-29 2016-03-04 2016-03-05 2016-08-27
 */

public class PayDBHelper extends SQLiteOpenHelper {

    //Constantes principales de la clase
    private static final String DATABASE_NAME ="PAGOS.db";
    private static final int DATABASE_VERSION = 1;

    //Constantes de la tabla
    private static final String TABLE_NAME = "DATOS_PAGO";

    //Nombre de las columnas
    public static final String COLUMN_ID ="_id";
    public static final String COLUMN_DETALLEPAGO="pagoID";
    public static final String COLUMN_TARJETA ="tarjetaID";
    public static final String COLUMN_MONTO="monto";
    public static final String COLUMN_FECHAPAGO="fechapago";
    public static final String COLUMN_FECHAPAGOFORM="fechapagoformato";
    public static final String COLUMN_FECHA="fecha";

    //Constructor de la clase
    public PayDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creación de las entradas de la tabla
    public void onCreate(SQLiteDatabase database)
    {
        String CREATE_ITEM_TABLE="CREATE TABLE " + TABLE_NAME +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_DETALLEPAGO + " TEXT, "
                + COLUMN_TARJETA + " TEXT, "
                + COLUMN_MONTO + " TEXT, "
                + COLUMN_FECHAPAGO + " TEXT, "
                + COLUMN_FECHAPAGOFORM + " TEXT, "
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
    public void insertarPAGO(String nombreID, String tarjetaID, double monto, String fechapago)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String fecha_actual = establecerFechaSinFormato();
        //int IDPAGO = numberOfRows()+1; Se hace en forma automática

        if(db != null)
        {
            ContentValues valoresPago = new ContentValues();

            //valoresPago.put(COLUMN_ID, IDPAGO);
            valoresPago.put(COLUMN_DETALLEPAGO, nombreID);
            valoresPago.put(COLUMN_TARJETA, tarjetaID);
            valoresPago.put(COLUMN_MONTO, monto);
            valoresPago.put(COLUMN_FECHAPAGO, fechapago);
            valoresPago.put(COLUMN_FECHAPAGOFORM, formatoFecha(fechapago));
            valoresPago.put(COLUMN_FECHA, fecha_actual);

            db.insert(TABLE_NAME, null, valoresPago);
            db.close();
        }
    }

    //Actualizar un registro
    public void actualizarPAGO(int idPAGO, String nombreID, String tarjetaID, double monto, String fechapago)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String fecha_actual = establecerFechaSinFormato();

        ContentValues valoresPago = new ContentValues();

        valoresPago.put(COLUMN_ID, idPAGO);
        valoresPago.put(COLUMN_DETALLEPAGO, nombreID);
        valoresPago.put(COLUMN_TARJETA, tarjetaID);
        valoresPago.put(COLUMN_MONTO, monto);
        valoresPago.put(COLUMN_FECHAPAGO, fechapago);
        valoresPago.put(COLUMN_FECHAPAGOFORM, formatoFecha(fechapago));
        valoresPago.put(COLUMN_FECHA, fecha_actual);

        db.update(TABLE_NAME, valoresPago, COLUMN_ID + "=?", new String[]{Integer.toString(idPAGO)});
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

    //Borra todos los registros con el identificador dado
    public void DeleteAllRegisterIdentified(String cardName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_TARJETA + "=?", new String[]{cardName});
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

    //Fecha con formato definido por SimpleDateFormat
    private  String formatoFecha(String fechaSinFormato) {
        Long fechaLong;
        fechaLong = Long.parseLong(fechaSinFormato);

        SimpleDateFormat formatoFecha= new SimpleDateFormat("dd/MM/yyyy");
        Date mi_fecha = new Date(fechaLong);

        return formatoFecha.format(mi_fecha).toString();

    }

    //Gestión de la Información de la base de datos
    public PayInfo recuperarPago(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperados = {COLUMN_ID,
                COLUMN_DETALLEPAGO, COLUMN_TARJETA, COLUMN_MONTO, COLUMN_FECHAPAGO, COLUMN_FECHAPAGOFORM,
                COLUMN_FECHA};

        Cursor mi_cursor = db.query(TABLE_NAME, valores_recuperados, COLUMN_ID + "=?" ,
                new String[]{Integer.toString(id)}, null, null, null, null);

        if (mi_cursor != null)
        {
            mi_cursor.moveToFirst();
        }

        PayInfo mi_payInfo = new PayInfo(mi_cursor.getInt(0),
                mi_cursor.getString(1),
                mi_cursor.getString(2),
                mi_cursor.getDouble(3),
                mi_cursor.getString(4),
                mi_cursor.getString(5),
                mi_cursor.getString(6)
        );


        db.close();
        mi_cursor.close();

        return mi_payInfo;
    }

    //Recuperar Pago por nombre de Tarjeta a una clase Lista CardInfo
    public List<PayInfo> recuperarPagoPorNombreTarjeta(String tarjetaID) {

        SQLiteDatabase db = getReadableDatabase();
        List<PayInfo> lista_pago= new ArrayList<>();
        String[] valores_recuperados = {COLUMN_ID,
                COLUMN_DETALLEPAGO, COLUMN_TARJETA, COLUMN_MONTO, COLUMN_FECHAPAGO, COLUMN_FECHAPAGOFORM,
                COLUMN_FECHA};

        Cursor mi_cursor = db.query(TABLE_NAME, valores_recuperados, COLUMN_TARJETA + "=?",
                new String[]{tarjetaID}, null, null, null , null);

        if (mi_cursor != null) {
            mi_cursor.moveToFirst();
        }

        do {
            PayInfo mi_payInfo = new PayInfo(mi_cursor.getInt(0),
                    mi_cursor.getString(1),
                    mi_cursor.getString(2),
                    mi_cursor.getDouble(3),
                    mi_cursor.getString(4),
                    mi_cursor.getString(5),
                    mi_cursor.getString(6)
            );
            lista_pago.add(mi_payInfo);
        } while (mi_cursor.moveToNext());
        db.close();
        mi_cursor.close();

        return lista_pago;

    }



    //Recuperación de todos los registros en una lista tipo CardInfo
    public List<PayInfo> recuperarPagos() {
        SQLiteDatabase db = getReadableDatabase();
        List<PayInfo> lista_pago= new ArrayList<>();
        String[] valores_recuperados = {COLUMN_ID,
                COLUMN_DETALLEPAGO, COLUMN_TARJETA, COLUMN_MONTO, COLUMN_FECHAPAGO, COLUMN_FECHAPAGOFORM,
                COLUMN_FECHA};

        Cursor mi_cursor = db.query(TABLE_NAME, valores_recuperados, null,
                null, null, null, null, null);

        if (mi_cursor != null) {
            mi_cursor.moveToFirst();
        }

        do {
            PayInfo mi_payInfo = new PayInfo(mi_cursor.getInt(0),
                    mi_cursor.getString(1),
                    mi_cursor.getString(2),
                    mi_cursor.getDouble(3),
                    mi_cursor.getString(4),
                    mi_cursor.getString(5),
                    mi_cursor.getString(6)
            );
            lista_pago.add(mi_payInfo);
        } while (mi_cursor.moveToNext());
        db.close();
        mi_cursor.close();

        return lista_pago;
    }

    //Para trabajar con ListView cuando sea necesario

    //Función Cursor todos los registros
    public Cursor fetchAllRegister() {
        SQLiteDatabase db = getReadableDatabase();

        String[] valores_recuperados = {COLUMN_ID,
                COLUMN_DETALLEPAGO, COLUMN_TARJETA, COLUMN_MONTO, COLUMN_FECHAPAGO, COLUMN_FECHAPAGOFORM,
                COLUMN_FECHA};

        Cursor mi_cursor = db.query(TABLE_NAME, valores_recuperados, null, null, null, null, null);

        if (mi_cursor != null) {
            mi_cursor.moveToFirst();
        }
        return mi_cursor;
    }

    //Funcion Cursor por nombre de tarjeta
    public Cursor fetchARegisterbyCardName(String NombreTarjeta) {
        SQLiteDatabase db = getReadableDatabase();

        String[] valores_recuperados = {COLUMN_ID,
                COLUMN_DETALLEPAGO, COLUMN_TARJETA, COLUMN_MONTO, COLUMN_FECHAPAGO, COLUMN_FECHAPAGOFORM,
                COLUMN_FECHA};

        Cursor mi_cursor = db.query(true, TABLE_NAME, valores_recuperados,  COLUMN_TARJETA + " like ?" , new String[]{NombreTarjeta + "%"}, null, null, null, null, null);

        if (mi_cursor != null) {
            mi_cursor.moveToFirst();
        }
        return mi_cursor;
    }

    //Recuperación de una fila por nombre en una lista String
    public List<String> recuperarFila(String nombrePago){
        //Recuperación de la base de datos
        SQLiteDatabase db = this.getReadableDatabase();

        //Grabar en una lista todos los registros
        List<String> lista_registro = new ArrayList<String>();
        String[] valores_recuperados = {COLUMN_ID,
                COLUMN_DETALLEPAGO, COLUMN_TARJETA, COLUMN_MONTO, COLUMN_FECHAPAGO, COLUMN_FECHAPAGOFORM,
                COLUMN_FECHA};

        //Recuperación de los datos en un cursor
        Cursor c = db.query(TABLE_NAME, valores_recuperados,COLUMN_DETALLEPAGO + "=?" , new String[]{nombrePago}, null, null, null);

        //Moverse al inicio del registro
        if ( c != null) {
            c.moveToFirst();
        }

        try {
            do {
                lista_registro.add(Integer.toString(c.getInt(0))); //Pago ID
                lista_registro.add(c.getString(1)); //Nombre Pago
                lista_registro.add(c.getString(2)); //Nombre Tarjeta
                lista_registro.add(Double.toString(c.getDouble(3))); //Monto
                lista_registro.add(c.getString(4)); //Fecha de pago
                lista_registro.add(c.getString(5)); //Fecha de pago con formato
                lista_registro.add(c.getString(6)); //Fecha creación registro
            } while (c.moveToNext());
        }
        catch (CursorIndexOutOfBoundsException myexception)
        {
            //Código faltante
        }

        return lista_registro;
    }

}
