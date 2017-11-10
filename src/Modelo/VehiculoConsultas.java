/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;
import java.sql.*;
import java.util.ArrayList;

/**
 *  Esta clase se dedica a consultar la base de datos de vehiculo.
 *  Estos datos los recoge un arraylist.
 *  Tambien tiene método para mostrar los datos
 * @author Rafa
 */
public class VehiculoConsultas {
    private static VehiculoConsultas miConsultaVehiculo = null;

    private ArrayList<Vehiculo> listadeVehiculos;
    private ArrayList<Vehiculo> vehiculoSeleccionado;
    private Connection con = ConexionBBDD.con().conectar();
    private String sSQL = "";

    /**
     * Metodo singelton que devuelve el almacen, esto se realiza porque solo
     * tendremos un único objeto de esta clase.
     *
     * @return almacen
     */
    public static VehiculoConsultas consulta() {

        if (miConsultaVehiculo != null) {
            return miConsultaVehiculo;
        } else {
            miConsultaVehiculo = new VehiculoConsultas();
            return miConsultaVehiculo;
        }
    }

    public VehiculoConsultas() {
        listadeVehiculos = new ArrayList<>();
        vehiculoSeleccionado = new ArrayList<>();
    }

    /**
     * Método que recorre la base de datos y al terminar almacena los datos
     * obtenidos en un obejto el cual guarda en un arraylist
     *
     * @throws SQLException
     */
    public void obtenVehiculoBBDD() throws SQLException {
        sSQL = "select * from vehiculo order by idVehiculo";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sSQL);

