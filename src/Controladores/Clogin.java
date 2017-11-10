/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelo.Trabajador;
import Modelo.TrabajadorConsultas;
import Vistas.Escritorio;
import Vistas.JFrameLogin;
import java.sql.SQLException;

/**
 * Controlador del JFrame de Login.
 * Recoge los datos de la vista y los pasa al modelo y viceversa.
 * @author Rafa
 */
public class Clogin {

    private JFrameLogin vistaLogin;
    private Trabajador trabajador;
    private int totalregistros;

    public Clogin(JFrameLogin vistaLogin) {
        this.vistaLogin = vistaLogin;
    }

    public boolean Ingresar() throws SQLException {
        //Creamos un Array con los datos correspondientes que extraerá de la base de datos.
        totalregistros = 0;

        Trabajador[] lista = TrabajadorConsultas.consulta().listarTrabajadores();
        
        for (Trabajador t : lista) {
            if(t.getUsuario().equals(vistaLogin.getUsuario()) && t.getContrasena().equals(vistaLogin.getContrasena()) && t.getEstado().equals("A")) {
            setTrabajador(t);
            totalregistros = totalregistros + 1;
          
            }
        }

        // comprobamos si nuestro usuario existe o no registrado
        // si es mayor que 0 existe, entonces devolverá true
        // sino devolverá false.
        
        if (totalregistros > 0) return true;
        else return false;
       
    }

    public Trabajador getTrabajador() {
        return trabajador;
    }

    public void setTrabajador(Trabajador trabajador) {
        this.trabajador = trabajador;
    }

    /**
     * Método que renombra los labels del JFrame Escritorio.
     * Los datos los recoge de la tabla de la vista.
     * @param i 
     */
    
    public void renombrarLabels (Escritorio i) {
        
        Trabajador t = getTrabajador();
        
        String acceso = t.getAcceso();
        
        i.setjLabelDNI( t.getDni() );
        i.setjLabelNombre( t.getNombre() );
        i.setjLabelApellidos( t.getApellidos() );
        i.setjLabelUsuario( t.getUsuario() );
        i.setjLabelAcceso(acceso);
        
        // si no es administrador no estaran habilitados los dos items del menu correspondientes
        if ( !acceso.equals("Administrador") ) {
            i.jMenuArchivo.setEnabled(false);
            i.jMenuConfiguracion.setEnabled(false);
        }
        
    }
    

}
