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
 * @since 2015-12-08 2016-03-02 2016-03-03
 */

public class CardDBHelper extends SQLiteOpenHelper {

    //Constantes principales de la clase
    private static final String DATABASE_NAME ="TARJETAS.db";
    private static final int DATABASE_VERSION = 1;

    //Constantes de la tabla
    private static final String TABLE_NAME = "DATOS_TARJETA";

    //Nombre de las columnas
    public static final String COLUMN_ID ="_id";
    public static final String COLUMN_IDUSER="iduser";
    public static final String COLUMN_NOMBRE="nombre";
    public static final String COLUMN_EMISOR ="emisor";
    public static final String COLUMN_DIASCORTE = "dias_corte";
    public static final String COLUMN_DIASGRACIA="dias_gracia";
    public static final String COLUMN_FECHA="fecha";

    //Constructor de la clase
    public CardDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creación de las entradas de la tabla
    public void onCreate(SQLiteDatabase database)
    {
        String CREATE_ITEM_TABLE="CREATE TABLE " + TABLE_NAME +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_IDUSER + " TEXT, "
                + COLUMN_NOMBRE + " TEXT, "
                + COLUMN_EMISOR + " TEXT, "
                + COLUMN_DIASCORTE + " TEXT, "
                + COLUMN_DIASGRACIA + " TEXT, "
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

    public void VacuumSQLiteDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        //Aplica el VACUUM
        db.execSQL("VACUUM");
        db.close();
    }


    //Gestión de la base de datos TARJETA número de registro(ID) y fecha son automáticos
    public void insertarTARJETA(String idUSER, String nombreID, String emisorID,
                                int diascorte, int diasgracia)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String fecha_actual = establecerFechaSinFormato();
        //Se hace automático int IDTARJETA = numberOfRows()+1;

