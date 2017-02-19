package com.example.imagineup.centinela;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.03
 * @since 2015-12-09 2016-03-03 2016-05-22
 */

public class InfoCardActivity extends AppCompatActivity{

    //Spinner
    private Spinner Spinner_Int;
    private Spinner Spinner_Mor;
    private Spinner Spinner_DC;
    private Spinner Spinner_DG;

    //Widgets
    private EditText TarjetaNomb;
    private EditText EmisorTarj;
    private EditText SaldoDisp;
    private EditText SaldoDeudINT;
    private EditText SaldoDeudCERO;

    //Objeto base de datos CardInfo
    private CardDBHelper CardDB;

    //variables
    private String userID;
    private String nombreid;
    private String emisor;
    private String disponible;
    private String deudorINT;
    private String deudorCERO;
    private int intereses;
    private int moratorios;
    private int diasgracia;
    private int diascorte;

    //Variable Usuario de control
    private String userEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        //recuperación del correo del usuario de Actividad Login
        Bundle extras = getIntent().getExtras();
        userEmail = extras.getString(getResources().getString(R.string.userID));
        //userID = extras.getString(getResources().getString(R.string.userNumber));
        userID = recuperaIDUSER(userEmail);

        //Creación de los Spinners
        CreacionSpinner();

        //Widgets de EditTexto
        TarjetaNomb = (EditText) findViewById(R.id.tarjetaIDN);
        EmisorTarj = (EditText) findViewById(R.id.emisorIDN);
        SaldoDisp =(EditText) findViewById(R.id.dispIDN);
        SaldoDeudINT = (EditText) findViewById(R.id.deudorINT_IDN);
        SaldoDeudCERO = (EditText) findViewById(R.id.deudorCERO_IDN);

        //Boton de Guardar
        Button GuardarBTN = (Button) findViewById(R.id.buttonOK);
        GuardarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    //Obtener los datos desde los widgets
                    nombreid = TarjetaNomb.getText().toString();
                    emisor = EmisorTarj.getText().toString();
                    disponible = SaldoDisp.getText().toString();
                    disponible = formateoDecimal(disponible);
                    deudorINT = SaldoDeudINT.getText().toString();
                    deudorINT = formateoDecimal(deudorINT);
                    deudorCERO = SaldoDeudCERO.getText().toString();
                    deudorCERO = formateoDecimal(deudorCERO);

                    intereses = Spinner_Int.getSelectedItemPosition() + 1;
                    moratorios = Spinner_Mor.getSelectedItemPosition() + 1;
                    diascorte = Spinner_DC.getSelectedItemPosition() + 1;
                    diasgracia = Spinner_DG.getSelectedItemPosition() + 1;

                    //Inicializador base de datos
                    CardDB = new CardDBHelper(getApplicationContext());

                    boolean comprobador = longitudString(nombreid) &&
                            longitudString(emisor) &&
                            longitudString(disponible) &&
                            longitudString(deudorINT) &&
                            longitudString(deudorCERO);

