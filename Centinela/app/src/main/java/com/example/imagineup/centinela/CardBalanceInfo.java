package com.example.imagineup.centinela;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by brpackage com.example.imagineup.centinela;

 import java.text.SimpleDateFormat;
 import java.util.Date;

 /**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.01
 * @since 2016-02-29
 */

public class CardBalanceInfo {

    /**
     * Variables descriptivas de la clase Usuario
     *
     * @param IDTransaction número del registro
     * @param TarjetaID identificador de la tarjeta
     * @param SaldoDeudorINT Saldo deudor compras con intereses
     * @param SaldoDeudorCEROINT Saldo deudor compras con cero INT
     * @param MontoDisponible Monto disponible
     * @param fechacreacion fecha de creación del registro
     */

    private int IDTransaction;
    private String TarjetaID;
    private Double SaldoDeudorINT;
    private Double SaldoDeudorCEROINT;
    private Double MontoDisponible;
    private String fechacreacion;


    //Constructor vacío
    public CardBalanceInfo() {
        this.IDTransaction = 0;
        this.TarjetaID = "";
        this.SaldoDeudorINT = 0.0;
        this.SaldoDeudorCEROINT = 0.0;
        this.MontoDisponible = 0.0;
        this.fechacreacion = establecerFecha();
    }

    //Constructor con parámetros
    public CardBalanceInfo(int idtransaction, String tarjetaID,  double SaldoDeudorINT,
                           double SaldoDeudorCEROINT, double MontoDisponible, String fechacreacion) {
        establecerIDTransaction(idtransaction);
        establecerTarjetaID(tarjetaID);
        establecerSaldoDeudorINT(SaldoDeudorINT);
        establecerSaldoDeudorCEROINT(SaldoDeudorCEROINT);
        establecerMontoDisponible(MontoDisponible);
        establecerFecha(fechacreacion);
    }

    public void establecerIDTransaction(int idtransaction) {
        IDTransaction = idtransaction;
    }

    public int obtenerIDTransaction() {
        return IDTransaction;
    }

    private void establecerTarjetaID(String tarjetaid) {
        TarjetaID = tarjetaid;
    }

    public String obtenerTarjetaID() {
        return TarjetaID;
    }


    private void establecerSaldoDeudorINT(double saldodeudorint) {
        SaldoDeudorINT = saldodeudorint;
    }

    public double obtenerSaldoDeudorINT() {
        return SaldoDeudorINT;
    }

    private void establecerSaldoDeudorCEROINT(double saldodeudorceroint) {
        SaldoDeudorCEROINT = saldodeudorceroint;
    }

    public double obtenerSaldoDeudorCEROINT() {
        return SaldoDeudorCEROINT;
    }

    private void establecerMontoDisponible(double montodisponible) {
        MontoDisponible = montodisponible;
    }

    public double obtenerMontoDisponible() {
        return MontoDisponible;
    }


    private void establecerFecha(String fecha) {
        fechacreacion = fecha;
    }

    public String obtenerFecha() {
        return fechacreacion;
    }


    //Devolver la fecha y hora de registro automático
    private String establecerFecha() {
        SimpleDateFormat formatofecha = new SimpleDateFormat("ddMMyyyy_HHmmss");
        String fecharegistro = formatofecha.format(new Date());
        return fecharegistro;
    }

}
