/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 * Crea una nueva clase de tipo Extra que tendrá unos atributos específicos.
 * @author Rafa
 */
public class Extras {

    private int idExtra;
    private String nombre;
    private String descripcion;
    private double precioAlquiler;

    public Extras(int idExtra, String nombre, String descripcion, double precio) {
        this.idExtra = idExtra;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioAlquiler = precio;
    }

    public Extras() {
    }

    public int getIdExtra() {
        return idExtra;
    }

    public void setIdExtra(int idExtra) {
        this.idExtra = idExtra;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioAlquiler() {
        return precioAlquiler;
    }

    public void setPrecioAlquiler(double precioAlquiler) {
        this.precioAlquiler = precioAlquiler;
    }

   

}
