/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.sql.*;
import java.util.ArrayList;

/**
 *  Esta clase se dedica a consultar la base de datos de reserva.
 *  Estos datos los recoge un arraylist.
 *  Tambien tiene método para mostrar los datos
 * @author Sergio Alberca
 */
public class ReservaConsultas {

    private static ReservaConsultas miConsultaReservas = null;

    private ArrayList<Reserva> listaDeReservas;
    private Connection con = ConexionBBDD.con().conectar();
    private String sSQL = "";

    public static ReservaConsultas consulta() {

        if (miConsultaReservas != null) {
            return miConsultaReservas;
        } else {
            miConsultaReservas = new ReservaConsultas();
            return miConsultaReservas;
        }
    }

    public ReservaConsultas() {
        listaDeReservas = new ArrayList<>();
    }

    public void obtenReservaBBDD() throws SQLException {
        sSQL = "select r.idreserva, r.matricula_vehiculo, r.dni_cliente, r.dni_trabajador, r.idextra,"+
             "r.tipo_tarifa, r.fecha_reserva, r.fecha_recogida, r.fecha_devolucion, r.costo_alquiler, r.estado "+
             "from reserva r, vehiculo v where r.matricula_vehiculo = v.matricula order by idreserva desc";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sSQL);

        while (rs.next()) {

            int idReserva = Integer.parseInt(rs.getString("idreserva"));
            
            // Buscamos un objeto vehiculo con la matricula recogida de la base de datos
            // de ese objeto obtenemos su matricula y su modelo
            Vehiculo v = VehiculoConsultas.consulta().BuscarporMatricula( rs.getString("matricula_vehiculo") );
            
            // Buscamos un objeto cliente con el nif recogido de la base de datos
            // de ese objeto obtenemos su nif, nombre y apellidos
            Cliente c = ClienteConsultas.consulta().buscarporDNI( rs.getString("dni_cliente") );
            
            // Idem con trabajador
            Trabajador t = TrabajadorConsultas.consulta().buscarporDNI(  rs.getString("dni_trabajador")  );
            
            //Idem con extra
            Extras e = ExtrasConsultas.consulta().buscaId( Integer.parseInt(rs.getString("idextra")) );
         
            String tipoTarifa = rs.getString("tipo_tarifa");
            Date fechaReserva = Date.valueOf(rs.getString("fecha_reserva"));
            Date fechaRecogida = Date.valueOf(rs.getString("fecha_recogida"));
            Date fechaDevolucion = Date.valueOf(rs.getString("fecha_devolucion"));
            double costoAlquiler = Double.parseDouble(rs.getString("costo_alquiler"));
            String estado = rs.getString("estado");
            
            Reserva r = new Reserva(idReserva, v, c, t, e, tipoTarifa, fechaReserva, fechaRecogida, fechaDevolucion, costoAlquiler, estado);
            
            guardaReservaArray(r);  
            
        }
    }

    public void guardaReservaArray(Reserva r) {
        listaDeReservas.add(r);
    }

    public void borrarReservaArray(Reserva r) {
        listaDeReservas.remove(r);
    }

    public Reserva[] listarReservas() {
        Reserva r[] = new Reserva[listaDeReservas.size()];

        for (int i = 0; i < listaDeReservas.size(); i++) {
            r[i] = listaDeReservas.get(i);
        }

        return r;
    }

    /**
     * Método para editar mediante consulta sql un objeto de tipo extra que se
     * le haya pasado.
     *
     * @param r
     * @throws java.sql.SQLException
     */
    public void editar(Reserva r) throws SQLException {
        sSQL = "update reserva set matricula_vehiculo=?, dni_cliente=?, dni_trabajador=?, idextra=?, tipo_tarifa=?, fecha_reserva=?, fecha_recogida=?, fecha_devolucion=?, costo_alquiler=?, estado=?"
                + " where idreserva=?";

        PreparedStatement pst = con.prepareStatement(sSQL);
        pst.setString(1, r.getVehiculo().getMatricula());
        pst.setString(2, r.getCliente().getDni());
        pst.setString(3, r.getTrabajador().getDni());
        pst.setInt(4, r.getExtra().getIdExtra() );
        pst.setString(5, r.getTipoTarifa());
        pst.setDate(6, r.getFechaReserva());
        pst.setDate(7, r.getFechaRecogida());
        pst.setDate(8, r.getFechaDevolucion());
        pst.setDouble(9, r.getCostoAlquiler());
        pst.setString(10, r.getEstado());
        pst.setInt(11, r.getIdReserva());

        pst.executeUpdate();

    }
    
    /**
     * Actualiza el estado de la reserva en la base de datos según la id de ese reserva
     * El estado es el pasado por el controlador
     * @param idreserva
     * @param estado
     * @throws SQLException 
     */
    
    public void actualizaEstado(int idreserva, String estado) throws SQLException {
        sSQL = "update reserva set estado=? where idreserva=?";
        
        PreparedStatement pst = con.prepareStatement(sSQL);
        pst.setString(1, estado);
        pst.setInt(2, idreserva);
        
        pst.executeUpdate();
        
    }

    /**
     * Método para guardar mediante consulta sql un objeto de tipo extra que se
     * le haya pasado.
     *
     * @param r
     * @return true si se crea, false si no se puede crear
     */
    public boolean nuevo(Reserva r) throws SQLException {
        sSQL = "insert into reserva (idreserva, matricula_vehiculo, dni_cliente, dni_trabajador, idextra, tipo_tarifa, fecha_reserva, fecha_recogida, fecha_devolucion, costo_alquiler, estado) "
                + "values (?,?,?,?,?,?,?,?,?,?,?)";

        PreparedStatement pst = con.prepareStatement(sSQL);

        pst.setInt(1, r.getIdReserva());
        pst.setString(2, r.getVehiculo().getMatricula());
        pst.setString(3, r.getCliente().getDni());
        pst.setString(4, r.getTrabajador().getDni());
        pst.setInt(5, r.getExtra().getIdExtra());
        pst.setString(6, r.getTipoTarifa());
        pst.setDate(7, r.getFechaReserva());
        pst.setDate(8, r.getFechaRecogida());
        pst.setDate(9, r.getFechaDevolucion());
        pst.setDouble(10, r.getCostoAlquiler());
        pst.setString(11, r.getEstado());

        pst.executeUpdate();
        guardaReservaArray(r);
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
    public int idNueva() throws SQLException {
        String id;

        // Creamos la sentencia que se va a ejecutar en el ResultSet
        sSQL = "SELECT MAX(idreserva) AS idreserva FROM reserva";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sSQL);

        while (rs.next()) {
            // Guardamos en la variable el resultado
            id = rs.getString("idreserva");

            // Comprobamos en caso de que no haya registros en la tabla
            // que la id sea la primera
            int idNuevo;

            if (id == null) {
                idNuevo = 1;
                return idNuevo;
            } else {
                idNuevo = Integer.parseInt(id) + 1;
                return idNuevo;
            }
        }
        return -1;
    }

    /**
     * Método para borrar de la base de datos y posteriormente del arraylist un
     * objeto de tipo extra de la tabla.
     *
     * @param r
     * @throws SQLException
     */
    public void borrar(Reserva r) throws SQLException {

        // Creamos la primera consulta y un prepared statement para ella.
        sSQL = "delete from reserva where idreserva=?";
        PreparedStatement pst = con.prepareStatement(sSQL);
        pst.setInt(1, r.getIdReserva());
        pst.executeUpdate();

        // borramos también la reserva del arraylist.
        borrarReservaArray(r);

    }
    
    /**
     * Método para buscar la ID de la reserva en el arraylist
     * @param id
     * @return 
     */

    public Reserva buscaId(int id) {
        Reserva r;
        r = null;

        for (int i = 0; i < listaDeReservas.size(); i++) {
            if (id == (this.listaDeReservas.get(i).getIdReserva())) {
                r = listaDeReservas.get(i);
            }
        }
        return r;
    }
    
    /**
     * Método que busca por fecha en el arraylist la reserva
     * @param fechaReserva
     * @return 
     */

    public Reserva[] buscarPorFecha(Date fechaReserva) {    
        
        // copiamos el arraylist a un array
        Reserva r[] = new Reserva[listaDeReservas.size()];

        // recorremos ese array con un bucle for, copiando los elementos que encuentre iguales
        // a ese array que hemos creado.
        for (int i = 0; i < listaDeReservas.size(); i++) {
            // hacemos un matches con una expresión regular para comprobar que el nombre se guala a lo que hemos introducido
            if (listaDeReservas.get(i).getFechaReserva().equals(fechaReserva) ) {
            r[i] = listaDeReservas.get(i);
            return r;
            }
        }
        return null;
    }

}