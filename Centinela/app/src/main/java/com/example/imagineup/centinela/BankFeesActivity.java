package com.example.imagineup.centinela;

import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.01
 * @since 2016-05-26
 */

public class BankFeesActivity extends AppCompatActivity {

    private String cardname;

    //Informacion a recuperar
    private int interesesAdelanto;
    private int idTransaction;

    private double montoComisionAdministrativa;
    private double montoComisionSeguro;
    private double montoComisionAdelantoAdm;
    private double montoComisionAdelantoPorc;
    private double montoComisionMoraUnica;
    private double montoComisionMora230;
    private double montoComisionMora3190;
    private double montoComisionMora91180;

    //Widgets
    TextView BankFeesTitle;

    EditText ComisionAdministrativa;
    EditText ComisionSeguro;
    EditText ComisionAdelantoAdministrativo;

    EditText ComisionMoraUnico;
    EditText ComisionMora230;
    EditText ComisionMora3190;
    EditText ComisionMora91180;

    Spinner Spinner_InteresesAdelanto;

    BankFeesDBHelper mycardBankFees;
    BankFeesInfo myBankFees;
    BankFeesInfo mytemporalBankFees;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankfees);

        //recuperación del correo y el número de usuario desde Actividad Login
        Bundle extras = getIntent().getExtras();
        cardname = extras.getString(getResources().getString(R.string.cardInfoID));

        BankFeesTitle = (TextView) findViewById(R.id.etiquetatarjetaTitulo);
        BankFeesTitle.setText(cardname);

        //Inicialización de widgets
        ComisionAdministrativa = (EditText) findViewById(R.id.comisionAdministrativaINP);
        ComisionSeguro = (EditText) findViewById(R.id.comisionSeguroINP);
        ComisionAdelantoAdministrativo = (EditText) findViewById(R.id.comisionAdelantoINP);
        ComisionMoraUnico = (EditText) findViewById(R.id.comisionMoraUnicoINP);
        ComisionMora230 = (EditText) findViewById(R.id.comision_2_30INP);
        ComisionMora3190 = (EditText) findViewById(R.id.comision_31_90INP);
        ComisionMora91180 = (EditText) findViewById(R.id.comision_91_180INP);

        myBankFees = recuperarvaloresComision(cardname);



        //numero de trasacción de control

        if (myBankFees != null) {
            Log.e("Comisiones: ", "recuperado exitosamente");
            idTransaction = myBankFees.obtenerIDTransaction();
            //recuperacion de valores
            montoComisionAdministrativa = myBankFees.obtenerComisionAdmnistrativa();
            montoComisionSeguro = myBankFees.obtenerComisionSeguro();
            montoComisionAdelantoAdm = myBankFees.obtenerComisionAdelantoAdminist();
            montoComisionAdelantoPorc = myBankFees.obtenerComisionAdelantoPorcent();
            montoComisionMoraUnica = myBankFees.obtenerComisionMoraUnico();
            montoComisionMora230 = myBankFees.obtenerComisionMora_2_30();
            montoComisionMora3190 = myBankFees.obtenerComisionMora_31_90();
            montoComisionMora91180 = myBankFees.obtenerComisionMora_91_180();
        }
        else {
            Log.e("Settings: ", "no existen se creara uno");

            //idTransaction = myBankFees.obtenerIDTransaction();
            //recuperacion de valores
            montoComisionAdministrativa = 0.0;
            montoComisionSeguro = 0.0;
            montoComisionAdelantoAdm = 0.0;
            montoComisionAdelantoPorc = 0.0;
            montoComisionMoraUnica = 0.0;
            montoComisionMora230 = 0.0;
            montoComisionMora3190 = 0.0;
            montoComisionMora91180 = 0.0;
        }

        interesesAdelanto = (int) montoComisionAdelantoPorc;

        //Formateo a dos Decimales
        DecimalFormat myformat = new DecimalFormat("0.00");
        ComisionAdministrativa.setText(myformat.format(montoComisionAdministrativa));
        ComisionSeguro.setText(myformat.format(montoComisionSeguro));
        ComisionAdelantoAdministrativo.setText(myformat.format(montoComisionAdelantoAdm));
        ComisionMoraUnico.setText(myformat.format(montoComisionMoraUnica));
        ComisionMora230.setText(myformat.format(montoComisionMora230));
        ComisionMora3190.setText(myformat.format(montoComisionMora3190));
        ComisionMora91180.setText(myformat.format(montoComisionMora91180));

        //Creación del spinner
        crearSpinner(interesesAdelanto);

        //Boton de Guardar
        Button GuardarBTN = (Button) findViewById(R.id.buttonOK);
        GuardarBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //recuperacion de valores desde los widgets
                montoComisionAdministrativa = Double.parseDouble(formateoDecimal(ComisionAdministrativa.getText().toString()));
                montoComisionSeguro = Double.parseDouble(formateoDecimal(ComisionSeguro.getText().toString()));
                montoComisionAdelantoAdm = Double.parseDouble(formateoDecimal(ComisionAdelantoAdministrativo.getText().toString()));
                montoComisionMoraUnica = Double.parseDouble(formateoDecimal(ComisionMoraUnico.getText().toString()));
                montoComisionMora230 = Double.parseDouble(formateoDecimal(ComisionMora230.getText().toString()));
                montoComisionMora3190 = Double.parseDouble(formateoDecimal(ComisionMora3190.getText().toString()));
                montoComisionMora91180 = Double.parseDouble(formateoDecimal(ComisionMora91180.getText().toString()));

                montoComisionAdelantoPorc = (double) recuperarValorSpinner();

                if (myBankFees != null )
                {
                    //Actualiza los datos de las comisiones
                    actualizarDatosComisiones(idTransaction,
                            montoComisionAdministrativa,
                            montoComisionSeguro,
                            montoComisionAdelantoAdm,
                            montoComisionAdelantoPorc,
                            montoComisionMoraUnica,
                            montoComisionMora230,
                            montoComisionMora3190,
                            montoComisionMora91180,
                            cardname
                    );
                }
                else
                {
                    //Guarda los datos de las comisiones
                    guardarDatosComisiones(
                            montoComisionAdministrativa,
                            montoComisionSeguro,
                            montoComisionAdelantoAdm,
                            montoComisionAdelantoPorc,
                            montoComisionMoraUnica,
                            montoComisionMora230,
                            montoComisionMora3190,
                            montoComisionMora91180,
                            cardname
                    );

                }

                cerrarActividad();
            }

        });


        //Botón de Cancelar
        Button CancelBTN = (Button) findViewById(R.id.button_CNL);
        CancelBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarActividad();
            }
        });

    }


    //*********************************************************************************************
    //Guardar datos en la base de datos
    private void guardarDatosComisiones(double montoComisionAdministrativa,
                                        double montoComisionSeguro,
                                        double montoComisionAdelantoAdm,
                                        double montoComisionAdelantoPorc,
                                        double montoComisionMoraUnica,
                                        double montoComisionMora230,
                                        double montoComisionMora3190,
                                        double montoComisionMora91180,
                                        String nombretarjeta) {

        //Inicializador base de datos comisiones
        BankFeesDBHelper myBankFees = new BankFeesDBHelper(getApplicationContext());

        myBankFees.insertarCOMISION(nombretarjeta,
                montoComisionAdministrativa,
                montoComisionSeguro,
                montoComisionAdelantoAdm,
                montoComisionAdelantoPorc,
                montoComisionMoraUnica,
                montoComisionMora230,
                montoComisionMora3190,
                montoComisionMora91180);

        myBankFees.close();
    }

    //Actualizar datos en la base de datos
    private void actualizarDatosComisiones(int idtransaction,
                                        double montoComisionAdministrativa,
                                        double montoComisionSeguro,
                                        double montoComisionAdelantoAdm,
                                        double montoComisionAdelantoPorc,
                                        double montoComisionMoraUnica,
                                        double montoComisionMora230,
                                        double montoComisionMora3190,
                                        double montoComisionMora91180,
                                        String nombretarjeta) {

        //Inicializador base de datos comisiones
        BankFeesDBHelper myBankFees = new BankFeesDBHelper(getApplicationContext());

        myBankFees.actualizarCOMISION(idtransaction,
                nombretarjeta,
                montoComisionAdministrativa,
                montoComisionSeguro,
                montoComisionAdelantoAdm,
                montoComisionAdelantoPorc,
                montoComisionMoraUnica,
                montoComisionMora230,
                montoComisionMora3190,
                montoComisionMora91180);

        myBankFees.close();
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

    //**************************************************************************************
    //cierra la actividad
    private void cerrarActividad(){
        this.finish();
    }

    //**************************************************************************************
    //Crea el Spinner de los días antes de pago
    private void crearSpinner (int interesesAdelantoPorc) {
        Spinner_InteresesAdelanto = (Spinner) findViewById(R.id.spinner_interesesAdelanto);
        ArrayAdapter spinner_adapter_IA = ArrayAdapter.createFromResource(this, R.array.interesAdelanto, android.R.layout.simple_list_item_1);
        spinner_adapter_IA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_InteresesAdelanto.setAdapter(spinner_adapter_IA);
        Spinner_InteresesAdelanto.setSelection(interesesAdelantoPorc);
    }

    private int recuperarValorSpinner() {
        int myvaluedays;
        myvaluedays = Spinner_InteresesAdelanto.getSelectedItemPosition();
        return myvaluedays;
    }


    //*************************************************************************************

    private BankFeesInfo recuperarvaloresComision(String cardname){

        try {
            BankFeesInfo mybankFeesInfo = new BankFeesInfo();
            BankFeesDBHelper bankFeesDB = new BankFeesDBHelper(getApplicationContext());
            mybankFeesInfo = bankFeesDB.recuperarNombreTarjeta(cardname);

            if (mybankFeesInfo != null) {

                mytemporalBankFees = mybankFeesInfo;
            }
            else {
                //si no existe registro lo crea
                mytemporalBankFees = new BankFeesInfo(cardname);
            }

            bankFeesDB.close();

        }catch (SQLiteException e) {
            Log.e("SQLERROR", " recuperarvaloresComision - BankFeesActivity");
        }catch (CursorIndexOutOfBoundsException e) {
            Log.e("Cursor ERROR", " recuperarvaloresComision - BankFeesActivity");
        } finally {
            Log.e("GarbageMem: "," recuperarvaloresComision - BankFeesActivity");
        }

        return mytemporalBankFees;
    }


}
