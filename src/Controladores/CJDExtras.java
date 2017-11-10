/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelo.Extras;
import Modelo.ExtrasConsultas;
import Vistas.JDExtra;
import java.sql.*;

/**
 * Controlador del JDialog de Extras.
 * Recoge los datos de la vista y los pasa al modelo y viceversa.
 * @author Rafa
 */
public class CJDExtras {

    private JDExtra vistaExtra;

    public CJDExtras(JDExtra vistaExtra) {
        this.vistaExtra = vistaExtra;
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
        
        // declaramos el objeto y la variable idextra.
        Extras ext;
        int idextra = 0;
        String nombre = vistaExtra.getNombre();
        String descripcion = vistaExtra.getDescripcion();
        int precio_alquiler = Integer.parseInt(vistaExtra.getPrecio());
        
        

        // Guardamos en el objeto los datos
        // Comprueba si es editar (false) o crear uno nuevo (true)
        if (vistaExtra.getFuncion() == true) {
            try {
                // Si el método nuevo no ha devuelto true (no se ha podido crear el objeto), no cerrará la ventana.
                // aqui recogerá el id extra y comprobará si es nulo o no.
                idextra = ExtrasConsultas.consulta().idNueva();
                
                ext = new Extras(idextra, nombre, descripcion, precio_alquiler);
                if (ExtrasConsultas.consulta().nuevo(ext) != true) {
                    return false;
                }
            } catch (SQLException esql) {
                vistaExtra.muestraExcepcion();
                esql.printStackTrace();
                return false;
            }

        } else {
      
            // en caso de editar recogera la id extra directamente del JDialog.
            idextra = Integer.parseInt(vistaExtra.getId());
            
            // igualamos el objeto a la busqueda realizada de la id
            ext = ExtrasConsultas.consulta().buscaId(idextra); 
            
            // guardamos los nuevos datos de ese objeto.
            ext.setIdExtra(idextra);
            ext.setNombre(nombre);
            ext.setDescripcion(descripcion);
            ext.setPrecioAlquiler(precio_alquiler);
            try {
                ExtrasConsultas.consulta().editar(ext);
                return true;
            } catch (SQLException esql) {
                vistaExtra.muestraExcepcion();
                esql.printStackTrace();
            }
        }
        return true;
    }

}
