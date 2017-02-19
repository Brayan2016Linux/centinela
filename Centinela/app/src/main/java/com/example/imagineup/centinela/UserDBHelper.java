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
 * @since 2015-11-15
 */

public class UserDBHelper extends SQLiteOpenHelper {

    //Constantes principales de la clase
    private static final String DATABASE_NAME ="USUARIOS.db";
    private static final int DATABASE_VERSION = 1;

    //Constantes de la tabla
    private static final String TABLE_NAME = "DATOS_USUARIOS";

    //Nombre de las columnas
    private static final String COLUMN_ID ="_id";
    private static final String COLUMN_USUARIO="usuario";
    private static final String COLUMN_APELLIDO1="apellido1";
    private static final String COLUMN_APELLIDO2="apellido2";
    private static final String COLUMN_IDUSERCARD ="idusercard";
    private static final String COLUMN_CORREO ="correo";
    private static final String COLUMN_PASSWORD="password";
    private static final String COLUMN_FECHA="fecha";

    //Constructor de la clase
    public UserDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creación de las entradas de la tabla
    public void onCreate(SQLiteDatabase database)
    {
        String CREATE_ITEM_TABLE="CREATE TABLE " + TABLE_NAME +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_USUARIO + " TEXT, "
                + COLUMN_APELLIDO1 + " TEXT, "
                + COLUMN_APELLIDO2 + " TEXT, "
                + COLUMN_IDUSERCARD + " TEXT, "
                + COLUMN_CORREO + " TEXT, "
                + COLUMN_PASSWORD + " TEXT, "
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


    //Gestión de la base de datos USUARIO número de usuario y fecha son automáticos
    public void insertarUSUARIO(String nombre, String apellido1, String apellido2, String idusercard, String correo, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String fecha_actual = establecerFechaSinFormato();
        //int IDUser = numberOfRows()+1; Se hace en forma automática

        if(db != null)
        {
            ContentValues valoresUsuario = new ContentValues();
            //valoresUsuario.put(COLUMN_ID, IDUser);
            valoresUsuario.put(COLUMN_USUARIO, nombre);
            valoresUsuario.put(COLUMN_APELLIDO1, apellido1);
            valoresUsuario.put(COLUMN_APELLIDO2, apellido2);
            valoresUsuario.put(COLUMN_IDUSERCARD, idusercard);
            valoresUsuario.put(COLUMN_CORREO,correo);
            valoresUsuario.put(COLUMN_PASSWORD, password);
            valoresUsuario.put(COLUMN_FECHA, fecha_actual);

            db.insert(TABLE_NAME, null, valoresUsuario);
            db.close();
        }
    }

    //Actualizar un registro
    public void actualizarUSUARIO(int iduser, String nombre, String apellido1, String apellido2, String idusercard, String correo, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String fecha_actual = establecerFechaSinFormato();

        ContentValues valoresUsuario = new ContentValues();

        valoresUsuario.put(COLUMN_ID, iduser);
        valoresUsuario.put(COLUMN_USUARIO, nombre);
        valoresUsuario.put(COLUMN_APELLIDO1, apellido1);
        valoresUsuario.put(COLUMN_APELLIDO2, apellido2);
        valoresUsuario.put(COLUMN_IDUSERCARD, idusercard);
        valoresUsuario.put(COLUMN_CORREO,correo);
        valoresUsuario.put(COLUMN_PASSWORD, password);
        valoresUsuario.put(COLUMN_FECHA, fecha_actual);

        db.update(TABLE_NAME, valoresUsuario, COLUMN_ID + "=?", new String[]{Integer.toString(iduser)});
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
    public Usuario recuperarUsuario(int id) {
        SQLiteDatabase db = getReadableDatabase();
        String[] valores_recuperados = {COLUMN_ID, COLUMN_USUARIO, COLUMN_APELLIDO1, COLUMN_APELLIDO2, COLUMN_IDUSERCARD,
                 COLUMN_CORREO, COLUMN_PASSWORD, COLUMN_FECHA};

        Cursor mi_cursor = db.query(TABLE_NAME, valores_recuperados, COLUMN_ID + "=?" ,
                new String[]{Integer.toString(id)}, null, null, null, null);

        if (mi_cursor != null)
        {
            mi_cursor.moveToFirst();
        }

        Usuario mi_usuario = new Usuario(mi_cursor.getInt(0),
                mi_cursor.getString(1),
                mi_cursor.getString(2),
                mi_cursor.getString(3),
                mi_cursor.getString(4),
                mi_cursor.getString(5),
                mi_cursor.getString(6),
                mi_cursor.getString(7)
                );


        db.close();
        mi_cursor.close();

        return mi_usuario;
    }

    //Recuperar usuario por nombre de correo a una clase Usuario
    public Usuario recuperarUsuarioCorreo(String Correo) {

            SQLiteDatabase db = getReadableDatabase();
            Usuario mi_usuario;

            String[] valores_recuperados = {COLUMN_ID, COLUMN_USUARIO, COLUMN_APELLIDO1, COLUMN_APELLIDO2, COLUMN_IDUSERCARD,
                    COLUMN_CORREO, COLUMN_PASSWORD, COLUMN_FECHA};

            Cursor mi_cursor = db.query(TABLE_NAME, valores_recuperados, COLUMN_CORREO + "=?",
                    new String[]{Correo}, null, null, null , null);

            if (mi_cursor != null)
            {
                mi_cursor.moveToFirst();
            }

            mi_usuario = new Usuario(mi_cursor.getInt(0),
                        mi_cursor.getString(1),
                        mi_cursor.getString(2),
                        mi_cursor.getString(3),
                        mi_cursor.getString(4),
                        mi_cursor.getString(5),
                        mi_cursor.getString(6),
                        mi_cursor.getString(7)
                );


            db.close();
            mi_cursor.close();

            return mi_usuario;

    }



    //Recuperación de todos los registros en una lista tipo Usuario
    public List<Usuario> recuperarUsuarios() {
        SQLiteDatabase db = getReadableDatabase();
        List<Usuario> lista_usuarios= new ArrayList<>();
        String[] valores_recuperados = {COLUMN_ID, COLUMN_USUARIO, COLUMN_APELLIDO1, COLUMN_APELLIDO2, COLUMN_IDUSERCARD,
                COLUMN_CORREO, COLUMN_PASSWORD, COLUMN_FECHA};
        Cursor mi_cursor = db.query(TABLE_NAME, valores_recuperados, null,
                null, null, null, null, null);

        if (mi_cursor != null) {
            mi_cursor.moveToFirst();
            do {
                Usuario mi_usuario = new Usuario(mi_cursor.getInt(0),
                        mi_cursor.getString(1),
                        mi_cursor.getString(2),
                        mi_cursor.getString(3),
                        mi_cursor.getString(4),
                        mi_cursor.getString(5),
                        mi_cursor.getString(6),
                        mi_cursor.getString(7)
                );
                lista_usuarios.add(mi_usuario);
            } while (mi_cursor.moveToNext());
            db.close();
            mi_cursor.close();
        }

        return lista_usuarios;
    }

    //Recuperación de una fila por nombre en una lista String
    public List<String> recuperarFila(String Correo){
        //Recuperación de la base de datos
        SQLiteDatabase db = this.getReadableDatabase();

        //Grabar en una lista todos los registros
        List<String> lista_registro = new ArrayList<String>();
        String[] valores_recuperacion = {COLUMN_ID, COLUMN_USUARIO, COLUMN_APELLIDO1, COLUMN_APELLIDO2, COLUMN_IDUSERCARD,
                COLUMN_CORREO, COLUMN_PASSWORD, COLUMN_FECHA};

        //Recuperación de los datos en un cursor
        Cursor c = db.query(TABLE_NAME, valores_recuperacion,COLUMN_CORREO + "=?" , new String[]{Correo}, null, null, null);
        //Moverse al inicio del registro

        if (c != null) {
            c.moveToFirst();


            try {
                do {
                    lista_registro.add(Integer.toString(c.getInt(0))); //User ID
                    lista_registro.add(c.getString(1)); //Nombre Usuario
                    lista_registro.add(c.getString(2)); //Apellido1
                    lista_registro.add(c.getString(3)); //Apellido2
                    lista_registro.add(c.getString(4)); //IDCARD
                    lista_registro.add(c.getString(5)); //Correo
                    lista_registro.add(c.getString(6)); //Password
                    lista_registro.add(c.getString(7)); //Fecha
                } while (c.moveToNext());
            } catch (CursorIndexOutOfBoundsException myexception) {
                //Código faltante
            }
        }

        return lista_registro;
    }

}
