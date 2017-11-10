/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelo.Trabajador;
import Modelo.TrabajadorConsultas;
import Vistas.JFrameTrabajador;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;

/**
 * Controlador del JFrame de Trabajador.
 * Recoge los datos de la vista y los pasa al modelo y viceversa.
 * @author Rafa
 */
public class Ctrabajador {

    private JFrameTrabajador vistaTrabajadores;
    private DefaultTableModel tablaTrabajador;
    private int totalregistros;

    /**
     * Constructor de la clase que enlazará el controlador con los vehiculos
     *
     * @param vistaTrabajadores
     */
    public Ctrabajador(JFrameTrabajador vistaTrabajadores) {
        this.vistaTrabajadores = vistaTrabajadores;
    }

    public Ctrabajador() {
    }

    public int getTotalregistros() {
        return totalregistros;
    }

    public void setTotalregistros(int totalregistros) {
        this.totalregistros = totalregistros;
    }
   
    
    /**
     * Método que imprimirá los datos de la base de datos en la tabla que
     * tenemos en nuestro JFrame
     */
    public void rellenaTablaTrabajadores() {

        // Creamos una nueva tabla la cual le sobreescribiremos un parámetro para que no se puedan editar las celdas.
        tablaTrabajador = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Creamos las columnas de nuestra tabla.
        tablaTrabajador.addColumn("DNI");
        tablaTrabajador.addColumn("Nombre");
        tablaTrabajador.addColumn("Apellidos");
        tablaTrabajador.addColumn("Direccion");
        tablaTrabajador.addColumn("Telefono");
        tablaTrabajador.addColumn("Email");
        tablaTrabajador.addColumn("Sueldo");
        tablaTrabajador.addColumn("Acceso");
        tablaTrabajador.addColumn("Usuario");
        tablaTrabajador.addColumn("Contraseña");
        tablaTrabajador.addColumn("Estado");

        //Creamos un Array con los datos correspondientes que extraerá de la base de datos.
        totalregistros = 0;

        Trabajador[] lista = TrabajadorConsultas.consulta().listarTrabajadores();
        for (Trabajador t : lista) {

            String[] datos = new String[11];
            datos[0] = t.getDni();
            datos[1] = t.getNombre();
            datos[2] = t.getApellidos();
            datos[3] = t.getDireccion();
            datos[4] = String.valueOf(t.getTelefono());
            datos[5] = t.getEmail();
            datos[6] = String.valueOf(t.getSueldo());
            datos[7] = t.getAcceso();
            datos[8] = t.getUsuario();
            datos[9] = t.getContrasena();
            datos[10] = t.getEstado();

            totalregistros = totalregistros + 1;
            tablaTrabajador.addRow(datos);

        }

        vistaTrabajadores.setTMJTableTrabajadores(tablaTrabajador);

    }

    /**
     * Metodo que revisa la fila seleccionada. Después crea un nuevo objeto e
     * inicializa la id del objeto trabajador a la de la posición 0 de nuestra
     * tabla. Posteriormente crea una sentencia SQL y prepara un Statement para
     * que el borrado del registro sea satisfactorio. Finalmente repinta la
     * tabla.
     */
    public void borraTrabajador() {

        int seleccion = vistaTrabajadores.getFilaSeleccionada();
        if (seleccion >= 0) {

            String dni = (String) vistaTrabajadores.getTMJTableTrabajadores().getValueAt(seleccion, 0);
            Trabajador t = TrabajadorConsultas.consulta().buscarporDNI(dni);
            try {
                TrabajadorConsultas.consulta().borrar(t);
                rellenaTablaTrabajadores();
                vistaTrabajadores.repaint();
            } catch (SQLException esql) {
                vistaTrabajadores.muestraExcepcionSQL();
                esql.printStackTrace();
            }

        }

    }

    /**
     * Método que recoge todos los datos de la tabla y los envia a otro método
     * de la vista que se encargará de abrir un JDialog con esos datos.
     */
    public void editarTrabajador() {
        int seleccion = vistaTrabajadores.getFilaSeleccionada();
        if (seleccion >= 0) {

            String dni = (String) vistaTrabajadores.getTMJTableTrabajadores().getValueAt(seleccion, 0);
            String nombre = (String) vistaTrabajadores.getTMJTableTrabajadores().getValueAt(seleccion, 1);
            String apellidos = (String) vistaTrabajadores.getTMJTableTrabajadores().getValueAt(seleccion, 2);
            String direccion = (String) vistaTrabajadores.getTMJTableTrabajadores().getValueAt(seleccion, 3);
            int telefono = Integer.parseInt((String) vistaTrabajadores.getTMJTableTrabajadores().getValueAt(seleccion, 4));
            String email = (String) vistaTrabajadores.getTMJTableTrabajadores().getValueAt(seleccion, 5);
            double sueldo = Double.parseDouble((String) vistaTrabajadores.getTMJTableTrabajadores().getValueAt(seleccion, 6));
            String acceso = (String) vistaTrabajadores.getTMJTableTrabajadores().getValueAt(seleccion, 7);
            String usuario = (String) vistaTrabajadores.getTMJTableTrabajadores().getValueAt(seleccion, 8);
            String contrasena = (String) vistaTrabajadores.getTMJTableTrabajadores().getValueAt(seleccion, 9);
            String estado = (String) vistaTrabajadores.getTMJTableTrabajadores().getValueAt(seleccion, 10);

            vistaTrabajadores.AbreEditarTrabajador(dni, nombre, apellidos, direccion, telefono, email, sueldo, acceso, usuario, contrasena, estado);

        }
    }

    public void buscarTrabajador(String b) {

        Trabajador t = TrabajadorConsultas.consulta().buscarporDNI(b);

        if (t != null) {

            //Borramos el contenido de la tabla
            int numero_filas = tablaTrabajador.getRowCount();

            for (int i = numero_filas - 1; i > -1; i--) {
                tablaTrabajador.removeRow(i);
            }

            totalregistros = 0;

            String[] datos = new String[11];
            datos[0] = t.getDni();
            datos[1] = t.getNombre();
            datos[2] = t.getApellidos();
            datos[3] = t.getDireccion();
            datos[4] = String.valueOf(t.getTelefono());
            datos[5] = t.getEmail();
            datos[6] = String.valueOf(t.getSueldo());
            datos[7] = t.getAcceso();
            datos[8] = t.getUsuario();
            datos[9] = t.getContrasena();
            datos[10] = t.getEstado();

            totalregistros = totalregistros + 1;
            tablaTrabajador.addRow(datos);
        } else {
            vistaTrabajadores.muestraExcepcionTrabajadorNulo();
        }

    }

}
