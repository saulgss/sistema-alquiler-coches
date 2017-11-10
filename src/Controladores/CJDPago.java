/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelo.Pago;
import Modelo.PagoConsultas;
import Modelo.Reserva;
import Modelo.ReservaConsultas;
import Modelo.VehiculoConsultas;
import Vistas.JDPago;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.table.DefaultTableModel;

/**
 * Controlador del JDialog de Pago.
 * Recoge los datos de la vista y los pasa al modelo y viceversa.
 * @author Rafa
 */
public class CJDPago {

    private JDPago vistaPagos;
    private DefaultTableModel tablaExtra;
    private DefaultTableModel tablaPago;
    private double iva = 0.21;

    public CJDPago(JDPago vistaPagos) {
        this.vistaPagos = vistaPagos;
    }

    /**
     * Método que imprimirá los datos del arrayliat de Extras en la tabla que
     * tenemos en nuestro JDialog de Pagos
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
        
        Reserva r;
        if (vistaPagos.getIdReserva().equals("")){
            int idReserva = Integer.parseInt((String) vistaPagos.getTMJTablePagos().getValueAt(0, 1));
            r = ReservaConsultas.consulta().buscaId(idReserva);
        }else {
            r = ReservaConsultas.consulta().buscaId(Integer.parseInt(vistaPagos.getIdReserva()));
        }
         
        String[] datos = new String[4];
        datos[0] = String.valueOf(r.getExtra().getIdExtra());
        datos[1] = r.getExtra().getNombre();
        datos[2] = r.getExtra().getDescripcion();
        datos[3] = String.valueOf(r.getExtra().getPrecioAlquiler());

        tablaExtra.addRow(datos);
        
        vistaPagos.setTMJTableExtras(tablaExtra);
    }
    
    /**
     * Método que imprimirá los datos del arraylist de Pagos en la tabla que
     * tenemos en nuestro JDialog de Pagos pero sólo los que sean de la misma ID que la reserva seleccionada.
     */
    public void rellenaTablaPagosPorId() {

        // Creamos una nueva tabla la cual le sobreescribiremos un parámetro para que no se puedan editar las celdas.
        tablaPago = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Creamos las columnas de nuestra tabla.
        tablaPago.addColumn("Número de factura");
        tablaPago.addColumn("ID Reserva");
        tablaPago.addColumn("Fecha de Devolución de la reserva");
        tablaPago.addColumn("Matrícula Vehiculo de reserva");
        tablaPago.addColumn("Tipo de Pago");
        tablaPago.addColumn("IVA");
        tablaPago.addColumn("Pago Final");
        tablaPago.addColumn("Fecha de Emisión");
        tablaPago.addColumn("Fecha de Pago");

        Pago[] lista = PagoConsultas.consulta().listarPagosReserva(Integer.parseInt(vistaPagos.getIdReserva()));

        for (Pago p : lista) {
            if (p != null) {
            String[] datos = new String[9];
            datos[0] = String.valueOf(p.getNumFactura());
            datos[1] = String.valueOf(p.getReserva().getIdReserva());
            datos[2] = String.valueOf(p.getReserva().getFechaDevolucion());
            datos[3] = p.getReserva().getVehiculo().getMatricula();
            datos[4] = p.getTipoPago();
            datos[5] = String.valueOf(p.getIva());
            datos[6] = String.valueOf(p.getTotalPago());
            datos[7] = String.valueOf(p.getFechaEmision());
            datos[8] = String.valueOf(p.getFechaPago());

            tablaPago.addRow(datos);
            }
        }
        vistaPagos.setTMJTablePagos(tablaPago);
    }
    
    /**
     * Rellena los campos del los TextField de la vista con los parámetros
     * de la reserva seleccionada en el JFrame reserva
     */
    public void rellenaCamposPagoNuevo() {
        
        Reserva r = ReservaConsultas.consulta().buscaId(Integer.parseInt(vistaPagos.getIdReserva()));
        
        try {
            vistaPagos.setNumFactura(PagoConsultas.consulta().numNuevo());
        } catch (SQLException ex) {
            vistaPagos.muestraExcepcionSQL();
        }
        
        vistaPagos.setMatricula(r.getVehiculo().getMatricula());
        double costeReserva = r.getCostoAlquiler();
        vistaPagos.setCosteReserva(costeReserva);
        vistaPagos.setIva(iva);
        
        // vamos a hacer la opreación para calcular el total del pago
        double total = (costeReserva*iva) + costeReserva;
        vistaPagos.setTotalPago(total);
    }
    
