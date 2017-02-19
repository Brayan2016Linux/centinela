package com.example.imagineup.centinela;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.03
 * @since 2016-05-21 2016-05-26 2016-06-02
 */

public class BankFeesDBHelper extends SQLiteOpenHelper {

    //Constantes principales de la clase
    private static final String DATABASE_NAME ="COMISION.db";
    private static final int DATABASE_VERSION = 1;

    //Constantes de la tabla
    private static final String TABLE_NAME = "DATOS_COMISION";

    //Nombre de las columnas
    private static final String COLUMN_ID ="_id";
    private static final String COLUMN_TARJETA ="tarjetaID";
    private static final String COLUMN_COMISION_ADMINISTRATIVA="ComisionAdministiva";
    private static final String COLUMN_COMISION_SEGURO="ComisionSeguro";
    private static final String COLUMN_COMISION_ADELANTO_ADM="ComisionAdelantoADM";
    private static final String COLUMN_COMISION_ADELANTO_PORC="ComisionAdelantoPORC";
    private static final String COLUMN_COMISION_MORAUNICO="ComisionMoraUnico";
    private static final String COLUMN_COMISION_MORA230="ComisionMora230";
    private static final String COLUMN_COMISION_MORA3190="ComisionMora3190";
    private static final String COLUMN_COMISION_MORA91180="ComisionMora91180";
    private static final String COLUMN_FECHA="fecha";

    //Constructor de la clase
    public BankFeesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creación de las entradas de la tabla
    public void onCreate(SQLiteDatabase database)
    {
        String CREATE_ITEM_TABLE="CREATE TABLE " + TABLE_NAME +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_TARJETA + " TEXT, "
                + COLUMN_COMISION_ADMINISTRATIVA + " TEXT, "
                + COLUMN_COMISION_SEGURO + " TEXT, "
                + COLUMN_COMISION_ADELANTO_ADM + " TEXT, "
                + COLUMN_COMISION_ADELANTO_PORC + " TEXT, "
                + COLUMN_COMISION_MORAUNICO + " TEXT, "
                + COLUMN_COMISION_MORA230 + " TEXT, "
                + COLUMN_COMISION_MORA3190 + " TEXT, "
                + COLUMN_COMISION_MORA91180 + " TEXT, "
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


    //Gestión de la base de datos COMISIONES número de registro(ID) y fecha son automáticos
    public void insertarCOMISION(String tarjetaID,
                                 double MontoComisionAdministrativa,
                                 double MontoComisionSeguro,
                                 double MontoComisionAdelantoAdm,
                                 double MontoComisionAdelantoPorc,
                                 double MontoComisionMoraUnico,
                                 double MontoComisionMora230,
                                 double MontoComisionMora3190,
                                 double MontoComisionMora91180)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String fecha_actual = establecerFechaSinFormato();
        //int IDCOMISION = numberOfRows()+1; se hace en forma automática

        if(db != null)
        {
            ContentValues valoresComision = new ContentValues();
            //valoresCompra.put(COLUMN_ID, IDCOMISION);
            valoresComision.put(COLUMN_TARJETA, tarjetaID);
            valoresComision.put(COLUMN_COMISION_ADMINISTRATIVA, MontoComisionAdministrativa);
            valoresComision.put(COLUMN_COMISION_SEGURO, MontoComisionSeguro);
            valoresComision.put(COLUMN_COMISION_ADELANTO_ADM, MontoComisionAdelantoAdm);
            valoresComision.put(COLUMN_COMISION_ADELANTO_PORC, MontoComisionAdelantoPorc);
            valoresComision.put(COLUMN_COMISION_MORAUNICO, MontoComisionMoraUnico);
            valoresComision.put(COLUMN_COMISION_MORA230, MontoComisionMora230);
            valoresComision.put(COLUMN_COMISION_MORA3190, MontoComisionMora3190);
            valoresComision.put(COLUMN_COMISION_MORA91180, MontoComisionMora91180);
            valoresComision.put(COLUMN_FECHA, fecha_actual);

            db.insert(TABLE_NAME, null, valoresComision);
            db.close();
        }
    }

    //Actualizar un registro
    public void actualizarCOMISION(int idCOMISION,
                                   String tarjetaID,
                                   double MontoComisionAdministrativa,
                                   double MontoComisionSeguro,
                                   double MontoComisionAdelantoAdm,
                                   double MontoComisionAdelantoPorc,
                                   double MontoComisionMoraUnico,
                                   double MontoComisionMora230,
                                   double MontoComisionMora3190,
                                   double MontoComisionMora91180)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String fecha_actual = establecerFecha();

        ContentValues valoresComision = new ContentValues();

