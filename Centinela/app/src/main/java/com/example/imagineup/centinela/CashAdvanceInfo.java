package com.example.imagineup.centinela;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.01
 * @since 2016-05-21
 */

public class CashAdvanceInfo {

    /**
     * Variables descriptivas de la clase Usuario
     * @param IDTransaction número del registro
     * @param AdelantoID Detalle del adelanto
     * @param TarjetaID identificador de la tarjeta
     * @param MontoAdelanto Monto del adelanto
     * @param FechaAdelanto Día en el que fue realizado el adelanto de efectivo
     * @param FechaAdelantoFormato Día en el que fue realizado el adelanto de efectivo formato
     * @param fechacreacion fecha de creación del registro
     */

    private int IDTransaction;
    private String AdelantoID;
    private String TarjetaID;
    private Double MontoAdelanto;
    private String FechaAdelanto;
    private String FechaAdelantoFormato;
    private String fechacreacion;


    //Constructor vacío
    public CashAdvanceInfo() {
        this.IDTransaction = 0;
        this.AdelantoID = "";
        this.TarjetaID = "";
        this.MontoAdelanto = 0.0;
        this.FechaAdelanto ="";
        this.FechaAdelantoFormato ="";
        this.fechacreacion = establecerFecha();
    }

    //Constructor con parámetros
    public CashAdvanceInfo(int idtransaction, String adelantoid, String tarjetaID, double montoadelanto,
                     String fechaadelanto, String fechaadelantoformato, String fechacreacion) {
        establecerIDTransaction(idtransaction);
        establecerAdelantoDetalle(adelantoid);
        establecerTarjetaID(tarjetaID);
        establecerMontoAdelanto(montoadelanto);
        establecerFechaAdelanto(fechaadelanto);
        establecerFechaAdelantoFormato(fechaadelantoformato);
        establecerFecha(fechacreacion);
    }

    public void establecerIDTransaction (int idtransaction){
        IDTransaction = idtransaction;
    }

    public int obtenerIDTransaction() {
        return IDTransaction;
    }

    private void establecerAdelantoDetalle (String adelantodetalle){
        AdelantoID = adelantodetalle;
    }

    public String obtenerAdelantoDetalle() {
        return AdelantoID;
    }

    private void establecerTarjetaID (String tarjetaid){
        TarjetaID = tarjetaid;
    }

    public String obtenerTarjetaID() {
        return TarjetaID;
    }

    private void establecerMontoAdelanto(double monto){
        MontoAdelanto = monto;
    }

    public double obtenerMontoAdelanto() {
        return MontoAdelanto;
    }

    private void establecerFechaAdelanto (String fechaadelanto){
        FechaAdelanto = fechaadelanto;
    }

    public String obtenerFechaAdelanto() {
        return FechaAdelanto;
    }

    private void establecerFechaAdelantoFormato (String fechaadelantoformato){
        FechaAdelantoFormato = fechaadelantoformato;
    }

    public String obtenerFechaAdelantoFormato() {
        return FechaAdelantoFormato;
    }

    private void establecerFecha(String fecha) {
        fechacreacion = fecha;
    }

    public String obtenerFecha (){
        return fechacreacion;
    }


    //Devolver la fecha y hora de registro automático
    private String establecerFecha(){
        SimpleDateFormat formatofecha = new SimpleDateFormat("ddMMyyyy_HHmmss");
        String fecharegistro = formatofecha.format(new Date());
        return fecharegistro;
    }

}