/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.sql.*;
import java.util.ArrayList;

/**
 *  Esta clase se dedica a consultar la base de datos de trabajador.
 *  Estos datos los recoge un arraylist.
 *  Tambien tiene método para mostrar los datos
 * @author Rafa
 */
public class TrabajadorConsultas {
    
private static TrabajadorConsultas miConsultaTrabajador = null;

    private ArrayList<Trabajador> listadeTrabajadores;
    private Connection con = ConexionBBDD.con().conectar();
    private String sSQL = "";
    private String sSQL2 = "";

    /**
     * Metodo singelton que devuelve la clase consultas, esto se realiza porque solo
     * tendremos un único objeto de esta clase.
     *
     * @return almacen
     */
    
    public static TrabajadorConsultas consulta() {

        if (miConsultaTrabajador != null) {
            return miConsultaTrabajador;
        } else {
            miConsultaTrabajador = new TrabajadorConsultas();
            return miConsultaTrabajador;
        }
    }

    public TrabajadorConsultas() {
        listadeTrabajadores = new ArrayList<>();
    }
    
    

    /**
     * Método que recorre la base de datos y al terminar almacena los datos
     * obtenidos en un obejto el cual guarda en un arraylist
     *
     * @throws SQLException
     */
    public void obtenTrabajadorBBDD() throws SQLException {
        sSQL = "select p.dni, p.nombre, p.apellidos, p.direccion, p.telefono, p.email, t.sueldo, t.acceso, t.usuario, t.contrasena, t.estado "
                + "from persona p, trabajador t "
                + "where p.dni = t.dni order by p.dni desc";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sSQL);

        while (rs.next()) {

            String dni = rs.getString("dni");
            String nombre = rs.getString("nombre");
            String apellidos = rs.getString("apellidos");
            String direccion = rs.getString("direccion");
            // declaramos la variable telefono, y la recogemos de la base de dato, si el resultado es nulo
            // se iguala a 0 , sino ser iguala a lo que recoge de la base de datos
            int telefono;
            if (rs.getString("telefono") != null) {
                telefono = Integer.parseInt(rs.getString("telefono"));
            } else {
                telefono = 0;
            }
            String email = rs.getString("email");
            double sueldo = Double.parseDouble(rs.getString("sueldo"));
            String acceso = rs.getString("acceso");
            String usuario = rs.getString("usuario");
            String contrasena = rs.getString("contrasena");
            String estado = rs.getString("estado");

            Trabajador t = new Trabajador(dni, nombre, apellidos, direccion, telefono, email, sueldo, acceso, usuario, contrasena, estado);

            guardaTrabajadorArray(t);
        }
    }
    
    /**
     * ***************
     * Función para guardar un nuevo trabajador en el arraylist
     *
     * @param tr
     */
    public void guardaTrabajadorArray(Trabajador tr) {
        listadeTrabajadores.add(tr);
    } 
    
    /**
     * ***************
     * Función para eliminar un cliente del arraylist
     *
     * @param tr
     * 
     */
    
    public void borrarTrabajadorArray(Trabajador tr){
        listadeTrabajadores.remove(tr);
    }

    /**
     * ***************
     * Función que ordena el arraylist en un array para listar los clientes
     *
     * @return
     */
    public Trabajador[] listarTrabajadores() {
        Trabajador t[] = new Trabajador[listadeTrabajadores.size()];

        for (int i = 0; i < listadeTrabajadores.size(); i++) {
            t[i] = listadeTrabajadores.get(i);
        }

        return t;
    }

    /**
     * Método para editar mediante consulta sql un objeto de tipo trabajadorque se
     * le haya pasado.
     *
     * @param t
     * @throws java.sql.SQLException
     */
    public void editar(Trabajador t) throws SQLException {
        sSQL = "update persona set nombre=?,apellidos=?,direccion=?,telefono=?,email=?"
                + " where dni=?";
        
        sSQL2 = "update trabajador set sueldo=?, acceso=?, usuario=?, contrasena=?, estado=?"
                + "where dni=?";

        PreparedStatement pst = con.prepareStatement(sSQL);
        PreparedStatement pst2 = con.prepareStatement(sSQL2);
        pst.setString(1, t.getNombre());
        pst.setString(2, t.getApellidos());
        pst.setString(3, t.getDireccion());
        pst.setInt(4, t.getTelefono());
        pst.setString(5, t.getEmail());
        pst.setString(6, t.getDni());
        
        pst2.setDouble(1, t.getSueldo());
        pst2.setString(2, t.getAcceso());
        pst2.setString(3, t.getUsuario());
        pst2.setString(4, t.getContrasena());
        pst2.setString(5, t.getEstado());
        pst2.setString(6, t.getDni());

        pst.executeUpdate();
        pst2.executeUpdate();

    }

    /**
     * Método para guardar mediante consulta sql un objeto de tipo trabajador que se
     * le haya pasado.
     *
     * @param t
     * @return true si se crea, false si no se puede crear
     * @throws java.sql.SQLException
     * @throws com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
     */
    public boolean nuevo(Trabajador t) throws SQLException, com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException {
        sSQL = "insert into persona (dni, nombre, apellidos, direccion, telefono, email) "
                + "values (?,?,?,?,?,?)";
        sSQL2 = "insert into trabajador (dni, sueldo, acceso, usuario, contrasena, estado) "
                + "values (?,?,?,?,?,?)";

        PreparedStatement pst = con.prepareStatement(sSQL);
        PreparedStatement pst2 = con.prepareStatement(sSQL2);

        pst.setString(1, t.getDni());
        pst.setString(2, t.getNombre());
        pst.setString(3, t.getApellidos());
        pst.setString(4, t.getDireccion());
        pst.setInt(5, t.getTelefono());
        pst.setString(6, t.getEmail());

        pst2.setString(1, t.getDni());
        pst2.setDouble(2, t.getSueldo());
        pst2.setString(3, t.getAcceso());
        pst2.setString(4, t.getUsuario());
        pst2.setString(5, t.getContrasena());
        pst2.setString(6, t.getEstado());

        pst.executeUpdate();
        pst2.executeUpdate();
        guardaTrabajadorArray(t);
        return true;

    }

    /**
     * Método para borrar un trabajador de la base de datos y del arraylist
     * @param t
     * @throws SQLException 
     */
    
    public void borrar(Trabajador t) throws SQLException {
        
        // Creamos la primera consulta y un prepared statement para ella.
        sSQL = "delete from trabajador where dni=?";
        PreparedStatement pst = con.prepareStatement(sSQL);
        pst.setString(1, t.getDni());
        pst.executeUpdate();
        
        // Creamos la segunda sentencia.
        sSQL = "delete from persona where dni=?";
        pst = con.prepareStatement(sSQL);
        pst.setString(1, t.getDni());
        pst.executeUpdate();
        
        // borramos también el cliente del arraylist.
        borrarTrabajadorArray(t);

    }
    
    public Trabajador buscarporDNI(String dni) {
        Trabajador t;
        t = null;

        for (int i = 0; i < listadeTrabajadores.size(); i++) {
            if (dni.equals(this.listadeTrabajadores.get(i).getDni())) {
                t = listadeTrabajadores.get(i);
                return t;
            }
        }
        return null;
    }

}