        while (rs.next()) {

            int idVehiculo = Integer.parseInt(rs.getString("idVehiculo"));
            String matricula = rs.getString("matricula");
            String modelo = rs.getString("modelo");
            String caracteristicas = rs.getString("caracteristicas");
            double precio_diario= Double.parseDouble(rs.getString("precio_diario"));
            String estado = rs.getString("estado");
            String tipo_vehiculo = rs.getString("tipo_vehiculo");

            Vehiculo v = new Vehiculo(idVehiculo, matricula, modelo, caracteristicas, precio_diario, estado,tipo_vehiculo);

            guardaVehiculoArray(v);
        }
    }
    
    /**
     * ***************
     * Función para guardar un nuevo cliente en el arraylist
     *
     * @param v
     */
    public void guardaVehiculoArray(Vehiculo v) {
        listadeVehiculos.add(v);
    } 
    
    /**
     * ***************
     * Función para eliminar un cliente del arraylist
     *
     * @param v
     * 
     */
    
    public void borrarVehiculoArray(Vehiculo v){
        listadeVehiculos.remove(v);
    }

    /**
     * ***************
     * Función que ordena el arraylist en un array para listar los clientes
     *
     * @return vehiculo
     */
    public Vehiculo[] listarVehiculos() {
        Vehiculo v[] = new Vehiculo[listadeVehiculos.size()];

        for (int i = 0; i < listadeVehiculos.size(); i++) {
            v[i] = listadeVehiculos.get(i);
        }

        return v;
    }

    /**
     * Método para editar mediante consulta sql un objeto de tipo extra que se
     * le haya pasado.
     *
     * @param v
     * @throws java.sql.SQLException
     */
    public void editar(Vehiculo v) throws SQLException {
        sSQL = "update vehiculo set matricula=?,modelo=?,caracteristicas=?,precio_diario=?,estado=?,tipo_vehiculo=?"
                + " where idVehiculo=?";

        PreparedStatement pst = con.prepareStatement(sSQL);
        pst.setString(1, v.getMatricula());
        pst.setString(2, v.getModelo());
        pst.setString(3, v.getCaracteristicas());
        pst.setDouble(4, v.getPrecioDiario());
        pst.setString(5, v.getEstado());
        pst.setString(6, v.getTipoVehiculo());
        pst.setInt(7, v.getIdVehiculo());

        pst.executeUpdate();

    }
    
    /**
     * Actualiza el Estado del vehiculo en función de la introducido en la vista
     * @param idVehiculo
     * @param estado
     * @throws SQLException 
     */
    
    public void actualizaEstado(int idVehiculo, String estado) throws SQLException {
        sSQL = "update vehiculo set estado=? where idVehiculo=?";
        
        PreparedStatement pst = con.prepareStatement(sSQL);
        pst.setString(1, estado);
        pst.setInt(2, idVehiculo);
        
        pst.executeUpdate();
        
    }

    /**
     * Método para guardar mediante consulta sql un objeto de tipo extra que se
     * le haya pasado.
     * Lanza excepción en caso de que la matricula introducida ya exista.
     * @param v
     * @return true si se crea, false si no se puede crear
     * @throws java.sql.SQLException
     * @throws com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException
     */
    public boolean nuevo(Vehiculo v) throws SQLException, com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException {
        sSQL = "insert into vehiculo (idVehiculo, matricula, modelo, caracteristicas, precio_diario, estado, tipo_vehiculo) "
                + "values (?,?,?,?,?,?,?)";

        PreparedStatement pst = con.prepareStatement(sSQL);

        pst.setInt(1, v.getIdVehiculo());
        pst.setString(2, v.getMatricula());
        pst.setString(3, v.getModelo());
        pst.setString(4, v.getCaracteristicas());
        pst.setDouble(5, v.getPrecioDiario());
        pst.setString(6, v.getEstado());
        pst.setString(7, v.getTipoVehiculo());

        pst.executeUpdate();
        guardaVehiculoArray(v);
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
        sSQL = "SELECT MAX(idVehiculo) AS idVehiculo FROM vehiculo";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sSQL);

        while (rs.next()) {
            // Guardamos en la variable el resultado
            id = rs.getString("idVehiculo");

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
     * Método para borrar un cliente de la base de datos y del arraylist
     * @param v
     * @throws SQLException 
     */
    
    public void borrar(Vehiculo v) throws SQLException {
        
        // Creamos la primera consulta y un prepared statement para ella.
        sSQL = "delete from vehiculo where idVehiculo=?";
        PreparedStatement pst = con.prepareStatement(sSQL);
        pst.setInt(1, v.getIdVehiculo());
        pst.executeUpdate();
        
        // borramos también el cliente del arraylist.
        borrarVehiculoArray(v);

    }
    
    /**
     * Método que busca en el arraylist según la id del vehiculo
     * @param id
     * @return 
     */
    
    public Vehiculo BuscarporId(int id) {
        Vehiculo v;
        v = null;

        for (int i = 0; i < listadeVehiculos.size(); i++) {
            if (id == (this.listadeVehiculos.get(i).getIdVehiculo())) {
                v = listadeVehiculos.get(i);
            }
        }
        return v;
    }
    
    /**
     * Método que busca en el arraylist según la matricula del vehiculo
     * @param matricula
     * @return 
     */
    
    public Vehiculo BuscarporMatricula(String matricula) {
        Vehiculo v;
        v = null;

        for (int i = 0; i < listadeVehiculos.size(); i++) {
            if (matricula.equals(this.listadeVehiculos.get(i).getMatricula())) {
                v = listadeVehiculos.get(i);
            }
        }
        return v;
    }
    
    /**
     * Método que busca en el arraylist según el modelo del vehiculo
     * @param modelo
     * @return 
     */
    
    public Vehiculo[] buscarPorModelo(String modelo) {    
        
        // copiamos el arraylist a un array
        Vehiculo veh[] = new Vehiculo[listadeVehiculos.size()];

        // recorremos ese array con un bucle for, copiando los elementos que encuentre iguales
        // a ese array que hemos creado.
        for (int i = 0; i < listadeVehiculos.size(); i++) {
            // hacemos un matches con una expresión regular para comprobar que el nombre se guala a lo que hemos introducido
            if (listadeVehiculos.get(i).getModelo().matches("(?i).*"+modelo+".*")) {
            veh[i] = listadeVehiculos.get(i);
            }
        }
        return veh;
    }
    
    /** Métodos para las Vistas de Vehiculos Disponibles **/
    
    /**
     * ***************
     * Función para guardar un vehiculo en otro arraylist para luego obtener para la vista el 
     * vehiculo seleccionado
     * @param v
     */
    public void almacenaVehiculoDisponible(Vehiculo v) {
        vehiculoSeleccionado.add(v);
    } 
    
    /**
     * ***************
     * Función que ordena el arraylist en un array para listar el vehiculo seleccionado
     *
     * @return vehiculo
     */
    public Vehiculo[] listarVehiculoDisponible() {
        Vehiculo v[] = new Vehiculo[vehiculoSeleccionado.size()];

        for (int i = 0; i < vehiculoSeleccionado.size(); i++) {
            v[i] = vehiculoSeleccionado.get(i);
        }
        return v;
    }
    
    /**
     * Método que busca en el Arraylist por modelo y filtra solo los que estén diponibles
     * @param modelo
     * @return 
     */
    
    public Vehiculo[] buscarDisponiblePorModelo(String modelo) {    
        
        // copiamos el arraylist a un array
        Vehiculo veh[] = new Vehiculo[listadeVehiculos.size()];

        // recorremos ese array con un bucle for, copiando los elementos que encuentre iguales
        // a ese array que hemos creado.
        for (int i = 0; i < listadeVehiculos.size(); i++) {
            // hacemos un matches con una expresión regular para comprobar que el nombre se guala a lo que hemos introducido
            // la expresión regular indica (?i) será case insensitive, .* toda la palabra que englobe lo recogido mediante la varibale
            if (listadeVehiculos.get(i).getModelo().matches("(?i).*"+modelo+".*") && listadeVehiculos.get(i).getEstado().equals("Disponible")) {
            veh[i] = listadeVehiculos.get(i);
            return veh;
            }
        }
        return null;
    }

    
}
