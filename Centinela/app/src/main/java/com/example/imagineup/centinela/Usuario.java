package com.example.imagineup.centinela;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Brayan Rodríguez <bradrd2009jp@gmail.com>
 * @version 0.01
 * @since 2015-11-9
 */
public class Usuario {

    /**
     * Variables descriptivas de la clase Usuario
     * @param IDUser número de identificación del usuario
     * @param NombreUsuario nombre del usuario
     * @param Apellido1 primer apellido
     * @param Apellido2 segundo apellido
     * @param IDCard tarjeta o número de identificación usuario
     * @param Correo correo electrónico del usuario
     * @param Password clave del usuario registrada
     * @param fechacreacion fecha de creación del registro
     */

    private int IDUser;
    private String NombreUsuario;
    private String ApellidoU1;
    private String ApellidoU2;
    private String ID_Card;
    private String Correo;
    private String Password;
    private String fechacreacion;

    /**
     *Constructor vacío de la clase usuario
     *
     */
    public Usuario (){
        this.IDUser = 0;
        this.NombreUsuario = "";
        this.ApellidoU1 = "";
        this.ApellidoU2 = "";
        this.ID_Card = "";
        this.Correo = "";
        this.Password = "";
        this.fechacreacion = establecerFechaSinFormato();
    }

    public Usuario (int iduser, String nombreusuario, String apellido1, String apellido2, String idcard, String correo, String password, String fechanueva) {
        establecerIDUser(iduser);
        establecerUsuario(nombreusuario);
        establecerApellido1(apellido1);
        establecerApellido2(apellido2);
        establecerIDCARD(idcard);
        establecerCorreo(correo);
        establecerPassword(password);
        establecerFecha(fechanueva);
    }

    //Métodos privados
    private void establecerIDUser(int iduser){
        this.IDUser = iduser;
    }

    public int obtenerIDUser(){
        return IDUser;
    }

    private void establecerUsuario(String nombreusuario) {
        this.NombreUsuario = nombreusuario;
    }

    public String obtenerNombreUsuario(){
        return NombreUsuario;
    }

    private void establecerApellido1(String apellidoU1) {this.ApellidoU1 = apellidoU1;}

    public String obtenerApellido1() {return ApellidoU1;}

    private void establecerApellido2(String apellidoU2) {this.ApellidoU2 = apellidoU2;}

    public String obtenerApellido2() {return ApellidoU2;}

    private void establecerIDCARD(String idcard) {this.ID_Card = idcard;}

    public String obtenerIDCARD() {return ID_Card;}

    private void establecerCorreo(String correo) {
        this.Correo = correo;
    }

    public String obtenerCorreo(){
        return Correo;
    }

    private void establecerPassword(String password) {
        this.Password = password;
    }

    public String obtenerPassword(){
        return Password;
    }

    private void establecerFecha(String fechanueva) {
        this.fechacreacion = fechanueva;
    }

    public String obtenerfecha(){
        return fechacreacion;
    }

    //Devolver la fecha y hora de registro automático
    private  String establecerFecha(){
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
