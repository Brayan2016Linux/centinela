package com.example.imagineup.centinela;

import android.database.CursorIndexOutOfBoundsException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.03
 * @since 2015-12-13 2016-03-02 2016-03-06
 */

public class ReviewCard extends AppCompatActivity {

    private String cardName;
    private String userEmail;

    //variables a recuperar
    private int IDCard;
    private String NombreID;
    private String EmisorID;
    private Double SaldoDisponible;
    private Double SaldoDeudorINT;
    private Double SaldoDeudorCERO;
    private Double Intereses;
    private Double Moratorios;
    private int diaDeCorte;
    private int diasGracia;
    private Double MontoComisionAdelantoADM;
    private Double MontoComisionAdelantoPORC;

    //Variables del balance
    private Double TotalTasaCero;
    private Double TotalComprasInt;
    private Double TotalDeuda;
    private Double InteresesCompras;
    private Double TotalPagos;
    private Double TotalSaldDisponibleFecha;
    private Double TotalSaldDeudaFecha;
    private Double MontoPagoMinimo;

    //base de datos
    private CardDBHelper myCardInfo;
    private BalanceDBHelper myBalanceInfo;
    private InterestDBHelper myInterestInfo;
    private BankFeesDBHelper myBankFeesInfo;

    //Widgets Info general de la tarjeta
    private TextView ShowCard;
    private TextView ShowEmisor;
    private TextView ShowDispon;
    private TextView ShowDeudorINT;
    private TextView ShowDeudorCERO;
    private TextView ShowInter;
    private TextView ShowMor;
    private TextView diasCorte;
    private TextView diasGrac;
    private TextView ShowcomisionAdelantoADM;
    private TextView ShowcomisionAdelantoPORC;

