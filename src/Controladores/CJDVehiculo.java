/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Vistas.*;
import Modelo.Vehiculo;
import Modelo.VehiculoConsultas;
import java.sql.*;

/**
 * Controlador del JDialog de Vehiculo.
 * Recoge los datos de la vista y los pasa al modelo y viceversa.
 * @author Sergio Alberca
 */
public class CJDVehiculo {

    private JDVehiculo vistaVehiculo;

    public CJDVehiculo(JDVehiculo vistaVehiculo) {
        this.vistaVehiculo = vistaVehiculo;
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
        
        Vehiculo v ;
        int idVehiculo = 0;
        
        String matricula = vistaVehiculo.getMatricula();
        String modelo = vistaVehiculo.getModelo();
        String caracteristicas = vistaVehiculo.getCaracteristicas();
        double precio_diario = Double.parseDouble(vistaVehiculo.getPrecio());
        String estado = vistaVehiculo.getEstado();
        String tipo_vehiculo = vistaVehiculo.getTipo();
                

        // Guardamos en el objeto los datos
        // Comprueba si es editar (false) o crear uno nuevo (true)
        if (vistaVehiculo.getFuncion() == true) {
            try {
                // Si el método nuevo no ha devuelto true (no se ha podido crear el objeto), no cerrará la ventana.
                // aqui recogerá el id vehiculo y comprobará si es nulo o no.
                idVehiculo = VehiculoConsultas.consulta().idNueva();
                
                v = new Vehiculo(idVehiculo, matricula, modelo, caracteristicas, precio_diario, estado, tipo_vehiculo);
                if (VehiculoConsultas.consulta().nuevo(v) != true) {
                    return false;
                }
            } catch (IllegalArgumentException iae) {
                vistaVehiculo.excepcionMatricula2();
                return false;
            } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException em) {
                vistaVehiculo.excepcionMatricula();
                return false;
            } catch (SQLException esql) {
                vistaVehiculo.muestraExcepcion();
                esql.printStackTrace();
                return false;
            }

        } else {
            
            
            // en caso de editar recogera la id vehiculo directamente del JDialog.
            idVehiculo = Integer.parseInt(vistaVehiculo.getId());
            
            // igualamos el objeto a la busqueda realizada de la matricula
            v = VehiculoConsultas.consulta().BuscarporId(idVehiculo); 
            
            // guardamos los nuevos datos de ese objeto.
            v.setIdVehiculo(idVehiculo);
            v.setMatricula(matricula);
            v.setModelo(modelo);
            v.setCaracteristicas(caracteristicas);
            v.setPrecioDiario(precio_diario);
            v.setEstado(estado);
             v.setTipoVehiculo(tipo_vehiculo);
            try {
                VehiculoConsultas.consulta().editar(v);
                return true;
            } catch (SQLException esql) {
                vistaVehiculo.muestraExcepcion();
                esql.printStackTrace();
            }
        }
        return true;
    }

}
