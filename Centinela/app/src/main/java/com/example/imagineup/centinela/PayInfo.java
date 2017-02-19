package com.example.imagineup.centinela;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.03
 * @since 2015-12-7 2016-02-29 2016-08-27
 */

public class PayInfo {

    /**
     * Variables descriptivas de la clase Usuario
     * @param IDTransaction número del registro
     * @param PagoID nombre identificador de la compra
     * @param TarjetaID identificador de la tarjeta
     * @param MontoPago Monto del pago
     * @param DescripcionPago Describe el pago realizado
     * @param fechacreacion fecha de creación del registro
     */

    private int IDTransaction;
    private String PagoID;
    private String TarjetaID;
    private Double MontoPago;
    private String FechaPago;
    private String FechaPagoFormato;
    private String fechacreacion;


    //Constructor vacío
    public PayInfo() {
        this.IDTransaction = 0;
        this.PagoID = "";
        this.TarjetaID = "";
        this.MontoPago = 0.0;
        this.FechaPago = "";
        this.FechaPagoFormato ="";
        this.fechacreacion = establecerFechaSinFormato();
    }

    //Constructor con parámetros
    public PayInfo(int idtransaction, String pagoid, String tarjetaID, double monto, String fechapago, String fechaPagoFormato,
                   String fechacreacion) {
        establecerIDTransaction(idtransaction);
        establecerPagoID(pagoid);
        establecerTarjetaID(tarjetaID);
        establecerMontoPago(monto);
        establecerFechaPago(fechapago);
        establecerFechaPagoFormato(fechaPagoFormato);
        establecerFecha(fechacreacion);
    }

    public void establecerIDTransaction (int idtransaction){
        IDTransaction = idtransaction;
    }

    public int obtenerIDTransaction() {
        return IDTransaction;
    }

    private void establecerPagoID (String pagoid){
        PagoID = pagoid;
    }

    public String obtenerPagoID() {
        return PagoID;
    }

    private void establecerTarjetaID (String tarjetaid){
        TarjetaID = tarjetaid;
    }

    public String obtenerTarjetaID() {
        return TarjetaID;
    }

    private void establecerMontoPago(double monto){
        MontoPago = monto;
    }

    public double obtenerMontoPago() {
        return MontoPago;
    }

    private void establecerFechaPago(String fechaPago){
        FechaPago = fechaPago;
    }

    public String obtenerFechaPago() {
        return FechaPago;
    }

    private void establecerFechaPagoFormato(String fechaPagoFormato){
        FechaPagoFormato = fechaPagoFormato;
    }

    public String obtenerFechaPagoFormato() {
        return FechaPagoFormato;
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

    //Establecer la fecha en string pero sin formato
    private String establecerFechaSinFormato() {
        Date mi_fecha = new Date();
        Long fecha_numero = mi_fecha.getTime();
        String fecharegistro = fecha_numero.toString();
        return fecharegistro;
    }

}
