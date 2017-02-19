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

import java.text.DecimalFormat;
import java.util.List;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.03
 * @since 2015-12-13 2016-03-03 2016-05-22
 */

public class EditCard extends AppCompatActivity {

    private String cardName;
    private String userEmail;

    //variables a recuperar
    private int IDCard;
    private int IDBalance;
    private int IDInterest;
    private int IDBankFees;
    private String NombreID;
    private String EmisorID;
    private String UserID;
    private Double SaldoDisponible;
    private Double SaldoDeudorINT;
    private Double SaldoDeudorCERO;
    private Double Intereses;
    private Double Moratorios;
    private Double IntAdelanto;
    private int diaDeCorte;
    private int diasGracia;

    //base de datos
    private CardDBHelper myCardInfo;
    private BalanceDBHelper myBalanceInfo;
    private InterestDBHelper myInterestInfo;
    private UserDBHelper myUserInfo;

    //Inicialización de los widgets
    private EditText TarjetaNomb;
    private EditText EmisorTarj;
    private EditText SaldoDisp;
    private EditText SaldoDeudINT;
    private EditText SaldoDeudCERO;
    private EditText ComisionAdelanto;

    //Spinners
    private Spinner Spinner_Int;
    private Spinner Spinner_Mor;
    private Spinner Spinner_DC;
    private Spinner Spinner_DG;

    //Transitorios
    private int interes;
    private int morator;
    private int intadelanto;

    //Botones
    private Button saveButton;
    private Button cnlButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        //recuperación del correo del usuario de Actividad Login
        Bundle extras = getIntent().getExtras();
        cardName = extras.getString(getResources().getString(R.string.cardInfoID));
        userEmail = extras.getString(getResources().getString(R.string.userID));

        //inicialización base de datos
        myCardInfo = new CardDBHelper(getApplicationContext());
        myBalanceInfo = new BalanceDBHelper(getApplicationContext());
        myInterestInfo = new InterestDBHelper(getApplicationContext());

         /*Estructura de myCardInfo
        * ID
        * Cédula o Pasaporte
        * Nombre Tarjeta
        * Emisor
        * Dias Corte
        * Dias Gracia
        * Fecha de registro */

        /*Estructura de myBalanceInfo
        * ID
        * Nombre Tarjeta
        * Saldo Deudor sin Intereses
        * Saldo Deudor tasa Cero
        * Disponible
        * Fecha de registro*/

        /*Estructura de myInterestInfo
        * ID
        * Nombre Tarjeta
        * Interes Fijo
        * Interes Moratorio
        * Fecha de registro*/

        /*Estructura de myBankFeesInfo
        * ID
        * Nombre Tarjeta
        * Comision Adelanto
        * Fecha de registro*/

        /*Estructura de myCashAdvanceInfo
        * ID
        * IDAdelanto
        * Nombre Tarjeta
        * Monto Adelanto
        * Fecha Adelanto
        * Fecha de registro*/

        /*Estructura de myUserInfo
        * ID
        * Nombre
        * Apellido1
        * Apellido2
        * Cedula
        * Correo
        * Password
        * Fecha_registro*/

        final List<String> myCardINF = myCardInfo.recuperarFila(cardName);
        final List<String> myBalanceINF = myBalanceInfo.recuperarFila(cardName);
        final List<String> myInterestINF = myInterestInfo.recuperarFila(cardName);

        myCardInfo.close();
        myBalanceInfo.close();
        myInterestInfo.close();

        IDCard = Integer.parseInt(myCardINF.get(0));
        UserID = myCardINF.get(1);
        NombreID = myCardINF.get(2);
        EmisorID = myCardINF.get(3);
        diaDeCorte = Integer.parseInt(myCardINF.get(4));
        diasGracia = Integer.parseInt(myCardINF.get(5));

        IDBalance = Integer.parseInt(myBalanceINF.get(0));
        SaldoDeudorINT = Double.parseDouble(myBalanceINF.get(2));
        SaldoDeudorCERO = Double.parseDouble(myBalanceINF.get(3));
        SaldoDisponible = Double.parseDouble(myBalanceINF.get(4));

        IDInterest = Integer.parseInt(myInterestINF.get(0));
        Intereses = Double.parseDouble(myInterestINF.get(2));
        Moratorios = Double.parseDouble(myInterestINF.get(3));

        //convertir double to int
        interes = (int) Intereses.doubleValue();
        morator = (int) Moratorios.doubleValue();

        //Inicialización de los widgets
        //Widgets de EditTexto
        TarjetaNomb = (EditText) findViewById(R.id.tarjetaIDN);
        EmisorTarj = (EditText) findViewById(R.id.emisorIDN);
        SaldoDisp =(EditText) findViewById(R.id.dispIDN);
        SaldoDeudINT = (EditText) findViewById(R.id.deudorINT_IDN);
        SaldoDeudCERO = (EditText) findViewById(R.id.deudorCERO_IDN);

        //Formateo a dos Decimales
        DecimalFormat myformat = new DecimalFormat("0.00");

        //Setting de los widgets
        TarjetaNomb.setText(NombreID);
        EmisorTarj.setText(EmisorID);
        SaldoDisp.setText(myformat.format(SaldoDisponible));
        SaldoDeudINT.setText(myformat.format(SaldoDeudorINT));
        SaldoDeudCERO.setText(myformat.format(SaldoDeudorCERO));
        CreacionSpinner(interes, morator, diaDeCorte, diasGracia);

        //botones
        saveButton = (Button) findViewById(R.id.buttonOK);
        cnlButton = (Button) findViewById(R.id.button_CNL);