    //Widgets info general del balance a la fecha
    private TextView ShowTodayDate;
    private TextView ShowTotalTasaCero;
    private TextView ShowTotalComprasInt;
    private TextView ShowTotalDeuda;
    private TextView ShowInteresesCompras;
    private TextView ShowTotalPagos;
    private TextView ShowTotalSaldoDisp;
    private TextView ShowTotalSaldoDeuda;
    private TextView ShowMontoPagoMinimo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_cardinfo);

        //recuperación del correo del usuario de Actividad Login
        Bundle extras = getIntent().getExtras();
        cardName = extras.getString(getResources().getString(R.string.cardInfoID));

        //inicialización bases de datos
        myCardInfo = new CardDBHelper(getApplicationContext());
        myBalanceInfo = new BalanceDBHelper(getApplicationContext());
        myInterestInfo = new InterestDBHelper(getApplicationContext());
        myBankFeesInfo = new BankFeesDBHelper(getApplicationContext());

        //encuentra el entero del registro y lo pasa a la variable control

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

        /*Estructura de myBankFeestInfo
        * ID
        * Nombre Tarjeta
        * Monto Comision Administrativa
        * Monto Comision Seguro
        * Monto Comision Adelanto Administrativa
        * Monto Comision Adelanto Porcentaje
        * Monto Comisión Mora Único
        * Monto Comisión Mora  2  - 30
        * Monto Comisión Mora  31 - 90
        * Monto Comisión Mora 91 - 180
        * Fecha de registro*/


        final List<String> myCardINF = myCardInfo.recuperarFila(cardName);
        final List<String> myBalanceINF = myBalanceInfo.recuperarFila(cardName);
        final List<String> myInterestINF = myInterestInfo.recuperarFila(cardName);
        final List<String> myBankFeesINF = myBankFeesInfo.recuperarFila(cardName);

        IDCard = Integer.parseInt(myCardINF.get(0));
        NombreID = myCardINF.get(2);
        EmisorID = myCardINF.get(3);
        diaDeCorte = Integer.parseInt(myCardINF.get(4));
        diasGracia = Integer.parseInt(myCardINF.get(5));

        SaldoDeudorINT = Double.parseDouble(myBalanceINF.get(2));
        SaldoDeudorCERO = Double.parseDouble(myBalanceINF.get(3));
        SaldoDisponible = Double.parseDouble(myBalanceINF.get(4));

        Intereses = Double.parseDouble(myInterestINF.get(2));
        Moratorios = Double.parseDouble(myInterestINF.get(3));


        try {
        MontoComisionAdelantoADM = Double.parseDouble(myBankFeesINF.get(4));
        MontoComisionAdelantoPORC = Double.parseDouble(myBankFeesINF.get(5));
        }catch (IndexOutOfBoundsException ex) {
            Log.e("Index Excep", "ReviewCard");
        }


        //Inicialización de los widgets
        ShowCard = (TextView) findViewById(R.id.card_name);
        ShowEmisor = (TextView) findViewById(R.id.emisor_name);
        ShowDispon = (TextView) findViewById(R.id.saldo_disp);
        ShowDeudorINT = (TextView) findViewById(R.id.saldo_deudor);
        ShowDeudorCERO = (TextView) findViewById(R.id.saldo_deudor_cero);

        ShowInter = (TextView) findViewById(R.id.interes);
        ShowMor = (TextView) findViewById(R.id.moratorio);

        diasCorte = (TextView) findViewById(R.id.dia_corte);
        diasGrac = (TextView) findViewById(R.id.dia_gracia);

        ShowcomisionAdelantoADM = (TextView) findViewById(R.id.comision_adelanto_ADM);
        ShowcomisionAdelantoPORC = (TextView) findViewById(R.id.comision_adelantoPORC);

        //Mostrar los contenidos en los widgets
        ShowCard.setText(NombreID);
        ShowEmisor.setText(EmisorID);
        diasCorte.setText(Integer.toString(diaDeCorte));
        diasGrac.setText(Integer.toString(diasGracia));

        //Formateo a dos Decimales
        DecimalFormat myformat = new DecimalFormat("0.00");

        ShowDispon.setText(myformat.format(SaldoDisponible));
        ShowDeudorINT.setText(myformat.format(SaldoDeudorINT));
        ShowDeudorCERO.setText(myformat.format(SaldoDeudorCERO));

        ShowInter.setText(myformat.format(Intereses));
        ShowMor.setText(myformat.format(Moratorios));



        if (MontoComisionAdelantoADM != null){
            ShowcomisionAdelantoADM.setText(myformat.format(MontoComisionAdelantoADM));
        }
        else
        {
            ShowcomisionAdelantoADM.setText(myformat.format(0.0));
        }

        if (MontoComisionAdelantoPORC != null){
            ShowcomisionAdelantoPORC.setText(myformat.format(MontoComisionAdelantoPORC));
        }
        else
        {
            ShowcomisionAdelantoPORC.setText(myformat.format(0.0));
        }


        //Muestra la fecha de hoy del balance
        ShowTodayDate =(TextView) findViewById(R.id.today_date);
        Date todaydate = new Date();
        long mylongtodaydate = todaydate.getTime();
        SimpleDateFormat mydateformat = new SimpleDateFormat("dd/MM/yyyy");
        ShowTodayDate.setText(mydateformat.format(mylongtodaydate).toString());

        //Inicialización de los widgets del balance
        ShowTotalTasaCero=(TextView) findViewById(R.id.comprasTasaCero);
        ShowTotalComprasInt=(TextView) findViewById(R.id.comprasTasaInt);
        ShowTotalDeuda=(TextView) findViewById(R.id.totalDeuda);
        ShowInteresesCompras=(TextView) findViewById(R.id.InteresesCompras);
        ShowTotalPagos=(TextView) findViewById(R.id.PagosTotales);
        ShowTotalSaldoDisp=(TextView) findViewById(R.id.saldoDisponibleAprox);
        ShowTotalSaldoDeuda=(TextView) findViewById(R.id.saldoDeudorAprox);
        ShowMontoPagoMinimo=(TextView) findViewById(R.id.pagoMinimoAprox);

        //Cálculo de las variables Double para el balance
        TotalTasaCero = sumatoriaComprasTasaCero(cardName, mylongtodaydate);
        TotalComprasInt = sumatoriaComprasTasaInt(cardName, mylongtodaydate);
        InteresesCompras = (TotalComprasInt * Intereses) / 100;
        TotalDeuda = TotalTasaCero + TotalComprasInt + InteresesCompras;
        TotalPagos = sumatoriapagosTarjeta(cardName, mylongtodaydate);

        if(TotalPagos - InteresesCompras > 0)
            TotalSaldDisponibleFecha = SaldoDisponible - TotalComprasInt - InteresesCompras + TotalPagos;
        else
            TotalSaldDisponibleFecha = SaldoDisponible;

        TotalSaldDeudaFecha = TotalDeuda - TotalPagos;
        MontoPagoMinimo = (TotalDeuda + TotalSaldDeudaFecha) / 12; //Falta agregar Adelantos, no necesarios para el demo

        //Seteo del texto de los widgets
        ShowTotalTasaCero.setText(myformat.format(TotalTasaCero));
        ShowTotalComprasInt.setText(myformat.format(TotalComprasInt));
        ShowInteresesCompras.setText(myformat.format(InteresesCompras));
        ShowTotalDeuda.setText(myformat.format(TotalDeuda));

        ShowTotalPagos.setText(myformat.format(TotalPagos));

        if (TotalSaldDisponibleFecha > 0.0)
            ShowTotalSaldoDisp.setText(myformat.format(TotalSaldDisponibleFecha));
        else
            ShowTotalSaldoDisp.setText(myformat.format(0.0));

        if(TotalSaldDeudaFecha > 0.0)
            ShowTotalSaldoDeuda.setText(myformat.format(TotalSaldDeudaFecha));
        else
            ShowTotalSaldoDeuda.setText(myformat.format(0.0));

        if (MontoPagoMinimo > 0.0)
            ShowMontoPagoMinimo.setText(myformat.format(MontoPagoMinimo));
        else
            ShowMontoPagoMinimo.setText(myformat.format(0.0));

        //Click en botón cierra actividad
        LinearLayout outClick = (LinearLayout) findViewById(R.id.consultaOUT);

        outClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    //*************************Funciones de llamado y sumatorias de información de la base de datos

    //Compras tasa cero
    private Double sumatoriaComprasTasaCero(String nombreTarjeta, long fechaLong){
        Double totalCompras = 0.0;
        BuyDBHelper myshoppingDB = new BuyDBHelper(getApplicationContext());
        try {
            List<BuyInfo> mybuyListTemporal = myshoppingDB.recuperarCompraPorNombreTarjeta(nombreTarjeta);
            Iterator<BuyInfo> myiterator = mybuyListTemporal.iterator();
            BuyInfo compraTemporal;


            /* Estructura Base Datos Compras
            * IDTransaction;
            * CompraID;
            * TarjetaID;
            * MontoCredito;
            * PeriodoCredito;
            * Modalidad;
            * FechaCompra;
            * FechaCompraFormato;
            * fechacreacion;
            */

            while (myiterator.hasNext()) {

                compraTemporal = myiterator.next();

                if (Long.parseLong(compraTemporal.obtenerFecha()) <= fechaLong) {

                    if(compraTemporal.obtenerModalidad().equals(getResources().getString(R.string.interes_CERO)))
                        totalCompras = totalCompras + compraTemporal.obtenerMontoCredito();

                }
            }

            return totalCompras;

        }catch (CursorIndexOutOfBoundsException ex){
            return totalCompras;
        }
    }

    //Compras tasa intereses
    private Double sumatoriaComprasTasaInt(String nombreTarjeta, long fechaLong){
        Double totalCompras = 0.0;
        BuyDBHelper myshoppingDB = new BuyDBHelper(getApplicationContext());
        try {
            List<BuyInfo> mybuyListTemporal = myshoppingDB.recuperarCompraPorNombreTarjeta(nombreTarjeta);
            Iterator<BuyInfo> myiterator = mybuyListTemporal.iterator();
            BuyInfo compraTemporal;

            /* Estructura Base Datos Compras
            * IDTransaction;
            * CompraID;
            * TarjetaID;
            * MontoCredito;
            * PeriodoCredito;
            * Modalidad;
            * FechaCompra;
            * FechaCompraFormato;
            * fechacreacion;
            */

            while (myiterator.hasNext()) {

                compraTemporal = myiterator.next();

                if (Long.parseLong(compraTemporal.obtenerFecha()) <= fechaLong) {

                    if(compraTemporal.obtenerModalidad().equals(getResources().getString(R.string.interes_INT)))
                        totalCompras = totalCompras + compraTemporal.obtenerMontoCredito();

                }
            }

            return totalCompras;

        }catch (CursorIndexOutOfBoundsException ex){
            return totalCompras;
        }
    }


    //Pagos a tarjeta
    private Double sumatoriapagosTarjeta(String nombreTarjeta, long fechaLong){
        Double totalpagosTarjeta = 0.0;
        PayDBHelper mypaymentsDB = new PayDBHelper(getApplicationContext());
        try {
            List<PayInfo> mypayListTemporal = mypaymentsDB.recuperarPagoPorNombreTarjeta(nombreTarjeta);
            Iterator<PayInfo> myiterator = mypayListTemporal.iterator();
            PayInfo pagoTemporal;

        /*
        * IDTransaction;
        * PagoID;
        * TarjetaID;
        * MontoPago;
        * FechaPago;
        * FechaPagoFormato;
        * fechacreacion;
        * */

            while (myiterator.hasNext()) {

                pagoTemporal = myiterator.next();
                if (Long.parseLong(pagoTemporal.obtenerFecha()) <= fechaLong) {

                    totalpagosTarjeta = totalpagosTarjeta + pagoTemporal.obtenerMontoPago();
                }
            }

            return totalpagosTarjeta;
        } catch (CursorIndexOutOfBoundsException ex){
            return totalpagosTarjeta;
        }
    }

}
