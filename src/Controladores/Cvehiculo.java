/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Vistas.JFrameVehiculo;
import Modelo.Vehiculo;
import Modelo.VehiculoConsultas;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

/**
 * Controlador del JFrame de Vehiculo.
 * Recoge los datos de la vista y los pasa al modelo y viceversa.
 * @author Rafa
 */
public class Cvehiculo {

    private JFrameVehiculo vistaVehiculo;
    private DefaultTableModel tablaVehiculo;
    private int totalregistros;

    /**
     * Constructor de la clase que enlazará el controlador con los vehiculos
     *
     * @param vistaVehiculo
     */
    public Cvehiculo(JFrameVehiculo vistaVehiculo) {  
        this.vistaVehiculo = vistaVehiculo;
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
    public void rellenaTablaVehiculos() {

        // Creamos una nueva tabla la cual le sobreescribiremos un parámetro para que no se puedan editar las celdas.
        tablaVehiculo = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Creamos las columnas de nuestra tabla.
        tablaVehiculo.addColumn("Id");
        tablaVehiculo.addColumn("Matricula");
        tablaVehiculo.addColumn("Modelo");
        tablaVehiculo.addColumn("Cracteristicas");
        tablaVehiculo.addColumn("Precio ");
        tablaVehiculo.addColumn("Estado");
        tablaVehiculo.addColumn("Tipo de Vehiculo");

        //Creamos un Array con los datos correspondientes que extraerá de la base de datos.
        totalregistros = 0;

        Vehiculo[] lista = VehiculoConsultas.consulta().listarVehiculos();
        for (Vehiculo v : lista) {

            String[] datos = new String[7];
            datos[0] = String.valueOf(v.getIdVehiculo());
            datos[1] = v.getMatricula();
            datos[2] = v.getModelo();
            datos[3] = v.getCaracteristicas();
            datos[4] = String.valueOf(v.getPrecioDiario());
            datos[5] = v.getEstado();
            datos[6] = v.getTipoVehiculo();

            totalregistros = totalregistros + 1;
            tablaVehiculo.addRow(datos);

        }

        vistaVehiculo.setTMJTableVehiculos(tablaVehiculo);

    }

    /**
     * Metodo que revisa la fila seleccionada. Después crea un nuevo objeto e
     * inicializa la id del objeto vehiculo a la de la posición 0 de nuestra
     * tabla. Posteriormente crea una sentencia SQL y prepara un Statement para
     * que el borrado del registro sea satisfactorio. Finalmente repinta la
     * tabla.
     */
    public void borraVehiculo() {

        int seleccion = vistaVehiculo.getFilaSeleccionada();
        if (seleccion >= 0) {

            int id = Integer.parseInt( (String) vistaVehiculo.getTMJTableVehiculos().getValueAt(seleccion, 0) );
            Vehiculo v = VehiculoConsultas.consulta().BuscarporId(id);

            try {
                VehiculoConsultas.consulta().borrar(v);
                rellenaTablaVehiculos();
                vistaVehiculo.repaint();
                vistaVehiculo.ocultarColumnaId();
            } catch (SQLException esql) {
                vistaVehiculo.muestraExcepcionSQL();
                esql.printStackTrace();
            }

        }

    }

    /**
     * Método que recoge todos los datos de la tabla y los envia a otro método
     * de la vista que se encargará de abrir un JDialog con esos datos.
     */
    public void editarVehiculo() {
        int seleccion = vistaVehiculo.getFilaSeleccionada();
        if (seleccion >= 0) {

            int idVehiculo = Integer.parseInt((String) vistaVehiculo.getTMJTableVehiculos().getValueAt(seleccion, 0));
            String matricula = (String) vistaVehiculo.getTMJTableVehiculos().getValueAt(seleccion, 1);
            String modelo = (String) vistaVehiculo.getTMJTableVehiculos().getValueAt(seleccion, 2);
            String caracteristicas = (String) vistaVehiculo.getTMJTableVehiculos().getValueAt(seleccion, 3);
            double precio_diario = Double.parseDouble((String) vistaVehiculo.getTMJTableVehiculos().getValueAt(seleccion, 4));
            String estado = (String) vistaVehiculo.getTMJTableVehiculos().getValueAt(seleccion, 5);
            String tipo_vehiculo = (String) vistaVehiculo.getTMJTableVehiculos().getValueAt(seleccion, 5);

            vistaVehiculo.AbreEditarVehiculo(idVehiculo, matricula, modelo, caracteristicas, precio_diario, estado, tipo_vehiculo);

        }
    }

    public void buscarVehiculo(String b) {

        Vehiculo[] lista = VehiculoConsultas.consulta().buscarPorModelo(b);

        if (lista != null) {

            //Borramos el contenido de la tabla
            int numero_filas = tablaVehiculo.getRowCount();

            for (int i = numero_filas - 1; i > -1; i--) {
                tablaVehiculo.removeRow(i);
            }

            totalregistros = 0;

            for (int i = 0; i < lista.length; i++) {
                if (lista[i] != null) {

                    String[] datos = new String[7];
                    datos[0] = String.valueOf(lista[i].getIdVehiculo());
                    datos[1] = lista[i].getMatricula();
                    datos[2] = lista[i].getModelo();
                    datos[3] = lista[i].getCaracteristicas();
                    datos[4] = String.valueOf(lista[i].getPrecioDiario());
                    datos[5] = lista[i].getEstado();
                    datos[6] = lista[i].getTipoVehiculo();

                    totalregistros = totalregistros + 1;
                    tablaVehiculo.addRow(datos);
                }
            }
            
        } else {
            vistaVehiculo.muestraExcepcionVehiculoNulo();
        }

    }

}