    /**
     * Rellena los campos del los TextField de la vista con los parámetros
     * de la tabla de pagos
     */
    public void rellenaCamposPagoEditar() {
        
        int seleccion = vistaPagos.getFilaSeleccionadaPagos();
        if (seleccion >= 0) {

            int numFactura = Integer.parseInt((String) vistaPagos.getTMJTablePagos().getValueAt(seleccion, 0));
            int idReserva = Integer.parseInt((String) vistaPagos.getTMJTablePagos().getValueAt(seleccion, 1));
            
            Reserva r = ReservaConsultas.consulta().buscaId(idReserva);
            String matriculaReserva = r.getVehiculo().getMatricula();
            double costeReserva = r.getCostoAlquiler();
            
            String tipoPago = (String) vistaPagos.getTMJTablePagos().getValueAt(seleccion, 4);
            double totalPago = Double.parseDouble((String) vistaPagos.getTMJTablePagos().getValueAt(seleccion, 6));
            Date fechaEmision = Date.valueOf((String) vistaPagos.getTMJTablePagos().getValueAt(seleccion, 7));
            Date fechaPago = Date.valueOf((String) vistaPagos.getTMJTablePagos().getValueAt(seleccion, 8));

            vistaPagos.setNumFactura(numFactura);
            vistaPagos.setIdReserva(idReserva);
            vistaPagos.setMatricula(matriculaReserva);
            vistaPagos.setCosteReserva(costeReserva);
            vistaPagos.setIva(iva);
            vistaPagos.setTipoPago(tipoPago);
            vistaPagos.setTotalPago(totalPago);
            vistaPagos.setFechaEmision(fechaEmision);
            vistaPagos.setFechaPago(fechaPago);
        }
    }

    /**
     * Método que guarda los pagos en la base de datos y el arraylist
     */   
    public boolean guardaPago() {
        
            int numFactura = Integer.parseInt(vistaPagos.getNumFactura());
            Reserva r = ReservaConsultas.consulta().buscaId(Integer.parseInt(vistaPagos.getIdReserva()));
            String tipoPago = vistaPagos.getTipoPago();
            double totalPago = Double.parseDouble(vistaPagos.getTotal());
            Date fechaEmision = vistaPagos.getFechaEmision();
            Date fechaPago = vistaPagos.getFechaPago();
            
            Pago p = new Pago(numFactura, r, tipoPago, iva, totalPago, fechaEmision, fechaPago);
            try {
                p.getReserva().setEstado("Pagada");
                p.getReserva().getVehiculo().setEstado("Disponible");
                ReservaConsultas.consulta().actualizaEstado(r.getIdReserva(), r.getEstado());
                VehiculoConsultas.consulta().actualizaEstado(r.getVehiculo().getIdVehiculo(), r.getVehiculo().getEstado());
                
                PagoConsultas.consulta().nuevo(p);
                return true;
            } catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException em) {
                vistaPagos.excepcionRepetida();
                return false;
            } catch (SQLException ex) {
                vistaPagos.muestraExcepcionSQL();
                ex.printStackTrace();
                return false;
            }
    }
    
    /**
     * Método que guarda los pagos en la base de datos y el arraylist
     */   
    public void editaPago() {
        
            int numFactura = Integer.parseInt(vistaPagos.getNumFactura());
            Reserva r = ReservaConsultas.consulta().buscaId(Integer.parseInt(vistaPagos.getIdReserva()));
            String tipoPago = vistaPagos.getTipoPago();
            double totalPago = Double.parseDouble(vistaPagos.getTotal());
            Date fechaEmision = vistaPagos.getFechaEmision();
            Date fechaPago = vistaPagos.getFechaPago();
            
            Pago p = PagoConsultas.consulta().buscaNumero(numFactura);
            
            p.setReserva(r);
            p.setTipoPago(tipoPago);
            p.setIva(iva);
            p.setTotalPago(totalPago);
            p.setFechaEmision(fechaEmision);
            p.setFechaPago(fechaPago);
            
            try {
                PagoConsultas.consulta().editar(p);
            } catch (SQLException ex) {
                vistaPagos.muestraExcepcionSQL();
                ex.printStackTrace();
            }
    }
    
    /**
     * Metodo que revisa la fila seleccionada.
     * Posteriormente crea una sentencia SQL y prepara un Statement para
     * que el borrado del registro sea satisfactorio. Finalmente repinta la
     * tabla.
     */
    public void borraPago() {

        int seleccion = vistaPagos.getFilaSeleccionadaPagos();
        if (seleccion >= 0) {

            int numFactura = Integer.parseInt((String) vistaPagos.getTMJTablePagos().getValueAt(seleccion, 0));
            Pago p = PagoConsultas.consulta().buscaNumero(numFactura);

            try {
                PagoConsultas.consulta().borrar(p);
                p.getReserva().setEstado("Pago Eliminado");
                ReservaConsultas.consulta().actualizaEstado(p.getReserva().getIdReserva(), p.getReserva().getEstado());
                rellenaTablaPagosPorId();
                vistaPagos.repaint();
            } catch (SQLException esql) {
                vistaPagos.muestraExcepcionSQL();
                esql.printStackTrace();
            }
        }
    }
    
    /**
     * Método que calcula la fecha actual del sistema.
     * @return 
     */
    
    public Date fechaActual() {
        Calendar fecha = new GregorianCalendar();
        //Obtenemos el valor del año, mes, día,
        //hora, minuto y segundo del sistema
        //usando el método get y el parámetro correspondiente
        int d = fecha.get(Calendar.DAY_OF_MONTH);
        int m = fecha.get(Calendar.MONTH);
        int a = fecha.get(Calendar.YEAR) -1900;
        
        return new Date(a, m, d);
    }
    
}



