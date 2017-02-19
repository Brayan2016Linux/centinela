package com.example.imagineup.centinela;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.02
 * @since 2016-05-22 2016-05-26
 */

public class BankFeesInfo {

    /**
     * Variables descriptivas de la clase Usuario
     * @param IDTransaction número del registro
     * @param TarjetaID identificador de la tarjeta
     * @param ComisionAdministrativa Comisión por asuntos administrativos
     * @param ComisionSeguro Comisión por seguros
     * @param ComisionAdelantoAdminist Comisión por adelantos de efectivo administrativos
     * @param ComisionAdelanto_Porcentaje Comisión por cantidad de adelanto
     * @param ComisionMoraUnico Comisión por mora cuando el banco cobra una única cuota
     * @param ComisionMora_2_30 Comisión por mora cuando el banco de 2 a 30 días
     * @param ComisionMora_31_90 Comisión por mora cuando el banco de 31 a 90 días
     * @param ComisionMora_91_180 Comisión por mora cuando el banco de 91 a 180 días
     * @param fechacreacion fecha de creación del registro
     */

    private int IDTransaction;
    private String TarjetaID;
    private Double ComisionAdministrativa;
    private Double ComisionSeguro;
    private Double ComisionAdelantoAdminist;
    private Double ComisionAdelanto_Porcent;
    private Double ComisionMoraUnico;
    private Double ComisionMora_2_30;
    private Double ComisionMora_31_90;
    private Double ComisionMora_91_180;
    private String fechacreacion;


    //Constructor vacío
    public BankFeesInfo() {
        this.IDTransaction = 0;
        this.TarjetaID = "";
        this.ComisionAdministrativa = 0.0;
        this.ComisionSeguro = 0.0;
        this.ComisionAdelantoAdminist = 0.0;
        this.ComisionAdelanto_Porcent = 0.0;
        this.ComisionMoraUnico = 0.0;
        this.ComisionMora_2_30 = 0.0;
        this.ComisionMora_31_90 = 0.0;
        this.ComisionMora_91_180 = 0.0;
        this.fechacreacion = establecerFechaSinFormato();
    }

    //Constructor con parámetro nombre de tarjeta
    public BankFeesInfo(String nombreTarjeta) {
        this.IDTransaction = 0;
        this.TarjetaID = nombreTarjeta;
        this.ComisionAdministrativa = 0.0;
        this.ComisionSeguro = 0.0;
        this.ComisionAdelantoAdminist = 0.0;
        this.ComisionAdelanto_Porcent = 0.0;
        this.ComisionMoraUnico = 0.0;
        this.ComisionMora_2_30 = 0.0;
        this.ComisionMora_31_90 = 0.0;
        this.ComisionMora_91_180 = 0.0;
        this.fechacreacion = establecerFechaSinFormato();
    }

    //Constructor con parámetros
    public BankFeesInfo(int idtransaction,
                        String tarjetaID,
                        double comisionAdministrativa,
                        double comisionSeguro,
                        double comisionAdelantoAdminist,
                        double comisionAdelantoPorcent,
                        double comisionMoraUnico,
                        double comisionMora_2_30,
                        double comisionMora_31_90,
                        double comisionMora_91_180,
                        String fechacreacion) {

        establecerIDTransaction(idtransaction);
        establecerTarjetaID(tarjetaID);
        establecerComisionAdministrativa(comisionAdministrativa);
        establecerComisionSeguro(comisionSeguro);
        establecerComisionAdelantoAdminist(comisionAdelantoAdminist);
        establecerComisionAdelantoPorcent(comisionAdelantoPorcent);
        establecerComisionMoraUnico(comisionMoraUnico);
        establecerComisionMora230(comisionMora_2_30);
        establecerComisionMora3190(comisionMora_31_90);
        establecerComisionMora91180(comisionMora_91_180);
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

    private void establecerComisionAdministrativa (double comisionadministrativa){
        ComisionAdministrativa = comisionadministrativa;
    }

    public double obtenerComisionAdmnistrativa() {
        return ComisionAdministrativa;
    }

    private void establecerComisionSeguro (double comisionseguro){
        ComisionSeguro = comisionseguro;
    }

    public double obtenerComisionSeguro() {
        return ComisionSeguro;
    }

    private void establecerComisionAdelantoPorcent (double comisionAdelantoPorcent){
        ComisionAdelanto_Porcent = comisionAdelantoPorcent;
    }

    public double obtenerComisionAdelantoPorcent() {
        return ComisionAdelanto_Porcent;
    }

    private void establecerComisionAdelantoAdminist (double comisionAdelantoAdminist){
        ComisionAdelantoAdminist = comisionAdelantoAdminist;
    }

    public double obtenerComisionAdelantoAdminist() {
        return ComisionAdelantoAdminist;
    }

    private void establecerComisionMoraUnico (double comisionmoraunico){
        ComisionMoraUnico = comisionmoraunico;
    }

    public double obtenerComisionMoraUnico() {
        return ComisionMoraUnico;
    }

    private void establecerComisionMora230 (double comisionmora230){
        ComisionMora_2_30 = comisionmora230;
    }

    public double obtenerComisionMora_2_30() {
        return ComisionMora_2_30;
    }

    private void establecerComisionMora3190 (double comisionmora3190){
        ComisionMora_31_90 = comisionmora3190;
    }

    public double obtenerComisionMora_31_90() {
        return ComisionMora_31_90;
    }

    private void establecerComisionMora91180 (double comisionmora91180){
        ComisionMora_91_180 = comisionmora91180;
    }

    public double obtenerComisionMora_91_180() {
        return ComisionMora_91_180;
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