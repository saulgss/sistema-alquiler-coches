/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelo.Reserva;
import Modelo.ReservaConsultas;
import Vistas.JFrameReserva;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

/**
 * Controlador del JFrame de Reserva.
 * Recoge los datos de la vista y los pasa al modelo y viceversa.
 * @author Rafa
 */
public class Creserva {

    private JFrameReserva vistaReservas;
    private DefaultTableModel tablaReserva;
    private int totalregistros;

    /**
     * Constructor de la clase que enlazará el controlador con los vehiculos
     *
     * @param vistaReservas
     */
    public Creserva(JFrameReserva vistaReservas) {
        this.vistaReservas = vistaReservas;
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
    public void rellenaTablaReserva() {

        // Creamos una nueva tabla la cual le sobreescribiremos un parámetro para que no se puedan editar las celdas.
        tablaReserva = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Creamos las columnas de nuestra tabla.
        tablaReserva.addColumn("ID Reserva");
        tablaReserva.addColumn("Matricula Vehiculo");
        tablaReserva.addColumn("Modelo");
        tablaReserva.addColumn("DNI Cliente");
        tablaReserva.addColumn("Cliente");
        tablaReserva.addColumn("DNI Trabajador");
        tablaReserva.addColumn("Trabajador");
        tablaReserva.addColumn("ID Extra");
        tablaReserva.addColumn("Extra");
        tablaReserva.addColumn("Tipo de Tarifa");
        tablaReserva.addColumn("Fecha de Reserva");
        tablaReserva.addColumn("Fecha de Recogida");
        tablaReserva.addColumn("Fecha de Devolución");
        tablaReserva.addColumn("Coste del Alquiler");
        tablaReserva.addColumn("Estado");

        //Creamos un Array con los datos correspondientes que extraerá de la base de datos.
        totalregistros = 0;

        Reserva[] lista = ReservaConsultas.consulta().listarReservas();
        for (Reserva r : lista) {

            String[] datos = new String[15];
            datos[0] = String.valueOf(r.getIdReserva());
            datos[1] = r.getVehiculo().getMatricula();
            datos[2] = r.getVehiculo().getModelo();
            datos[3] = r.getCliente().getDni();
            datos[4] = r.getCliente().getNombre() + " " + r.getCliente().getApellidos();
            datos[5] = r.getTrabajador().getDni();
            datos[6] = r.getTrabajador().getNombre() + " " + r.getTrabajador().getApellidos();
            datos[7] = String.valueOf(r.getExtra().getIdExtra());
            datos[8] = r.getExtra().getNombre();
            datos[9] = r.getTipoTarifa();
            datos[10] = String.valueOf(r.getFechaReserva());
            datos[11] = String.valueOf(r.getFechaRecogida());
            datos[12] = String.valueOf(r.getFechaDevolucion());
            datos[13] = String.valueOf(r.getCostoAlquiler());
            datos[14] = r.getEstado();

            totalregistros = totalregistros + 1;
            tablaReserva.addRow(datos);

        }

        vistaReservas.setTMJTableReserva(tablaReserva);

    }

    /**
     * Metodo que revisa la fila seleccionada. Después crea un nuevo objeto e
     * inicializa la id del objeto vehiculo a la de la posición 0 de nuestra
     * tabla. Posteriormente crea una sentencia SQL y prepara un Statement para
     * que el borrado del registro sea satisfactorio. Finalmente repinta la
     * tabla.
     */
    public void borraReserva() {

        int seleccion = vistaReservas.getFilaSeleccionada();
        if (seleccion >= 0) {

            String id = (String) vistaReservas.getTMJTableReserva().getValueAt(seleccion, 0);
            Reserva r = ReservaConsultas.consulta().buscaId(Integer.parseInt(id));

            try {
                ReservaConsultas.consulta().borrar(r);
                rellenaTablaReserva();
                vistaReservas.repaint();
                vistaReservas.ocultarColumnas();
            } catch (SQLException esql) {
                vistaReservas.muestraExcepcionSQL();
                esql.printStackTrace();
            }

        }

    }

    /**
     * Método que recoge todos los datos de la tabla y los envia a otro método
     * de la vista que se encargará de abrir un JDialog con esos datos.
     */
    public void editarReserva() {
        int seleccion = vistaReservas.getFilaSeleccionada();
        if (seleccion >= 0) {

            int idReserva = Integer.parseInt((String) vistaReservas.getTMJTableReserva().getValueAt(seleccion, 0));
            String matricula = (String) vistaReservas.getTMJTableReserva().getValueAt(seleccion, 1);
            String modelo = (String) vistaReservas.getTMJTableReserva().getValueAt(seleccion, 2);
            String dniCliente = (String) vistaReservas.getTMJTableReserva().getValueAt(seleccion, 3);
            String nyapCliente = (String) vistaReservas.getTMJTableReserva().getValueAt(seleccion, 4);
            String dniTrabajador = (String) vistaReservas.getTMJTableReserva().getValueAt(seleccion, 5);
            String nyapTrabajador = (String) vistaReservas.getTMJTableReserva().getValueAt(seleccion, 6);
            int idExtra = Integer.parseInt((String) vistaReservas.getTMJTableReserva().getValueAt(seleccion, 7));
            String nombreExtra = (String) vistaReservas.getTMJTableReserva().getValueAt(seleccion, 8);
            String tipoTarifa = (String) vistaReservas.getTMJTableReserva().getValueAt(seleccion, 9);
            Date fechaReserva = Date.valueOf((String) vistaReservas.getTMJTableReserva().getValueAt(seleccion, 10));
            Date fechaRecogida = Date.valueOf((String) vistaReservas.getTMJTableReserva().getValueAt(seleccion, 11));
            Date fechaDevolucion = Date.valueOf((String) vistaReservas.getTMJTableReserva().getValueAt(seleccion, 12));
            double costoAlquiler = Double.parseDouble((String) vistaReservas.getTMJTableReserva().getValueAt(seleccion, 13));
            String estado = (String) vistaReservas.getTMJTableReserva().getValueAt(seleccion, 14);

            vistaReservas.abreEditarReserva(idReserva, matricula, modelo, dniCliente, nyapCliente, dniTrabajador, nyapTrabajador, idExtra, nombreExtra, tipoTarifa, fechaReserva, fechaRecogida, fechaDevolucion, costoAlquiler, estado);

        }
    }
    
    /**
     * Método que recoge todos los datos de la tabla y los envia a otro método
     * de la vista que se encargará de abrir un JDialog con esos datos.
     */
    public void realizarPago() {
        int seleccion = vistaReservas.getFilaSeleccionada();
        if (seleccion >= 0) {
            int idReserva = Integer.parseInt((String) vistaReservas.getTMJTableReserva().getValueAt(seleccion, 0));
            String estado = (String) vistaReservas.getTMJTableReserva().getValueAt(seleccion, 14);
                
            if(estado.equals("En Alquiler") || estado.equals("Pago Eliminado")) vistaReservas.abreRealizarPago(idReserva);
            
            else vistaReservas.muestraExcepcionNoPuedeRealizar();
        }
    }
    
    /**
     * Método que recoge todos los datos de la tabla y los envia a otro método
     * de la vista que se encargará de abrir un JDialog con esos datos.
     */
    public void verPago() {
        int seleccion = vistaReservas.getFilaSeleccionada();
        if (seleccion >= 0) {
            int idReserva = Integer.parseInt((String) vistaReservas.getTMJTableReserva().getValueAt(seleccion, 0));
            String estado = (String) vistaReservas.getTMJTableReserva().getValueAt(seleccion, 14);
                
            if(estado.equals("Pagada")) vistaReservas.abreVerPago(idReserva);
            
            else vistaReservas.muestraExcepcionNoPuedeRealizar2();
        }
    }
    
    /**
     * Método para buscar por fecha de la reserva un objeto de tipo reserva
     * Este será almacenado en un arraylist que será enviado desde la consulta de la reserva
     * esta consulta comprobará que haya algún elemento en ese arraylist que devuelva esa fecha de reserva
     * @param f 
     */

    public void buscarReserva(Date f) {

        Reserva[] lista = ReservaConsultas.consulta().buscarPorFecha(f);

        if (lista != null) {

            //Borramos el contenido de la tabla
            int numero_filas = tablaReserva.getRowCount();

            for (int i = numero_filas - 1; i > -1; i--) {
                tablaReserva.removeRow(i);
            }

            totalregistros = 0;

            for (int i = 0; i < lista.length; i++) {
                if (lista[i] != null) {
                    String[] datos = new String[15];
                    datos[0] = String.valueOf(lista[i].getIdReserva());
                    datos[1] = lista[i].getVehiculo().getMatricula();
                    datos[2] = lista[i].getVehiculo().getModelo();
                    datos[3] = lista[i].getCliente().getDni();
                    datos[4] = lista[i].getCliente().getNombre() + " " + lista[i].getCliente().getApellidos();
                    datos[5] = lista[i].getTrabajador().getDni();
                    datos[6] = lista[i].getTrabajador().getNombre() + " " + lista[i].getTrabajador().getApellidos();
                    datos[7] = String.valueOf(lista[i].getExtra().getIdExtra());
                    datos[8] = lista[i].getExtra().getNombre();
                    datos[9] = lista[i].getTipoTarifa();
                    datos[10] = String.valueOf(lista[i].getFechaReserva());
                    datos[11] = String.valueOf(lista[i].getFechaRecogida());
                    datos[12] = String.valueOf(lista[i].getFechaDevolucion());
                    datos[13] = String.valueOf(lista[i].getCostoAlquiler());
                    datos[14] = lista[i].getEstado();

                    totalregistros = totalregistros + 1;
                    tablaReserva.addRow(datos);
                }
            }
        } else {
            vistaReservas.muestraExcepcionReservaNula();
        }
    }

}
