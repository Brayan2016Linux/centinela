package com.example.imagineup.centinela;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.03
 * @since 2015-12-7 2016-02-29 2016-08-27
 */

public class BuyInfo {

    /**
     * Variables descriptivas de la clase Usuario
     * @param IDTransaction número del registro
     * @param CompraID Detalle de la compra
     * @param TarjetaID identificador de la tarjeta
     * @param MontoCredito Monto de la compra
     * @param PeriodoCredito Período de la compra
     * @param Modalidad Indica si es a crédito o a tasa cero
     * @param FechaCompra Día en el que fue realizada la compra
     * @param FechaCompraFormato Dia que se realiza la compra pero con formato
     * @param fechacreacion fecha de creación del registro
     */

    private int IDTransaction;
    private String CompraID;
    private String TarjetaID;
    private Double MontoCredito;
    private int PeriodoCredito;
    private String Modalidad;
    private String FechaCompra;
    private String FechaCompraFormato;
    private String fechacreacion;


    //Constructor vacío
    public BuyInfo() {
        this.IDTransaction = 0;
        this.CompraID = "";
        this.TarjetaID = "";
        this.MontoCredito = 0.0;
        this.PeriodoCredito = 0;
        this.Modalidad = "";
        this.FechaCompra ="";
        this.FechaCompraFormato ="";
        this.fechacreacion = establecerFechaSinFormato();
    }

    //Constructor con parámetros
    public BuyInfo(int idtransaction, String compraid, String tarjetaID, double montocredito, int periodocredito,
                    String modalidad, String fechacompra, String fechacompraformato, String fechacreacion) {
        establecerIDTransaction(idtransaction);
        establecerCompraID(compraid);
        establecerTarjetaID(tarjetaID);
        establecerMontoCredito(montocredito);
        establecerPeriodoCredito(periodocredito);
        establecerModalidad(modalidad);
        establecerFechaCompra(fechacompra);
        establecerFechaCompraFormato(fechacompraformato);
        establecerFecha(fechacreacion);
    }

    public void establecerIDTransaction (int idtransaction){
        IDTransaction = idtransaction;
    }

    public int obtenerIDTransaction() {
        return IDTransaction;
    }

    private void establecerCompraID (String compraid){
        CompraID = compraid;
    }

    public String obtenerCompraID() {
        return CompraID;
    }

    private void establecerTarjetaID (String tarjetaid){
        TarjetaID = tarjetaid;
    }

    public String obtenerTarjetaID() {
        return TarjetaID;
    }

    private void establecerMontoCredito(double monto){
        MontoCredito = monto;
    }

    public double obtenerMontoCredito() {
        return MontoCredito;
    }

    private void establecerPeriodoCredito (int periodo){
        PeriodoCredito = periodo;
    }

    public int obtenerPeriodoCredito() {
        return PeriodoCredito;
    }

    private void establecerModalidad (String modalidad){
        Modalidad = modalidad;
    }

    public String obtenerModalidad() {
        return Modalidad;
    }

    private void establecerFechaCompra (String fechaCompra){
        FechaCompra = fechaCompra;
    }

    public String obtenerFechaCompra() {
        return FechaCompra;
    }

    private void establecerFechaCompraFormato (String fechaCompraFormato){
        FechaCompraFormato = fechaCompraFormato;
    }

    public String obtenerFechaCompraFormato() {
        return FechaCompraFormato;
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
