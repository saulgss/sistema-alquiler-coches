/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelo.Trabajador;
import Modelo.TrabajadorConsultas;
import Vistas.JDTrabajador;
import java.sql.SQLException;

/**
 * Controlador del JDialog de Trabajador.
 * Recoge los datos de la vista y los pasa al modelo y viceversa.
 * @author Rafa
 */
public class CJDTrabajador {

    private JDTrabajador vistaTrabajador;

    public CJDTrabajador(JDTrabajador vistaTrabajador) {
        this.vistaTrabajador = vistaTrabajador;
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

        Trabajador t;

        
        
        String dni = vistaTrabajador.getDni();
        
        String nombre = vistaTrabajador.getNombre();
        String apellidos = vistaTrabajador.getApellidos();
        String direccion = vistaTrabajador.getDireccion();
        // declaramos la variable telefono, y la recogemos de la vista, si está vacia será
        // igual a 0, sino a lo que se recoga de la vista
        int telefono;
        if (vistaTrabajador.getTelefono().isEmpty()) {
            telefono = 0;
        } else {
         telefono = Integer.parseInt(vistaTrabajador.getTelefono());
        }
        String email = vistaTrabajador.getEmail();
        double sueldo = Double.parseDouble(vistaTrabajador.getSueldo());
        String acceso = vistaTrabajador.getAcceso();
        String usuario = vistaTrabajador.getUsuario();
        String contraseña = vistaTrabajador.getContrasena();
        String estado = vistaTrabajador.getEstado();

        // Guardamos en el objeto los datos
        // Comprueba si es editar (false) o crear uno nuevo (true)
        if (vistaTrabajador.getFuncion() == true) {
            try {
                // Si el método nuevo no ha devuelto true (no se ha podido crear el objeto), no cerrará la ventana.
                t = new Trabajador(dni, nombre, apellidos, direccion, telefono, email, sueldo, acceso, usuario, contraseña, estado);
                if (TrabajadorConsultas.consulta().nuevo(t) != true) {
                    return false;
                }
            } catch (IllegalArgumentException iae) {
                vistaTrabajador.excepcionDNI2();
                return false;
            } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException em) {
                vistaTrabajador.excepcionDNI();
                return false;
            } catch (SQLException esql) {
                vistaTrabajador.muestraExcepcion();
                esql.printStackTrace();
                return false;
            }

        } else {
            // igualamos el objeto a la busqueda realizada del dni
            t = TrabajadorConsultas.consulta().buscarporDNI(dni);

            // guardamos los nuevos datos de ese objeto.
            t.setDni(dni);
            t.setNombre(nombre);
            t.setApellidos(apellidos);
            t.setDireccion(direccion);
            t.setTelefono(telefono);
            t.setEmail(email);
            t.setSueldo(sueldo);
            t.setAcceso(acceso);
            t.setUsuario(usuario);
            t.setContrasena(contraseña);
            t.setEstado(estado);
            try {
                TrabajadorConsultas.consulta().editar(t);
                return true;
            } catch (SQLException esql) {
                vistaTrabajador.muestraExcepcion();
                esql.printStackTrace();
            }
        }
        return true;
    }

}
