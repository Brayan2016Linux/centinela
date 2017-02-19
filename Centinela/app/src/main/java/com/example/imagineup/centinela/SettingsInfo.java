package com.example.imagineup.centinela;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.01
 * @since 2016-05-25
 */

public class SettingsInfo {
    /**
     * Variables descriptivas de la clase Usuario
     * @param IDSettings número del registro
     * @param DiasAntePago número de Días seleccionados antes del pago
     * @param AutorizacionNotificacion identificador de la tarjeta
     * @param fechacreacion fecha de creación del registro
     */

    private int IDSettings;
    private int DiasAntePago;
    private int AutorizacionNotificacion;
    private String IDUsuario;
    private String fechacreacion;


    //Constructor vacío
    public SettingsInfo() {
        this.IDSettings = 0;
        this.DiasAntePago = 3;
        this.AutorizacionNotificacion = 1;
        this.IDUsuario = "";
        this.fechacreacion = establecerFechaSinFormato();
    }

    //Constructor con parámetros
    public SettingsInfo(int idtransaction, int dias_ante_pago, int autorizacionnotificacion, String idusuario,
                   String fechacreacion) {
        establecerIDTransaction(idtransaction);
        establecerDiasAntePago(dias_ante_pago);
        establecerAutorizacionNotificacion(autorizacionnotificacion);
        establecerUsuarioID(idusuario);
        establecerFecha(fechacreacion);
    }

    //Constructor con un parámetro para crear con el nombre del usuario
    public SettingsInfo(String iduser) {
        this.IDSettings = 0;
        this.DiasAntePago = 3;
        this.AutorizacionNotificacion = 1;
        this.IDUsuario = iduser;
        this.fechacreacion = establecerFecha();
    }

    public void establecerIDTransaction (int idtransaction){
        IDSettings = idtransaction;
    }

    public int obtenerIDTransaction() {
        return IDSettings;
    }

    private void establecerDiasAntePago(int dias_ante_pago){
        DiasAntePago = dias_ante_pago;
    }

    public int obtenerDiasAntePago() {
        return DiasAntePago;
    }

    private void establecerAutorizacionNotificacion (int autorizacionnotificacion){
        AutorizacionNotificacion = autorizacionnotificacion;
    }

    public int obtenerAutorizacionNotificacion() {
        return AutorizacionNotificacion;
    }

    private void establecerUsuarioID (String idusuario){
        IDUsuario = idusuario;
    }

    public String obtenerUsuarioID() {
        return IDUsuario;
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