                    if (comprobador) {
                        guardarDatos( userID,
                                nombreid,
                                emisor,
                                deudorINT,
                                deudorCERO,
                                disponible,
                                intereses,
                                moratorios,
                                diascorte,
                                diasgracia);

                        abrirActividadTarjetas(userEmail, userID);

                    } else {
                        mensajeCompleteCampos();
                    }

                }
                catch(NumberFormatException myexception){
                    mensajeDatosEquivocados();
                }
                catch (IllegalArgumentException myexc){
                    mensajeDatosEquivocados();
                }
            }

        });


        //Botón de Cancelar
        Button CancelBTN = (Button) findViewById(R.id.button_CNL);
        CancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirActividadTarjetas(userEmail, userID);
            }
        });


    }


    //Llenado de los Spinners
    private void CreacionSpinner() {

        Spinner_Int = (Spinner) findViewById(R.id.spinnerINT);
        ArrayAdapter spinner_adapter_INT = ArrayAdapter.createFromResource(this, R.array.intereses, android.R.layout.simple_list_item_1);
        spinner_adapter_INT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_Int.setAdapter(spinner_adapter_INT);

        Spinner_Mor = (Spinner) findViewById(R.id.spinnerMOR);
        ArrayAdapter spinner_adapter_MOR = ArrayAdapter.createFromResource(this, R.array.intereses, android.R.layout.simple_list_item_1);
        spinner_adapter_MOR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_Mor.setAdapter(spinner_adapter_MOR);

        Spinner_DC = (Spinner) findViewById(R.id.spinnerDC);
        ArrayAdapter spinner_adapter_DC = ArrayAdapter.createFromResource(this, R.array.spinner_days, android.R.layout.simple_list_item_1);
        spinner_adapter_DC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_DC.setAdapter(spinner_adapter_DC);

        Spinner_DG = (Spinner) findViewById(R.id.spinnerDG);
        ArrayAdapter spinner_adapter_DG = ArrayAdapter.createFromResource(this, R.array.intereses, android.R.layout.simple_list_item_1);
        spinner_adapter_DG.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_DG.setAdapter(spinner_adapter_DG);

    }


    //comprobador de longitud de string
    private boolean longitudString (String stringInput) {
        boolean resultado;
        if (stringInput.trim().length()>0)
            resultado = true;
        else
            resultado = false;

        return resultado;
    }

    //Mensaje para llenado de campos vacíos
    private void mensajeCompleteCampos() {
        String mensaje = getResources().getString(R.string.completeCamposVacios);
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    //Mensaje de operación exitosa
    private void mensajeOperacionExitosa() {
        String mensaje = getResources().getString(R.string.operacionExitosa);
        Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();
    }

    //Mensaje datos equivocados
    private void mensajeDatosEquivocados(){
        Toast.makeText(getApplicationContext(),getResources().getText(R.string.mensajeExcepcionTipo),Toast.LENGTH_SHORT).show();
    }


    //cierra la actividad actual
    private void finalizarActividad(){
        //cierre de actividad
        this.finish();
    }

    //formateo de captura de comas a punto decimal
    private String formateoDecimal(String numeroDecimal){
        String formatoDevuelto;
        StringBuffer myBuffer = new StringBuffer();

        for (int i = 0; i < numeroDecimal.length(); i++){
            if (numeroDecimal.charAt(i) == ','){
                myBuffer.append('.');
            }
            else {
                myBuffer.append(numeroDecimal.charAt(i));
            }
        }
        formatoDevuelto = myBuffer.toString();

        return formatoDevuelto;
    }

    //Guardar datos en la base de datos
    private void guardarDatos(String userID,
                              String nombretarjeta,
                              String emisorid,
                              String sdeudorINT,
                              String sdeudorCERO,
                              String sdisponible,
                              int porc_int,
                              int porc_mor,
                              int diascorte,
                              int diasgracia){

        //conversión a double
        double disponible = Double.parseDouble(sdisponible);
        double deudorINT = Double.parseDouble(sdeudorINT);
        double deudorCERO = Double.parseDouble(sdeudorCERO);
        double intereses = porc_int;
        double moratorios = porc_mor;

        //Inicializador base de datos
        CardDBHelper myCard = new CardDBHelper(getApplicationContext());
        BalanceDBHelper myCardBalance = new BalanceDBHelper(getApplicationContext());
        InterestDBHelper myCardInterest = new InterestDBHelper(getApplicationContext());

        myCard.insertarTARJETA(userID, nombretarjeta, emisorid, diascorte, diasgracia);
        myCard.close();

        myCardBalance.insertarSALDO(nombretarjeta, deudorINT, deudorCERO, disponible);
        myCardBalance.close();

        myCardInterest.insertarIntereses(nombretarjeta, intereses, moratorios);
        myCardInterest.close();

        mensajeOperacionExitosa();
        finalizarActividad();

    }

    //Abre actividad correspondiente
    private void abrirActividadTarjetas(String userEmail, String usernumber){
        Intent miActividad = new Intent(this, ControlActivity.class);
        miActividad.putExtra(getResources().getString(R.string.userID), userEmail);
        miActividad.putExtra(getResources().getString(R.string.userNumber), usernumber);
        startActivity(miActividad);

        finalizarActividad();
    }

    //recupera los datos del usuario
    private String recuperaIDUSER(String userEmail) {
        String userid;
        //Inicialización de la base de datos usuario para buscar lista
        UserDBHelper myUserInfo = new UserDBHelper(getApplicationContext());

        /*Estructura de myUserInfo
        * ID
        * Nombre
        * Apellido1
        * Apellido2
        * Cedula
        * Correo
        * Password
        * Fecha_registro*/

        List<String> myUserINF = myUserInfo.recuperarFila(userEmail);
        userid = myUserINF.get(4);
        myUserInfo.close();

        return userid;
    }

}
