/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelo.Cliente;
import Modelo.ClienteConsultas;
import Vistas.JDCliente;
import java.sql.*;

/**
 * Controlador del JDialog de Cliente.
 * Recoge los datos de la vista y los pasa al modelo y viceversa.
 * @author Rafa
 */
public class CJDCliente {

    private JDCliente vistaCliente;

    public CJDCliente(JDCliente vistaCliente) {
        this.vistaCliente = vistaCliente;
    }

    /**
     * Método que recoge los datos introducidos en el JDialog y los guarda en un
     * nuevo objeto de tipo Vehiculo Posteriormente comprueba si se debe crear
     * uno nuevo (true) o se debe editar un registro (false)
     *
     * @return true si se ha podido guardar, false en caso contario. Esto sirve
     * para que en caso de saltar una excepción no cierre nuestra ventana, que
     * está declarado en el JDialod.
     * @throws NumberFormatException
     */
    public boolean guardar() throws NumberFormatException {
        
        Cliente c ;
        
        String dni = vistaCliente.getDni();
        String nombre = vistaCliente.getNombre();
        String apellidos = vistaCliente.getApellidos();
        String direccion = vistaCliente.getDireccion();
        int telefono = Integer.parseInt(vistaCliente.getTelefono());
        String email = vistaCliente.getEmail();
                

        // Guardamos en el objeto los datos
        // Comprueba si es editar (false) o crear uno nuevo (true)
        if (vistaCliente.getFuncion() == true) {
            try {
                // Si el método nuevo no ha devuelto true (no se ha podido crear el objeto), no cerrará la ventana.
                c = new Cliente(dni, nombre, apellidos, direccion, telefono, email);
                if (ClienteConsultas.consulta().nuevo(c) != true) {
                    return false;
                }
            } catch (IllegalArgumentException iae) {
                vistaCliente.excepcionDniInvalido();
                return false;
            } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException em) {
                vistaCliente.excepcionDniExistente();
                return false;
            } catch (SQLException esql) {
                vistaCliente.muestraExcepcionSQL();
                esql.printStackTrace();
                return false;
            }

        } else {
            // igualamos el objeto a la busqueda realizada del dni
            c = ClienteConsultas.consulta().buscarporDNI(dni); 
            
            // guardamos los nuevos datos de ese objeto.
            c.setDni(dni);
            c.setNombre(nombre);
            c.setApellidos(apellidos);
            c.setDireccion(direccion);
            c.setTelefono(telefono);
            c.setEmail(email);
            try {
                ClienteConsultas.consulta().editar(c);
                return true;
            } catch (SQLException esql) {
                vistaCliente.muestraExcepcionSQL();
                esql.printStackTrace();
            }
        }
        return true;
    }

}
