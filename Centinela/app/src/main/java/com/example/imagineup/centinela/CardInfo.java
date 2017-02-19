package com.example.imagineup.centinela;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.02
 * @since 2015-12-7  2016-03-02
 */

public class CardInfo {

    /**
     * Variables descriptivas de la clase Usuario
     * @param IDCard número del registro
     * @param IDUser número identificador del usuario
     * @param NombreID nombre identificador de la tarjeta
     * @param EmisorID Banco Emisor de la tarjeta
     * @param diaDeCorte día de corte de la tarjeta
     * @param diasGracia números de días de gracia de la tarjeta
     * @param fechacreacion fecha de creación del registro
     */

    private int IDCard;
    private String IDUser;
    private String NombreID;
    private String EmisorID;
    private int diaDeCorte;
    private int diasGracia;
    private String fechacreacion;


    //Constructor vacío
    public CardInfo() {
        this.IDCard = 0;
        this.IDUser = "";
        this.NombreID = "";
        this.EmisorID = "";
        this.diaDeCorte = 0;
        this.diasGracia = 0;
        this.fechacreacion = establecerFechaSinFormato();
    }

    //Constructor con parámetros
    public CardInfo(int idcard, String iduser, String nombreid, String emisor, int diasgracia, int corte, String fechacreacion) {
        establecerIDCard(idcard);
        establecerIDUser(iduser);
        establecerNombreID(nombreid);
        establecerEmisorID(emisor);
        establecerDiasGracia(diasgracia);
        establecerDiaCorte(corte);
        establecerFecha(fechacreacion);
    }

    public void establecerIDCard (int idcard){
        IDCard = idcard;
    }

    public int obtenerIDCard() {
        return IDCard;
    }

    public void establecerIDUser (String iduser){
        IDUser = iduser;
    }

    public String obtenerIDUser() {
        return IDUser;
    }

    private void establecerNombreID (String nombreid){
        NombreID = nombreid;
    }

    public String obtenerNombreID() {
        return NombreID;
    }

    private void establecerEmisorID (String emisorid){
        EmisorID = emisorid;
    }

    public String obtenerEmisorID() {
        return EmisorID;
    }


    private void establecerDiaCorte (int diacorte){
        diaDeCorte = diacorte;
    }

    public int obtenerDiaCorte() {
        return diaDeCorte;
    }

    private void establecerDiasGracia (int diasgracia){
        diasGracia = diasgracia;
    }

    public int obtenerDiaGracia() {
        return diasGracia;
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