        if(db != null)
        {
            ContentValues valoresTarjeta = new ContentValues();
            //valoresTarjeta.put(COLUMN_ID, IDTARJETA);
            valoresTarjeta.put(COLUMN_IDUSER, idUSER);
            valoresTarjeta.put(COLUMN_NOMBRE, nombreID);
            valoresTarjeta.put(COLUMN_EMISOR, emisorID);
            valoresTarjeta.put(COLUMN_DIASCORTE, diascorte);
            valoresTarjeta.put(COLUMN_DIASGRACIA, diasgracia);
            valoresTarjeta.put(COLUMN_FECHA, fecha_actual);

            db.insert(TABLE_NAME, null, valoresTarjeta);
            db.close();
        }
    }

    //Actualizar un registro
    public void actualizarTarjeta(int idTARJETA, String idUSER, String nombreID, String emisorID,
                                  int diascorte, int diasgracia)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String fecha_actual = establecerFechaSinFormato();

        ContentValues valoresTarjeta = new ContentValues();

        valoresTarjeta.put(COLUMN_ID, idTARJETA);
        valoresTarjeta.put(COLUMN_IDUSER, idUSER);
        valoresTarjeta.put(COLUMN_NOMBRE, nombreID);
        valoresTarjeta.put(COLUMN_EMISOR, emisorID);
        valoresTarjeta.put(COLUMN_DIASCORTE, diascorte);
        valoresTarjeta.put(COLUMN_DIASGRACIA, diasgracia);
        valoresTarjeta.put(COLUMN_FECHA, fecha_actual);

        db.update(TABLE_NAME, valoresTarjeta, COLUMN_ID +"=?", new String[]{Integer.toString(idTARJETA)});
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


    //Borra todos los registros con el identificador dado
    public void DeleteAllRegisterIdentified(String userNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,COLUMN_IDUSER + "=?", new String[]{userNumber});
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
    public CardInfo recuperarTarjeta(String id) {
        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperados = {COLUMN_ID, COLUMN_IDUSER,
                COLUMN_NOMBRE, COLUMN_EMISOR,
                COLUMN_DIASCORTE,COLUMN_DIASGRACIA, COLUMN_FECHA};

        Cursor mi_cursor = db.query(TABLE_NAME, valores_recuperados, COLUMN_ID + "=?",
                new String[]{id}, null, null, null, null);

        if (mi_cursor != null)
        {
            mi_cursor.moveToFirst();
        }

        CardInfo mi_cardInfo = new CardInfo(mi_cursor.getInt(0),
                mi_cursor.getString(1),
                mi_cursor.getString(2),
                mi_cursor.getString(3),
                mi_cursor.getInt(4),
                mi_cursor.getInt(5),
                mi_cursor.getString(6)
        );


        db.close();
        mi_cursor.close();

        return mi_cardInfo;
    }

    //Recuperar Tarjeta por nombre de Tarjeta a una clase CardInfo
    public CardInfo recuperarNombreTarjeta(String TarjetaID) {

        SQLiteDatabase db = getReadableDatabase();

        String[] valores_recuperados = {COLUMN_ID,COLUMN_IDUSER,
                COLUMN_NOMBRE, COLUMN_EMISOR,
                COLUMN_DIASCORTE,COLUMN_DIASGRACIA, COLUMN_FECHA};

        Cursor mi_cursor = db.query(TABLE_NAME, valores_recuperados, COLUMN_NOMBRE + "=?",
                new String[]{TarjetaID}, null, null, null , null);

        if (mi_cursor != null)
        {
            mi_cursor.moveToFirst();
        }

        CardInfo mi_cardInfo = new CardInfo(mi_cursor.getInt(0),
                mi_cursor.getString(1),
                mi_cursor.getString(2),
                mi_cursor.getString(3),
                mi_cursor.getInt(4),
                mi_cursor.getInt(5),
                mi_cursor.getString(6)
        );


        db.close();
        mi_cursor.close();

        return mi_cardInfo;

    }

    //Recuperación de todos los registros en una lista tipo CardInfo por número de usuario
    public List<CardInfo> recuperarTarjetas(String UserID) {
        SQLiteDatabase db = getReadableDatabase();
        List<CardInfo> lista_tarjetas= new ArrayList<>();
        String[] valores_recuperados = {COLUMN_ID, COLUMN_IDUSER,
                COLUMN_NOMBRE, COLUMN_EMISOR,
                COLUMN_DIASCORTE,COLUMN_DIASGRACIA, COLUMN_FECHA};

        Cursor mi_cursor = db.query(TABLE_NAME, valores_recuperados, COLUMN_IDUSER + "=?", new String[]{UserID}, null, null, null, null);

        if (mi_cursor != null) {
            mi_cursor.moveToFirst();
            do {
                CardInfo mi_cardInfo = new CardInfo(mi_cursor.getInt(0),
                        mi_cursor.getString(1),
                        mi_cursor.getString(2),
                        mi_cursor.getString(3),
                        mi_cursor.getInt(4),
                        mi_cursor.getInt(5),
                        mi_cursor.getString(6)
                );
                lista_tarjetas.add(mi_cardInfo);
            } while (mi_cursor.moveToNext());
            db.close();
            mi_cursor.close();
        }

        return lista_tarjetas;
    }


    //Recuperación de todos los registros en una lista tipo CardInfo
    public List<CardInfo> recuperarTarjetas() {
        SQLiteDatabase db = getReadableDatabase();
        List<CardInfo> lista_tarjetas= new ArrayList<>();
        String[] valores_recuperados = {COLUMN_ID, COLUMN_IDUSER,
                COLUMN_NOMBRE, COLUMN_EMISOR,
                COLUMN_DIASCORTE,COLUMN_DIASGRACIA, COLUMN_FECHA};

        Cursor mi_cursor = db.query(TABLE_NAME, valores_recuperados, null,
                null, null, null, null, null);

        if (mi_cursor != null) {
            mi_cursor.moveToFirst();
            do {
                CardInfo mi_cardInfo = new CardInfo(mi_cursor.getInt(0),
                        mi_cursor.getString(1),
                        mi_cursor.getString(2),
                        mi_cursor.getString(3),
                        mi_cursor.getInt(4),
                        mi_cursor.getInt(5),
                        mi_cursor.getString(6)
                );
                lista_tarjetas.add(mi_cardInfo);
            } while (mi_cursor.moveToNext());
            db.close();
            mi_cursor.close();
        }

        return lista_tarjetas;
    }

    //Recuperación de una fila por nombre en una lista String
    public List<String> recuperarFila(String nombreTarjeta){
        //Recuperación de la base de datos
        SQLiteDatabase db = this.getReadableDatabase();

        //Grabar en una lista todos los registros
        List<String> lista_registro = new ArrayList<String>();
        String[] valores_recuperados = {COLUMN_ID, COLUMN_IDUSER,
                COLUMN_NOMBRE, COLUMN_EMISOR,
                COLUMN_DIASCORTE,COLUMN_DIASGRACIA, COLUMN_FECHA};

        //Recuperación de los datos en un cursor
        Cursor c = db.query(TABLE_NAME, valores_recuperados, COLUMN_NOMBRE + "=?", new String[]{nombreTarjeta}, null, null, null, null);
        //Moverse al inicio del registro

        if (c != null) {
            c.moveToFirst();

            try {
                do {
                    lista_registro.add(Integer.toString(c.getInt(0))); //Card ID
                    lista_registro.add(c.getString(1)); //Identificación del Usuario
                    lista_registro.add(c.getString(2)); //Nombre Tarjeta
                    lista_registro.add(c.getString(3)); //Nombre Emisor
                    lista_registro.add(Integer.toString(c.getInt(4))); //Saldo Dia de Corte
                    lista_registro.add(Integer.toString(c.getInt(5))); //Saldo Dias de gracia
                    lista_registro.add(c.getString(6)); //Fecha creación registro
                } while (c.moveToNext());
            } catch (CursorIndexOutOfBoundsException myexception) {
                //Código faltante
            }
        }

        return lista_registro;
    }

    //Borra todos los registros
    public void DeleteAllRegister() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.close();
    }

    //Para trabajar con ListView cuando sea necesario

    //Función Cursor todos los registros
    public Cursor fetchAllRegister() {
        SQLiteDatabase db = getReadableDatabase();

        String[] valores_recuperados = {COLUMN_ID, COLUMN_IDUSER,
                COLUMN_NOMBRE, COLUMN_EMISOR,
                COLUMN_DIASCORTE,COLUMN_DIASGRACIA, COLUMN_FECHA};

        Cursor mi_cursor = db.query(TABLE_NAME, valores_recuperados, null, null, null, null, null);

        if (mi_cursor != null) {
            mi_cursor.moveToFirst();
        }
        return mi_cursor;
    }

    //Funcion Cursor por nombre de tarjeta
    public Cursor fetchARegisterbyName(String NombreTarjeta) {
        SQLiteDatabase db = getReadableDatabase();

        String[] valores_recuperados = {COLUMN_ID, COLUMN_IDUSER,
                COLUMN_NOMBRE, COLUMN_EMISOR,
                COLUMN_DIASCORTE,COLUMN_DIASGRACIA, COLUMN_FECHA};

        Cursor mi_cursor = db.query(true, TABLE_NAME, valores_recuperados,  COLUMN_NOMBRE + " like ?" , new String[]{NombreTarjeta + "%"}, null, null, null, null, null);

        if (mi_cursor != null) {
            mi_cursor.moveToFirst();
        }
        return mi_cursor;
    }

    //Funcion Cursor por número de usuario Cédula o Pasaporte
    public Cursor fetchARegisterbyUSERNUMBER(String NumeroUsuario) {
        SQLiteDatabase db = getReadableDatabase();

        String[] valores_recuperados = {COLUMN_ID, COLUMN_IDUSER,
                COLUMN_NOMBRE, COLUMN_EMISOR,
                COLUMN_DIASCORTE,COLUMN_DIASGRACIA, COLUMN_FECHA};

        Cursor mi_cursor = db.query(true, TABLE_NAME, valores_recuperados,  COLUMN_IDUSER + " like ?" , new String[]{NumeroUsuario+"%"}, null, null, null, null, null);
        if (mi_cursor != null) {
            mi_cursor.moveToFirst();
        }
        return mi_cursor;
    }

}