        valoresComision.put(COLUMN_ID, idCOMISION);
        valoresComision.put(COLUMN_TARJETA, tarjetaID);
        valoresComision.put(COLUMN_COMISION_ADMINISTRATIVA, MontoComisionAdministrativa);
        valoresComision.put(COLUMN_COMISION_SEGURO, MontoComisionSeguro);
        valoresComision.put(COLUMN_COMISION_ADELANTO_ADM, MontoComisionAdelantoAdm);
        valoresComision.put(COLUMN_COMISION_ADELANTO_PORC, MontoComisionAdelantoPorc);
        valoresComision.put(COLUMN_COMISION_MORAUNICO, MontoComisionMoraUnico);
        valoresComision.put(COLUMN_COMISION_MORA230, MontoComisionMora230);
        valoresComision.put(COLUMN_COMISION_MORA3190, MontoComisionMora3190);
        valoresComision.put(COLUMN_COMISION_MORA91180, MontoComisionMora91180);
        valoresComision.put(COLUMN_FECHA, fecha_actual);

        db.update(TABLE_NAME, valoresComision, COLUMN_ID + "=?", new String[]{Integer.toString(idCOMISION)});
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

    //Función Cursor todos los registros
    public Cursor fetchAllRegister() {
        SQLiteDatabase db = getReadableDatabase();

        String[] valores_recuperados = {COLUMN_ID, COLUMN_TARJETA,COLUMN_COMISION_ADMINISTRATIVA,
                COLUMN_COMISION_SEGURO, COLUMN_COMISION_ADELANTO_ADM, COLUMN_COMISION_ADELANTO_PORC, COLUMN_COMISION_MORAUNICO,
                COLUMN_COMISION_MORA230, COLUMN_COMISION_MORA3190, COLUMN_COMISION_MORA91180, COLUMN_FECHA};

        Cursor mi_cursor = db.query(TABLE_NAME, valores_recuperados, null, null, null, null, null);

        if (mi_cursor != null) {
            mi_cursor.moveToFirst();
        }
        return mi_cursor;
    }

