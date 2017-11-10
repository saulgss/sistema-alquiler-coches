/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelo.Cliente;
import Modelo.ClienteConsultas;
import Vistas.JDListaClientes;
import javax.swing.table.DefaultTableModel;

/**
 * Controlador del JDialog Lista de Clientes.
 * Recoge los datos de la vista y los pasa al modelo y viceversa.
 * @author Rafa
 */
public class CJDListaClientes {

    private JDListaClientes mostrarClientes;
    private DefaultTableModel tablaCliente;
    private int totalregistros;

    /**
     * Constructor de la clase que enlazará el controlador con el JDialog para
     * mostrar los vehiculos
     *
     * @param mostrarClientes
     */
    public CJDListaClientes(JDListaClientes mostrarClientes) {
        this.mostrarClientes = mostrarClientes;
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
    public void rellenaTablaClientes() {

        // Creamos una nueva tabla la cual le sobreescribiremos un parámetro para que no se puedan editar las celdas.
        tablaCliente = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Creamos las columnas de nuestra tabla.
        tablaCliente.addColumn("DNI");
        tablaCliente.addColumn("Nombre");
        tablaCliente.addColumn("Apellidos");
        tablaCliente.addColumn("Direccion");
        tablaCliente.addColumn("Telefono");
        tablaCliente.addColumn("Email");

        //Creamos un Array con los datos correspondientes que extraerá de la base de datos.
        totalregistros = 0;

        Cliente[] lista = ClienteConsultas.consulta().listarClientes();
        for (Cliente c : lista) {

            String[] datos = new String[6];
            datos[0] = c.getDni();
            datos[1] = c.getNombre();
            datos[2] = c.getApellidos();
            datos[3] = c.getDireccion();
            datos[4] = String.valueOf(c.getTelefono());
            datos[5] = c.getEmail();

            totalregistros = totalregistros + 1;
            tablaCliente.addRow(datos);
        }

        mostrarClientes.setTMJTableClientes(tablaCliente);

    }

    /**
     * Método que recoge todos los datos de la tabla y los envia a otro método
     * de la vista que se encargará de abrir un JDialog con esos datos.
     */
    public void clienteAControlador() {
        int seleccion = mostrarClientes.getFilaSeleccionada();
        if (seleccion >= 0) {

            String dni = (String) mostrarClientes.getTMJTableClientes().getValueAt(seleccion, 0);
            String nombre = (String) mostrarClientes.getTMJTableClientes().getValueAt(seleccion, 1);
            String apellidos = (String) mostrarClientes.getTMJTableClientes().getValueAt(seleccion, 2);
            String direccion = (String) mostrarClientes.getTMJTableClientes().getValueAt(seleccion, 3);
            int telefono = Integer.parseInt((String) mostrarClientes.getTMJTableClientes().getValueAt(seleccion, 4));
            String email = (String) mostrarClientes.getTMJTableClientes().getValueAt(seleccion, 5);

            Cliente c = new Cliente(dni, nombre, apellidos, direccion, telefono, email);

            ClienteConsultas.consulta().clienteSeleccionado(c);
        }
    }

     public void buscarCliente(String b) {

        Cliente c = ClienteConsultas.consulta().buscarporDNI(b);

        if (c != null) {

            //Borramos el contenido de la tabla
            int numero_filas = tablaCliente.getRowCount();

            for (int i = numero_filas - 1; i > -1; i--) {
                tablaCliente.removeRow(i);
            }

            totalregistros = 0;

            String[] datos = new String[6];
            datos[0] = c.getDni();
            datos[1] = c.getNombre();
            datos[2] = c.getApellidos();
            datos[3] = c.getDireccion();
            datos[4] = String.valueOf(c.getTelefono());
            datos[5] = c.getEmail();

            totalregistros = totalregistros + 1;
            tablaCliente.addRow(datos);
        } else {
            mostrarClientes.muestraExcepcionClienteNulo();
        }

    }

}