        //Listeners
        //en caso de presionar el botón guardar
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               try {
                   //Obtener los datos desde los widgets
                   String nombretarjeta = TarjetaNomb.getText().toString();
                   String emisor = EmisorTarj.getText().toString();
                   String disponible = SaldoDisp.getText().toString();
                   disponible = formateoDecimal(disponible);
                   String deudorINT = SaldoDeudINT.getText().toString();
                   deudorINT = formateoDecimal(deudorINT);
                   String deudorCERO = SaldoDeudCERO.getText().toString();
                   deudorCERO = formateoDecimal(deudorCERO);
                   int intereses = Spinner_Int.getSelectedItemPosition() + 1;
                   int moratorios = Spinner_Mor.getSelectedItemPosition() + 1;
                   int diascorte = Spinner_DC.getSelectedItemPosition() + 1;
                   int diasgracia = Spinner_DG.getSelectedItemPosition() + 1;


                   boolean comprobador = longitudString(nombretarjeta) &&
                           longitudString(emisor) &&
                           longitudString(disponible) &&
                           longitudString(deudorINT) &&
                           longitudString(deudorCERO);

                   if (comprobador) {

                       //Actualización de las bases de datos
                       actualizarDatos(IDCard,
                               IDBalance,
                               IDInterest,
                               UserID,
                               nombretarjeta,
                               emisor,
                               disponible,
                               deudorINT,
                               deudorCERO,
                               intereses,
                               moratorios,
                               diascorte,
                               diasgracia
                       );


                        mensajeOperacionExitosa();
                        finalizarActividad();

                        abrirActividadTarjetas(userEmail, UserID);

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

        //cerrar actividad sin cambio alguno
        cnlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirActividadTarjetas(userEmail,UserID);
            }
        });


    }

    //cierra la actividad actual
    private void finalizarActividad(){
        //cierre de actividad
        this.finish();
    }


    //Abre actividad correspondiente
    private void abrirActividadTarjetas(String userEmail, String usernumber){
        Intent miActividad = new Intent(this, ControlActivity.class);
        miActividad.putExtra(getResources().getString(R.string.userID), userEmail);
        miActividad.putExtra(getResources().getString(R.string.userNumber), usernumber);
        startActivity(miActividad);
        finalizarActividad();
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

    //Llenado de los Spinners
    private void CreacionSpinner(int Intereses, int Moratorios, int DiasCorte, int DiasGracia) {

        Spinner_Int = (Spinner) findViewById(R.id.spinnerINT);
        ArrayAdapter spinner_adapter_INT = ArrayAdapter.createFromResource(this, R.array.intereses, android.R.layout.simple_list_item_1);
        spinner_adapter_INT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_Int.setAdapter(spinner_adapter_INT);
        Spinner_Int.setSelection(Intereses - 1);

        Spinner_Mor = (Spinner) findViewById(R.id.spinnerMOR);
        ArrayAdapter spinner_adapter_MOR = ArrayAdapter.createFromResource(this, R.array.intereses, android.R.layout.simple_list_item_1);
        spinner_adapter_MOR.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_Mor.setAdapter(spinner_adapter_MOR);
        Spinner_Mor.setSelection(Moratorios - 1);

        Spinner_DC = (Spinner) findViewById(R.id.spinnerDC);
        ArrayAdapter spinner_adapter_DC = ArrayAdapter.createFromResource(this, R.array.spinner_days, android.R.layout.simple_list_item_1);
        spinner_adapter_DC.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_DC.setAdapter(spinner_adapter_DC);
        Spinner_DC.setSelection(DiasCorte - 1);

        Spinner_DG = (Spinner) findViewById(R.id.spinnerDG);
        ArrayAdapter spinner_adapter_DG = ArrayAdapter.createFromResource(this, R.array.intereses, android.R.layout.simple_list_item_1);
        spinner_adapter_DG.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_DG.setAdapter(spinner_adapter_DG);
        Spinner_DG.setSelection(DiasGracia - 1);
    }

    //Guardar datos en la base de datos
    private void actualizarDatos(int cardID,
                                 int balanceID,
                                 int interestID,
                                 String userID,
                                 String nombretarjeta,
                                 String emisorid,
                                 String sdisponible,
                                 String sdeudorINT,
                                 String sdeudorCERO,
                                 int porc_int,
                                 int porc_mor,
                                 int diascorte,
                                 int diasgracia){

        //Inicializador base de datos
        CardDBHelper myCard = new CardDBHelper(getApplicationContext());
        BalanceDBHelper myCardBalance = new BalanceDBHelper(getApplicationContext());
        InterestDBHelper myCardInterest = new InterestDBHelper(getApplicationContext());
        BankFeesDBHelper myBankFees = new BankFeesDBHelper(getApplicationContext());

        //conversión a double
        double disponible = Double.parseDouble(sdisponible);
        double deudorINT = Double.parseDouble(sdeudorINT);
        double deudorCERO = Double.parseDouble(sdeudorCERO);
        double interesFijo = porc_int;
        double interesMoratorio = porc_mor;

        //Actualización de las bases de datos
        myCard.actualizarTarjeta(cardID, userID, nombretarjeta, emisorid, diascorte, diasgracia);
        myCard.close();

        myCardBalance.actualizarSALDO(balanceID, nombretarjeta, deudorINT, deudorCERO, disponible);
        myCardBalance.close();

        myCardInterest.actualizarInteres(interestID, nombretarjeta, interesFijo, interesMoratorio);
        myCardInterest.close();
    }

}
