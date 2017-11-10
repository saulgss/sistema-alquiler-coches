/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 *  Esta clase hereda de la clase persona. Y crea una nueva clase de tipo cliente
 *  hereda los atributos de la clase persona.
 * @author Rafa
 */
public class Cliente extends Persona {

    public Cliente() {
    }

    public Cliente(String dni, String nombre, String apellidos, String direccion, int telefono, String email) {
        super(dni, nombre, apellidos, direccion, telefono, email);
    }
    
    
}
