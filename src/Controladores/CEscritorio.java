/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelo.ClienteConsultas;
import Modelo.ExtrasConsultas;
import Modelo.PagoConsultas;
import Modelo.ReservaConsultas;
import Modelo.VehiculoConsultas;
import Vistas.Escritorio;
import java.sql.SQLException;

/**
 * Controlador del MDI de Escritorio.
 * Recoge los datos de la vista y los pasa al modelo y viceversa.
 * @author Rafa
 */
public class CEscritorio {
    
    private Escritorio vistaInicio;

    public CEscritorio(Escritorio vistaInicio) {
        this.vistaInicio = vistaInicio;
    }
    
    public void iniciaConsultas() {
        try {
            // carga los datos desde la base de datos la lista de vehiculos
            VehiculoConsultas.consulta().obtenVehiculoBBDD();
            // carga los datos desde la base de datos la lista de clientes
            ClienteConsultas.consulta().obtenClienteBBDD();
            // carga los datos desde la base de datos la lista de extras
            ExtrasConsultas.consulta().obtenExtrasBBDD();
            // carga los datos desde la base de datos la lista de reservas
            ReservaConsultas.consulta().obtenReservaBBDD();
            // carga los datos desde la base de datos la lista de pagos
            PagoConsultas.consulta().obtenPagoBBDD();
            
        } catch (SQLException esql) {
            vistaInicio.muestraExcepcionSQL();
            esql.printStackTrace();
        }
    }
    
    public void abreReserva() {
        String dni = vistaInicio.getjLabelDNI();
        String nombre = vistaInicio.getjLabelNombre();
        String apellidos = vistaInicio.getjLabelApellidos();
        
        vistaInicio.abreNuevaReserva(dni, nombre, apellidos);
    }
    
}
