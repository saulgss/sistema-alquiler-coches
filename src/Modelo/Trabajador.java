/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *  Esta clase hereda de la clase persona. Y crea una nueva clase de tipo Trabajador
 *  que tendrá sus propios atributos aparte de los heredados de persona.
 * @author Rafa
 */
public class Trabajador extends Persona {
    
    private double sueldo;
    private String acceso;
    private String usuario;
    private String contrasena;
    private String estado;

    public Trabajador() {
    }

    public Trabajador(String dni, String nombre, String apellidos, String direccion, int telefono, String email, double sueldo, String acceso, String usuario, String contrasena, String estado) {
        super(dni, nombre, apellidos, direccion, telefono, email);
        this.sueldo = sueldo;
        this.acceso = acceso;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.estado = estado;
    }

    public double getSueldo() {
        return sueldo;
    }

    public void setSueldo(double sueldo) {
        this.sueldo = sueldo;
    }

    public String getAcceso() {
        return acceso;
    }

    public void setAcceso(String acceso) {
        this.acceso = acceso;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }   
    
}
