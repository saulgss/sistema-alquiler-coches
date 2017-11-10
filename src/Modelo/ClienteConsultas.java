/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.sql.*;
import java.util.ArrayList;

/**
 *  Esta clase se dedica a consultar la base de datos de cliente.
 *  Estos datos los recoge un arraylist.
 *  Tambien tiene método para mostrar los datos
 * @author Rafa
 */
public class ClienteConsultas {

    // inicializa el objeto miConsultaCliente a nulo
    private static ClienteConsultas miConsultaCliente = null;

    private ArrayList<Cliente> listadeClientes;
    private ArrayList<Cliente> clienteSeleccionado;
    private Connection con = ConexionBBDD.con().conectar();
    private String sSQL = "";
    private String sSQL2 = "";

    /**
     * Metodo singelton que devuelve el objeto consulta de clienteconsulta, esto se realiza porque solo
     * tendremos un único objeto de esta clase.
     *
     * @return consulta
     */
    public static ClienteConsultas consulta() {

        if (miConsultaCliente != null) {
            return miConsultaCliente;
        } else {
            miConsultaCliente = new ClienteConsultas();
            return miConsultaCliente;
        }
    }
    
    /**
     * Constructor de la clase. Inicializa los dos ArrayList
     */

    public ClienteConsultas() {
        listadeClientes = new ArrayList<>();
        clienteSeleccionado = new ArrayList<>();
    }
    
    

    /**
     * Método que recorre la base de datos y al terminar almacena los datos
     * obtenidos en un obejeto de tipo cliente el cual guarda en un arraylist
     *
     * @throws SQLException
     */
    public void obtenClienteBBDD() throws SQLException {
        // Creamos la sentencia de SQL para acceder a la base de datos
        sSQL = "select p.dni, p.nombre, p.apellidos, p.direccion, p.telefono, p.email "
                + "from persona p, cliente c "
                + "where p.dni = c.dni order by p.dni desc";

        // el Statement hace un hilo de conexión con la base de datos.
        Statement st = con.createStatement();
        // el ResultSet ejecuta y recoge los datos obtenidos de la base de datos
        // que serán el Statement creado anteriormente, ejecutando la consulta 
        // introducida en la sentencia de sql sSQL.
        ResultSet rs = st.executeQuery(sSQL);

        // mientras el ResulSet devuelva valores
        while (rs.next()) {

            // recoge los datos de la cabecera que se le pase al getString
            String dni = rs.getString("dni");
            String nombre = rs.getString("nombre");
            String apellidos = rs.getString("apellidos");
            String direccion = rs.getString("direccion");
            int telefono = Integer.parseInt(rs.getString("telefono"));
            String email = rs.getString("email");

            // creamos un objeto cliente con los parámetro recogidos del ResulSet
            Cliente c = new Cliente(dni, nombre, apellidos, direccion, telefono, email);

            // guarda en el arraylist listaDeClientes el cliente devuelvo
            guardaClienteArray(c);
        }
    }
    
    /**
     * ***************
     * Función para guardar un nuevo cliente en el arraylist
     *
     * @param cli
     */
    public void guardaClienteArray(Cliente cli) {
        listadeClientes.add(cli);
    } 
    
    /**
     * ***************
     * Función para eliminar un cliente del arraylist
     *
     * @param c
     * 
     */
    
    public void borrarClienteArray(Cliente c){
        listadeClientes.remove(c);
    }

    /**
     * ***************
     * Función que ordena el arraylist en un array para listar los clientes
     *
     * @return
     */
    public Cliente[] listarClientes() {
        Cliente c[] = new Cliente[listadeClientes.size()];

        for (int i = 0; i < listadeClientes.size(); i++) {
            c[i] = listadeClientes.get(i);
        }

        return c;
    }

    /**
     * Método para editar mediante consulta sql un objeto de tipo cliente que se
     * le haya pasado.
     *
     * @param c
     * @throws java.sql.SQLException
     */
    public void editar(Cliente c) throws SQLException {
        sSQL = "update persona set nombre=?,apellidos=?,direccion=?,telefono=?,email=?"
                + " where dni=?";

        // El PreparedStatement le entrega una sentencia SQL cuando se crea.
        PreparedStatement pst = con.prepareStatement(sSQL);
        pst.setString(1, c.getNombre());
        pst.setString(2, c.getApellidos());
        pst.setString(3, c.getDireccion());
        pst.setInt(4, c.getTelefono());
        pst.setString(5, c.getEmail());
        pst.setString(6, c.getDni());

        // ejecuta la consulta con los datos del pst
        pst.executeUpdate();

    }

    /**
     * Método para guardar mediante consulta sql un objeto de tipo cliente que se
     * le haya pasado.
     *
     * @param c
     * @return true si se crea, false si no se puede crear
     * @throws java.sql.SQLException
     * @throws com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
     */
    public boolean nuevo(Cliente c) throws SQLException, com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException {
        sSQL = "insert into persona (dni, nombre, apellidos, direccion, telefono, email) "
                + "values (?,?,?,?,?,?)";
        sSQL2 = "insert into cliente (dni) "
                + "values (?)";

        PreparedStatement pst = con.prepareStatement(sSQL);
        PreparedStatement pst2 = con.prepareStatement(sSQL2);

        pst.setString(1, c.getDni());
        pst.setString(2, c.getNombre());
        pst.setString(3, c.getApellidos());
        pst.setString(4, c.getDireccion());
        pst.setInt(5, c.getTelefono());
        pst.setString(6, c.getEmail());

        pst2.setString(1, c.getDni());

        pst.executeUpdate();
        pst2.executeUpdate();
        guardaClienteArray(c);
        return true;

    }

    /**
     * Método para borrar un cliente de la base de datos y del arraylist
     * @param c
     * @throws SQLException 
     */
    
    public void borrar(Cliente c) throws SQLException {
        
        // Creamos la primera consulta y un prepared statement para ella.
        sSQL = "delete from cliente where dni=?";
        PreparedStatement pst = con.prepareStatement(sSQL);
        pst.setString(1, c.getDni());
        pst.executeUpdate();
        
        // Creamos la segunda sentencia.
        sSQL = "delete from persona where dni=?";
        pst = con.prepareStatement(sSQL);
        pst.setString(1, c.getDni());
        pst.executeUpdate();
        
        // borramos también el cliente del arraylist.
        borrarClienteArray(c);

    }
    
    /**
     * Busca por DNI el cliente del arraylist. El dni lo obtiene del método del controlador.
     * @param dni
     * @return 
     */
    
    public Cliente buscarporDNI(String dni) {
        Cliente c;
        c = null;

        for (int i = 0; i < listadeClientes.size(); i++) {
            if (dni.equals(this.listadeClientes.get(i).getDni())) {
                c = listadeClientes.get(i);
                return c;
            }
        }
        return null;
    }
    
    /** Métodos para las Vistas de Cliente seleccionado **/
    
    /**
     * ***************
     * Función para guardar un cliente en otro arraylist para luego obtener para la vista el 
     * cliente seleccionado
     * @param c
     */
    public void clienteSeleccionado(Cliente c) {
        clienteSeleccionado.add(c);
    } 
    
    /**
     * ***************
     * Función que ordena el arraylist en un array para listar el vehiculo seleccionado
     *
     * @return vehiculo
     */
    public Cliente[] listarClienteSeleccionado() {
        Cliente c[] = new Cliente[clienteSeleccionado.size()];

        for (int i = 0; i < clienteSeleccionado.size(); i++) {
            c[i] = clienteSeleccionado.get(i);
        }
        return c;
    }

}
