/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.sql.*;
import java.util.ArrayList;

/**
 *  Esta clase se dedica a consultar la base de datos de extras.
 *  Estos datos los recoge un arraylist.
 *  Tambien tiene método para mostrar los datos
 * @author Sergio Alberca
 */
public class ExtrasConsultas {

    private static ExtrasConsultas miConsultaExtras = null;

    private ArrayList<Extras> listadeExtras;
    private ArrayList<Extras> extraSeleccionado;
    private Connection con = ConexionBBDD.con().conectar();
    private String sSQL = "";

    public static ExtrasConsultas consulta() {

        if (miConsultaExtras != null) {
            return miConsultaExtras;
        } else {
            miConsultaExtras = new ExtrasConsultas();
            return miConsultaExtras;
        }
    }

    public ExtrasConsultas() {
        listadeExtras = new ArrayList<>();
        extraSeleccionado = new ArrayList<>();
    }

    public void obtenExtrasBBDD() throws SQLException {
        sSQL = "select * from extras order by idextra";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sSQL);

        while (rs.next()) {

            int idextra = Integer.parseInt(rs.getString("idextra"));
            String nombre = rs.getString("nombre");
            String descripcion = rs.getString("descripcion");
            double precio_alquiler = Double.parseDouble(rs.getString("precio_alquiler"));

            Extras ex = new Extras(idextra, nombre, descripcion, precio_alquiler);

            guardaExtrasArray(ex);
        }
    }

    public void guardaExtrasArray(Extras ext) {
        listadeExtras.add(ext);
    }

    public void borrarExtrasArray(Extras ext) {
        listadeExtras.remove(ext);
    }

    public Extras[] listarExtras() {
        Extras ext[] = new Extras[listadeExtras.size()];

        for (int i = 0; i < listadeExtras.size(); i++) {
            ext[i] = listadeExtras.get(i);
        }

        return ext;
    }

    /**
     * Método para editar mediante consulta sql un objeto de tipo extra que se
     * le haya pasado.
     *
     * @param ext
     * @throws java.sql.SQLException
     */
    public void editar(Extras ext) throws SQLException {
        sSQL = "update extras set nombre=?,descripcion=?,precio_alquiler=?"
                + " where idextra=?";

        PreparedStatement pst = con.prepareStatement(sSQL);
        pst.setString(1, ext.getNombre());
        pst.setString(2, ext.getDescripcion());
        pst.setDouble(3, ext.getPrecioAlquiler());
        pst.setInt(4, ext.getIdExtra());

        pst.executeUpdate();

    }

    /**
     * Método para guardar mediante consulta sql un objeto de tipo extra que se
     * le haya pasado.
     *
     * @param ext
     * @return true si se crea, false si no se puede crear
     */
    public boolean nuevo(Extras ext) throws SQLException {
        sSQL = "insert into extras (idextra, nombre, descripcion, precio_alquiler) "
                + "values (?,?,?,?)";

        PreparedStatement pst = con.prepareStatement(sSQL);

        pst.setInt(1, ext.getIdExtra());
        pst.setString(2, ext.getNombre());
        pst.setString(3, ext.getDescripcion());
        pst.setDouble(4, ext.getPrecioAlquiler());

        pst.executeUpdate();
        guardaExtrasArray(ext);
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
        sSQL = "SELECT MAX(idextra) AS idextra FROM extras";

        Statement st = con.createStatement();
        ResultSet rs = st.executeQuery(sSQL);

        while (rs.next()) {
            // Guardamos en la variable el resultado
            id = rs.getString("idextra");

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
     * @param ext
     * @throws SQLException
     */
    public void borrar(Extras ext) throws SQLException {

        // Creamos la primera consulta y un prepared statement para ella.
        sSQL = "delete from extras where idextra=?";
        PreparedStatement pst = con.prepareStatement(sSQL);
        pst.setInt(1, ext.getIdExtra());
        pst.executeUpdate();

        // borramos también el cliente del arraylist.
        borrarExtrasArray(ext);

    }

    public Extras buscaId(int id) {
        Extras ext;
        ext = null;

        for (int i = 0; i < listadeExtras.size(); i++) {
            if (id == (this.listadeExtras.get(i).getIdExtra())) {
                ext = listadeExtras.get(i);
            }
        }
        return ext;
    }
    
    /**
     * Método que buscar por nombre introducido los extras mediante una expresión regular.
     * @param nombre
     * @return 
     */
    
    public Extras[] buscarPorNombre(String nombre) {    
        
        // copiamos el arraylist a un array
        Extras ext[] = new Extras[listadeExtras.size()];

        // recorremos ese array con un bucle for, copiando los elementos que encuentre iguales
        // a ese array que hemos creado.
        for (int i = 0; i < listadeExtras.size(); i++) {
            // hacemos un matches con una expresión regular para comprobar que el nombre se guala a lo que hemos introducido
            if (listadeExtras.get(i).getNombre().matches("(?i).*"+nombre+".*")) {
            ext[i] = listadeExtras.get(i);
            return ext;
            }
        }
        return null;
    }
    
    /** Métodos para las Vistas de Extras seleccionado **/
    
    /**
     * ***************
     * Función para guardar un extra en otro arraylist para luego obtener para la vista el 
     * extra seleccionado
     * @param e
     */
    public void extraSeleccionado(Extras e) {
        extraSeleccionado.add(e);
    } 
    
    /**
     * ***************
     * Función que ordena el arraylist en un array para listar el extra seleccionado
     *
     * @return e
     */
    public Extras[] listarExtraSeleccionado() {
        Extras e[] = new Extras[extraSeleccionado.size()];

        for (int i = 0; i < extraSeleccionado.size(); i++) {
            e[i] = extraSeleccionado.get(i);
        }
        return e;
    }

}
