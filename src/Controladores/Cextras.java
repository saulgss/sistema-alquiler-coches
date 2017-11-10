/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelo.Extras;
import Modelo.ExtrasConsultas;
import Vistas.JFrameExtras;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

/**
 * Controlador del JFrame de Extras.
 * Recoge los datos de la vista y los pasa al modelo y viceversa.
 * @author Rafa
 */
public class Cextras {

    private JFrameExtras vistaExtras;
    private DefaultTableModel tablaExtra;
    private int totalregistros;

    /**
     * Constructor de la clase que enlazará el controlador con los vehiculos
     *
     * @param vistaExtra
     */
    public Cextras(JFrameExtras vistaExtra) {
        this.vistaExtras = vistaExtra;
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
    public void rellenaTablaExtras() {

        // Creamos una nueva tabla la cual le sobreescribiremos un parámetro para que no se puedan editar las celdas.
        tablaExtra = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Creamos las columnas de nuestra tabla.
        tablaExtra.addColumn("Id");
        tablaExtra.addColumn("Nombre");
        tablaExtra.addColumn("Descripcion");
        tablaExtra.addColumn("Precio");

        //Creamos un Array con los datos correspondientes que extraerá de la base de datos.
        totalregistros = 0;

        Extras[] lista = ExtrasConsultas.consulta().listarExtras();
        for (Extras ext : lista) {

            String[] datos = new String[4];
            datos[0] = String.valueOf(ext.getIdExtra());
            datos[1] = ext.getNombre();
            datos[2] = ext.getDescripcion();
            datos[3] = String.valueOf(ext.getPrecioAlquiler());

            totalregistros = totalregistros + 1;
            tablaExtra.addRow(datos);

        }

        vistaExtras.setTMJTableExtras(tablaExtra);

    }

    /**
     * Metodo que revisa la fila seleccionada. Después crea un nuevo objeto e
     * inicializa la id del objeto vehiculo a la de la posición 0 de nuestra
     * tabla. Posteriormente crea una sentencia SQL y prepara un Statement para
     * que el borrado del registro sea satisfactorio. Finalmente repinta la
     * tabla.
     */
    public void borraExtra() {

        int seleccion = vistaExtras.getFilaSeleccionada();
        if (seleccion >= 0) {

            String id = (String) vistaExtras.getTMJTableExtras().getValueAt(seleccion, 0);
            Extras ext = ExtrasConsultas.consulta().buscaId(Integer.parseInt(id));

            try {
                ExtrasConsultas.consulta().borrar(ext);
                rellenaTablaExtras();
                vistaExtras.repaint();
                vistaExtras.ocultarColumnaId();
            } catch (SQLException esql) {
                vistaExtras.muestraExcepcionSQL();
                esql.printStackTrace();
            }

        }

    }

    /**
     * Método que recoge todos los datos de la tabla y los envia a otro método
     * de la vista que se encargará de abrir un JDialog con esos datos.
     */
    public void editarExtra() {
        int seleccion = vistaExtras.getFilaSeleccionada();
        if (seleccion >= 0) {

            int idExtra = Integer.parseInt((String) vistaExtras.getTMJTableExtras().getValueAt(seleccion, 0));
            String nombre = (String) vistaExtras.getTMJTableExtras().getValueAt(seleccion, 1);
            String descripcion = (String) vistaExtras.getTMJTableExtras().getValueAt(seleccion, 2);
            double precio_alquiler = Double.parseDouble((String) vistaExtras.getTMJTableExtras().getValueAt(seleccion, 3));

            vistaExtras.AbreEditarExtra(idExtra, nombre, descripcion, precio_alquiler);

        }
    }

    public void buscarExtra(String b) {

        Extras[] lista = ExtrasConsultas.consulta().buscarPorNombre(b);

        if (lista != null) {

            //Borramos el contenido de la tabla
            int numero_filas = tablaExtra.getRowCount();

            for (int i = numero_filas - 1; i > -1; i--) {
                tablaExtra.removeRow(i);
            }

            totalregistros = 0;

            for (int i = 0; i < lista.length; i++) {
                if (lista[i] != null) {
                    String[] datos = new String[4];
                    datos[0] = String.valueOf(lista[i].getIdExtra());
                    datos[1] = lista[i].getNombre();
                    datos[2] = lista[i].getDescripcion();
                    datos[3] = String.valueOf(lista[i].getPrecioAlquiler());

                    totalregistros = totalregistros + 1;
                    tablaExtra.addRow(datos);
                }
            }
        } else {
            vistaExtras.muestraExcepcionExtraNulo();
        }
    }

}
