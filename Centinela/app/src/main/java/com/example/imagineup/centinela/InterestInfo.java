package com.example.imagineup.centinela;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.02
 * @since 2016-02-29  2016-05-21
 */

public class InterestInfo {

    /**
     * Variables descriptivas de la clase Usuario
     * @param IDTransaction número del registro
     * @param TarjetaID identificador de la tarjeta
     * @param InteresFijo Intereses fijos
     * @param InteresMoratorio Intereses Moratorios
     * @param fechacreacion fecha de creación del registro
     */

    private int IDTransaction;
    private String TarjetaID;
    private Double InteresFijo;
    private Double InteresMoratorio;
    private String fechacreacion;


    //Constructor vacío
    public InterestInfo() {
        this.IDTransaction = 0;
        this.TarjetaID = "";
        this.InteresFijo = 0.0;
        this.InteresMoratorio = 0.0;
        this.fechacreacion = establecerFechaSinFormato();
    }

    //Constructor con parámetros
    public InterestInfo(int idtransaction, String tarjetaID, double InteresFijo, double InteresMoratorio, String fechacreacion) {
        establecerIDTransaction(idtransaction);
        establecerTarjetaID(tarjetaID);
        establecerInteresFijo(InteresFijo);
        establecerInteresMoratorio(InteresMoratorio);
        establecerFecha(fechacreacion);
    }

    public void establecerIDTransaction (int idtransaction){
        IDTransaction = idtransaction;
    }

    public int obtenerIDTransaction() {
        return IDTransaction;
    }

    private void establecerTarjetaID (String tarjetaid){
        TarjetaID = tarjetaid;
    }

    public String obtenerTarjetaID() {
        return TarjetaID;
    }

    private void establecerInteresFijo(double interesfijo){
        InteresFijo = interesfijo;
    }

    public double obtenerInteresFijo() {
        return InteresFijo;
    }

    private void establecerInteresMoratorio(double interesmor){
        InteresMoratorio = interesmor;
    }

    public double obtenerInteresMoratorio() {
        return InteresMoratorio;
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