    //Funcion Cursor por nombre de tarjeta
    public Cursor fetchARegisterbyCardName(String NombreTarjeta) {
        SQLiteDatabase db = getReadableDatabase();

        String[] valores_recuperados = {COLUMN_ID, COLUMN_TARJETA,COLUMN_COMISION_ADMINISTRATIVA,
                COLUMN_COMISION_SEGURO, COLUMN_COMISION_ADELANTO_ADM, COLUMN_COMISION_ADELANTO_PORC, COLUMN_COMISION_MORAUNICO,
                COLUMN_COMISION_MORA230, COLUMN_COMISION_MORA3190, COLUMN_COMISION_MORA91180, COLUMN_FECHA};

        Cursor mi_cursor = db.query(true, TABLE_NAME, valores_recuperados,  COLUMN_TARJETA + " like ?" , new String[]{NombreTarjeta + "%"}, null, null, null, null, null);

        if (mi_cursor != null) {
            mi_cursor.moveToFirst();
        }
        return mi_cursor;
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
    public BankFeesInfo recuperarComision(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperados = {COLUMN_ID, COLUMN_TARJETA,COLUMN_COMISION_ADMINISTRATIVA,
                COLUMN_COMISION_SEGURO, COLUMN_COMISION_ADELANTO_ADM, COLUMN_COMISION_ADELANTO_PORC,COLUMN_COMISION_MORAUNICO,
                COLUMN_COMISION_MORA230, COLUMN_COMISION_MORA3190, COLUMN_COMISION_MORA91180, COLUMN_FECHA};

        Cursor mi_cursor = db.query(TABLE_NAME, valores_recuperados, COLUMN_ID + "=?" ,
                new String[]{Integer.toString(id)}, null, null, null, null);

        if (mi_cursor != null)
        {
            mi_cursor.moveToFirst();
        }

        BankFeesInfo mi_adelantoInfo = new BankFeesInfo(mi_cursor.getInt(0),
                mi_cursor.getString(1),
                mi_cursor.getDouble(2),
                mi_cursor.getDouble(3),
                mi_cursor.getDouble(4),
                mi_cursor.getDouble(5),
                mi_cursor.getDouble(6),
                mi_cursor.getDouble(7),
                mi_cursor.getDouble(8),
                mi_cursor.getDouble(9),
                mi_cursor.getString(10)
        );


        db.close();
        mi_cursor.close();

        return mi_adelantoInfo;
    }

    //Recuperar saldo por nombre de Tarjeta a una clase CardBalanceInfo
    public BankFeesInfo recuperarNombreTarjeta(String tarjetaID) {

        SQLiteDatabase db = getReadableDatabase();

        String[] valores_recuperados = {COLUMN_ID, COLUMN_TARJETA,COLUMN_COMISION_ADMINISTRATIVA,
                COLUMN_COMISION_SEGURO, COLUMN_COMISION_ADELANTO_ADM, COLUMN_COMISION_ADELANTO_PORC, COLUMN_COMISION_MORAUNICO,
                COLUMN_COMISION_MORA230, COLUMN_COMISION_MORA3190, COLUMN_COMISION_MORA91180, COLUMN_FECHA};

        Cursor mi_cursor = db.query(TABLE_NAME, valores_recuperados, COLUMN_TARJETA + "=?",
                new String[]{tarjetaID}, null, null, null , null);

        if (mi_cursor != null)
        {
            mi_cursor.moveToFirst();
        }

        BankFeesInfo mi_comisionInfo = new BankFeesInfo(mi_cursor.getInt(0),
                mi_cursor.getString(1),
                mi_cursor.getDouble(2),
                mi_cursor.getDouble(3),
                mi_cursor.getDouble(4),
                mi_cursor.getDouble(5),
                mi_cursor.getDouble(6),
                mi_cursor.getDouble(7),
                mi_cursor.getDouble(8),
                mi_cursor.getDouble(9),
                mi_cursor.getString(10)
        );


        db.close();
        mi_cursor.close();

        return mi_comisionInfo;

    }



    //Recuperación de todos los registros en una lista tipo CardBalanceInfo
    public List<BankFeesInfo> recuperarComisiones() {
        SQLiteDatabase db = getReadableDatabase();
        List<BankFeesInfo> lista_comisiones= new ArrayList<>();
        String[] valores_recuperados = {COLUMN_ID, COLUMN_TARJETA,COLUMN_COMISION_ADMINISTRATIVA,
                COLUMN_COMISION_SEGURO, COLUMN_COMISION_ADELANTO_ADM, COLUMN_COMISION_ADELANTO_PORC, COLUMN_COMISION_MORAUNICO,
                COLUMN_COMISION_MORA230, COLUMN_COMISION_MORA3190, COLUMN_COMISION_MORA91180, COLUMN_FECHA};

        Cursor mi_cursor = db.query(TABLE_NAME, valores_recuperados, null,
                null, null, null, null, null);

        if (mi_cursor != null) {
            mi_cursor.moveToFirst();
        }

        do {
            BankFeesInfo mi_comisionInfo = new BankFeesInfo(mi_cursor.getInt(0),
                    mi_cursor.getString(1),
                    mi_cursor.getDouble(2),
                    mi_cursor.getDouble(3),
                    mi_cursor.getDouble(4),
                    mi_cursor.getDouble(5),
                    mi_cursor.getDouble(6),
                    mi_cursor.getDouble(7),
                    mi_cursor.getDouble(8),
                    mi_cursor.getDouble(9),
                    mi_cursor.getString(10)
            );
            lista_comisiones.add(mi_comisionInfo);
        } while (mi_cursor.moveToNext());
        db.close();
        mi_cursor.close();

        return lista_comisiones;
    }

    //Recuperación de una fila por nombre en una lista String
    public List<String> recuperarFila(String nombreTarjeta){
        //Recuperación de la base de datos
        SQLiteDatabase db = this.getReadableDatabase();

        //Grabar en una lista todos los registros
        List<String> lista_registro = new ArrayList<String>();
        String[] valores_recuperados = {COLUMN_ID, COLUMN_TARJETA,COLUMN_COMISION_ADMINISTRATIVA,
                COLUMN_COMISION_SEGURO, COLUMN_COMISION_ADELANTO_ADM, COLUMN_COMISION_ADELANTO_PORC, COLUMN_COMISION_MORAUNICO,
                COLUMN_COMISION_MORA230, COLUMN_COMISION_MORA3190, COLUMN_COMISION_MORA91180, COLUMN_FECHA};

        //Recuperación de los datos en un cursor
        Cursor c = db.query(TABLE_NAME, valores_recuperados,COLUMN_TARJETA + "=?" , new String[]{nombreTarjeta}, null, null, null);

        //Moverse al inicio del registro
        if ( c != null) {
            c.moveToFirst();
        }

        try {
            do {
                lista_registro.add(Integer.toString(c.getInt(0))); //Adelanto ID
                lista_registro.add(c.getString(1)); //Nombre Tarjeta
                lista_registro.add(Double.toString(c.getDouble(2))); //Comision Administrativa
                lista_registro.add(Double.toString(c.getDouble(3))); //Comision Seguro
                lista_registro.add(Double.toString(c.getDouble(4))); //Comision Adelanto Administrativa
                lista_registro.add(Double.toString(c.getDouble(5))); //Comision Adelanto Porcentaje
                lista_registro.add(Double.toString(c.getDouble(6))); //Comision Mora Unico
                lista_registro.add(Double.toString(c.getDouble(7))); //Comision Mora 2 - 30
                lista_registro.add(Double.toString(c.getDouble(8))); //Comision Mora 31 - 90
                lista_registro.add(Double.toString(c.getDouble(9))); //Comision Mora 91 - 180
                lista_registro.add(c.getString(10)); //Fecha creación registro
            } while (c.moveToNext());
        }
        catch (CursorIndexOutOfBoundsException myexception)
        {
            //Código faltante
        }

        return lista_registro;
    }

}
