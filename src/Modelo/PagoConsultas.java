/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.sql.*;
import java.util.ArrayList;

/**
 *  Esta clase se dedica a consultar la base de datos de pago
 *  Estos datos los recoge un arraylist.
 *  Tambien tiene método para mostrar los datos
 * @author Sergio Alberca
 */
public class PagoConsultas {

    private static PagoConsultas miConsultaPago = null;

    private ArrayList<Pago> listaDePagos;
    private Connection con = ConexionBBDD.con().conectar();
    private String sSQL = "";

    public static PagoConsultas consulta() {

        if (miConsultaPago != null) {
            return miConsultaPago;
        } else {
            miConsultaPago = new PagoConsultas();
            return miConsultaPago;
        }
    }

    public PagoConsultas() {
        listaDePagos = new ArrayList<>();
    }

    public void obtenPagoBBDD() throws SQLException {
        sSQL = "select p.numero_factura, p.idreserva, p.tipo_pago, p.iva, p.total_pago, "+
             "p.fecha_emision, p.fecha_pago from pago p, reserva r "+
             "where p.idreserva = r.idreserva order by numero_factura desc";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sSQL);

        while (rs.next()) {

            int numFactura = Integer.parseInt(rs.getString("numero_factura"));
            
            // Buscamos un objeto Reserva con la id recogida de la base de datos
            // de ese objeto obtenemos su matricula y su modelo
            Reserva r = ReservaConsultas.consulta().buscaId(Integer.parseInt(rs.getString("idreserva")));
         
            String tipoPago = rs.getString("tipo_pago");
            double iva = Double.parseDouble(rs.getString("iva"));
            double totalPago = Double.parseDouble(rs.getString("total_pago"));
            Date fechaEmision = Date.valueOf(rs.getString("fecha_emision"));
            Date fechaPago = Date.valueOf(rs.getString("fecha_pago"));
            
            Pago p = new Pago(numFactura, r, tipoPago, iva, totalPago, fechaEmision, fechaPago);
            
            guardaPagoArray(p);  
            
        }
    }

    public void guardaPagoArray(Pago p) {
        listaDePagos.add(p);
    }

    public void borrarPagoArray(Pago p) {
        listaDePagos.remove(p);
    }
    
    public Pago[] listarPagos() {
        Pago p[] = new Pago[listaDePagos.size()];

        for (int i = 0; i < listaDePagos.size(); i++) {
            p[i] = listaDePagos.get(i);
        }

        return p;
    }
    
    /**
     * Copia a un array de Pagos el ArrayList de Pagos, pero solo copia aquellos objetos
     * que cumplan la condición. Sea esta la de que coincida la id de la reserva.
     * @param idReserva
     * @return 
     */

    public Pago[] listarPagosReserva(int idReserva) {
        Pago p[] = new Pago[listaDePagos.size()];

        for (int i = 0; i < listaDePagos.size(); i++) {
            if(idReserva == this.listaDePagos.get(i).getReserva().getIdReserva())
            p[i] = listaDePagos.get(i);
        }

        return p;
    }

    /**
     * Método para editar mediante consulta sql un objeto de tipo pago que se
     * le haya pasado.
     *
     * @param p
     * @throws java.sql.SQLException
     */
    public void editar(Pago p) throws SQLException {
        sSQL = "update pago set idreserva=?, tipo_pago=?, iva=?, total_pago=?, fecha_emision=?, fecha_pago=?"
                + " where numero_factura=?";

        PreparedStatement pst = con.prepareStatement(sSQL);
        pst.setInt(1, p.getReserva().getIdReserva());
        pst.setString(2, p.getTipoPago());
        pst.setDouble(3, p.getIva());
        pst.setDouble(4, p.getTotalPago());
        pst.setDate(5, p.getFechaEmision());
        pst.setDate(6, p.getFechaPago());  
        pst.setInt(7, p.getNumFactura());

        pst.executeUpdate();
    }

    /**
     * Método para guardar mediante consulta sql un objeto de tipo pago que se
     * le haya pasado.
     *
     * @param p
     * @return true si se crea, false si no se puede crear
     */
    public boolean nuevo(Pago p) throws SQLException {
        sSQL = "insert into pago (numero_factura, idreserva, tipo_pago, iva, total_pago, fecha_emision, fecha_pago) "
                + "values (?,?,?,?,?,?,?)";

        PreparedStatement pst = con.prepareStatement(sSQL);
        pst.setInt(1, p.getNumFactura());
        pst.setInt(2, p.getReserva().getIdReserva());
        pst.setString(3, p.getTipoPago());
        pst.setDouble(4, p.getIva());
        pst.setDouble(5, p.getTotalPago());
        pst.setDate(6, p.getFechaEmision());
        pst.setDate(7, p.getFechaPago());  

        pst.executeUpdate();
        guardaPagoArray(p);
        return true;

    }

    /**
     * Método que devuelve la nueva id en caso de que se cree un nuevo objeto.
     * Si es nula, osea no hay datos porque es la primera en introducir, la
     * iguala a 1. Si no es nula la incrementa.
     *
     * @return la id nueva asignada, o 0 en caso de no asignar ninguna que será
     * un error.
     * @throws java.sql.SQLException
     */
    public int numNuevo() throws SQLException {
        String num;

        // Creamos la sentencia que se va a ejecutar en el ResultSet
        sSQL = "SELECT MAX(numero_factura) AS numero_factura FROM pago";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sSQL);

        while (rs.next()) {
            // Guardamos en la variable el resultado
            num = rs.getString("numero_factura");

            // Comprobamos en caso de que no haya registros en la tabla
            // que la id sea la primera
            int numNuevo;

            if (num == null) {
                numNuevo = 1;
                return numNuevo;
            } else {
                numNuevo = Integer.parseInt(num) + 1;
                return numNuevo;
            }
            
        }
        
        return -1;
    }

    /**
     * Método para borrar de la base de datos y posteriormente del arraylist un
     * objeto de tipo pago de la tabla.
     *
     * @param p
     * @throws SQLException
     */
    public void borrar(Pago p) throws SQLException {

        // Creamos la primera consulta y un prepared statement para ella.
        sSQL = "delete from pago where numero_factura=?";
        PreparedStatement pst = con.prepareStatement(sSQL);
        pst.setInt(1, p.getNumFactura());
        pst.executeUpdate();

        // borramos también el pago del arraylist.
        borrarPagoArray(p);

    }
    
    /**
     * Método para buscar el numero de factura en el arraylist
     * @param num
     * @return 
     */

    public Pago buscaNumero(int num) {
        Pago p;
        p = null;

        for (int i = 0; i < listaDePagos.size(); i++) {
            if (num == (this.listaDePagos.get(i).getNumFactura())) {
                p = listaDePagos.get(i);
            }
        }
        return p;
    }
    
    /**
     * Método que busca por fecha en el arraylist la reserva
     * @param fechaEmision
     * @return 
     */

    public Pago[] buscarPorFecha(Date fechaEmision) {    
        
        // copiamos el arraylist a un array
        Pago p[] = new Pago[listaDePagos.size()];

        // recorremos ese array con un bucle for, copiando los elementos que encuentre iguales
        // a ese array que hemos creado.
        for (int i = 0; i < listaDePagos.size(); i++) {
            // hacemos un matches con una expresión regular para comprobar que el nombre se guala a lo que hemos introducido
            if (listaDePagos.get(i).getFechaEmision().equals(fechaEmision) ) {
            p[i] = listaDePagos.get(i);
            return p;
            }
        }
        return null;
    }

}