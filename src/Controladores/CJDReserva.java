/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controladores;

import Modelo.*;
import Vistas.JDReserva;
import java.sql.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Controlador del JDialog de Reserva.
 * Recoge los datos de la vista y los pasa al modelo y viceversa.
 * @author Rafa
 */
public class CJDReserva {

    private JDReserva vistaReserva;

    public CJDReserva(JDReserva vistaReserva) {
        this.vistaReserva = vistaReserva;
    }

    /**
     * Método que recoge los datos introducidos en el JDialog y los guarda en un
     * nuevo objeto de tipo Reserva Posteriormente comprueba si se debe crear
     * uno nuevo (true) o se debe editar un registro (false)
     *
     * @return true si se ha podido guardar, false en caso contario. Esto sirve
     * para que en caso de saltar una excepción no cierre nuestra ventana, que
     * está declarado en el JDialog.
     * @throws NumberFormatException
     */
    public boolean guardar() throws NumberFormatException {

        // declaramos el objeto y la variable.
        Reserva r;
        int idReserva = 0;
        Vehiculo v = VehiculoConsultas.consulta().BuscarporMatricula(vistaReserva.getMatricula());
        Cliente c = ClienteConsultas.consulta().buscarporDNI(vistaReserva.getDniCliente());
        Trabajador t = TrabajadorConsultas.consulta().buscarporDNI(vistaReserva.getDniTrabajador());
        Extras e = ExtrasConsultas.consulta().buscaId(Integer.parseInt(vistaReserva.getIdExtra()));
        String tipoTarifa;
        if (vistaReserva.getTarifa() == true) tipoTarifa = "Basica";
        else tipoTarifa = "Ampliada";
        
        Date fechaRecogida = vistaReserva.getFechaRecogida();
        Date fechaDevolucion = vistaReserva.getFechaDevolucion();
        double costoAlquiler = Double.parseDouble(vistaReserva.getCoste());
        String estado = vistaReserva.getEstado();

        // Guardamos en el objeto los datos
        // Comprueba si es editar (false) o crear uno nuevo (true)
        if (vistaReserva.getFuncion() == true) {
            try {
                // Si el método nuevo no ha devuelto true (no se ha podido crear el objeto), no cerrará la ventana.
                // aqui recogerá el id extra y comprobará si es nulo o no.
                idReserva = ReservaConsultas.consulta().idNueva();

                r = new Reserva(idReserva, v, c, t, e, tipoTarifa,fechaActual(), fechaRecogida, fechaDevolucion, costoAlquiler, estado);
                
                if(estado.equals("En Alquiler")) r.getVehiculo().setEstado("Ocupado");
                else r.getVehiculo().setEstado("Disponible");
                VehiculoConsultas.consulta().actualizaEstado(r.getVehiculo().getIdVehiculo(), r.getVehiculo().getEstado());
                
                if (ReservaConsultas.consulta().nuevo(r) != true) {
                    return false;
                }
            } catch (SQLException esql) {
                vistaReserva.muestraExcepcionSQL();
                esql.printStackTrace();
                return false;
            }

        } else {

            // en caso de editar recogera la id extra directamente del JDialog.
            idReserva = Integer.parseInt(vistaReserva.getIdReserva());

            // igualamos el objeto a la busqueda realizada de la id
            r = ReservaConsultas.consulta().buscaId(idReserva);

            // guardamos los nuevos datos de ese objeto.
            r.setIdReserva(idReserva);
            r.setVehiculo(v);
            r.setCliente(c);
            r.setTrabajador(t);
            r.setExtra(e);
            r.setTipoTarifa(tipoTarifa);
            r.setFechaRecogida(fechaRecogida);
            r.setFechaDevolucion(fechaDevolucion);
            r.setCostoAlquiler(costoAlquiler);
            r.setEstado(estado);
            
            if(estado.equals("En Alquiler")) r.getVehiculo().setEstado("Ocupado");
            else r.getVehiculo().setEstado("Disponible");
            
            try {
                VehiculoConsultas.consulta().actualizaEstado(r.getVehiculo().getIdVehiculo(), r.getVehiculo().getEstado());
                ReservaConsultas.consulta().editar(r);
                return true;
            } catch (SQLException esql) {
                vistaReserva.muestraExcepcionSQL();
                esql.printStackTrace();
            }
        }
        return true;
    }
    
    /**
     * Recibe datos de JDialog muestra vehiculos disponibles y los rellenará en el JDReserva
     */
    
    public void rellenaCamposVehiculo() {
        
        // crea un array que recoge los datos de la clase consulta, el cual recogera los datos del controlador
        Vehiculo[] lista = VehiculoConsultas.consulta().listarVehiculoDisponible();
        
        // hacemos un bucle for para recoger los datos de ese objeto
        for (Vehiculo v : lista) {
            // recogemos solo los dos que nos interesan en este caso, matricula y modelo
            String[] datos = new String[2];
            datos[0] = v.getMatricula();
            datos[1] = v.getModelo();
        
            // llamamos a los método que seleccionan el texto de la matricula y modelo y le pasamos los parametros obtenidos
        vistaReserva.setMatricula(datos[0]);
        vistaReserva.setModelo(datos[1]);
        }
    }
    
    /**
     * Recibe datos de JDialog muestra clientes disponibles y los rellenará en el JDReserva
     */
    
    public void rellenaCamposCliente() {
        
        // crea un array que recoge los datos de la clase consulta, el cual recogera los datos del controlador
        Cliente[] lista = ClienteConsultas.consulta().listarClienteSeleccionado();
        
        // hacemos un bucle for para recoger los datos de ese objeto
        for (Cliente c : lista) {
            // recogemos solo los dos que nos interesan en este caso, matricula y modelo
            String[] datos = new String[3];
            datos[0] = c.getDni();
            datos[1] = c.getNombre();
            datos[2] = c.getApellidos();
        
            // llamamos a los método que seleccionan el texto de la matricula y modelo y le pasamos los parametros obtenidos
        vistaReserva.setDniCliente(datos[0]);
        vistaReserva.setNyapCliente(datos[1] + " " + datos[2]);
        }
    }

    /**
     * Recibe datos de JDialog muestra los extras disponibles y los rellenará en el JDReserva
     */
    
    public void rellenaCamposExtras() {
        
        // crea un array que recoge los datos de la clase consulta, el cual recogera los datos del controlador
        Extras[] lista = ExtrasConsultas.consulta().listarExtraSeleccionado();
        
        // hacemos un bucle for para recoger los datos de ese objeto
        for (Extras e : lista) {
            // recogemos solo los dos que nos interesan en este caso, matricula y modelo
            String[] datos = new String[2];
            datos[0] = String.valueOf( e.getIdExtra() );
            datos[1] = e.getNombre();
        
            // llamamos a los método que seleccionan el texto de la matricula y modelo y le pasamos los parametros obtenidos
        vistaReserva.setIdExtra(Integer.parseInt(datos[0]));
        vistaReserva.setNombreExtra(datos[1]);
        }
    }
    
    /**
     * Método para calcular el precio de coste total de la reserva.
     * Comprueba que no esten vacios los campos de precio, en caso de que estén vacios el precio será 0.
     * Después la variable miliseg sirve para convertir los milisegundos a días, esta se usará
     * para dividir la fecha total que será la resta de la fecha de devolucion menos la de recogida.
     * El resultado se almacena en la variable fechaFinal que la multiplicará por el precio del vehiculo
     * Esto lo sumará a los extras y a la tarifa, que será 0 o 65 dependiendo si es básica o ampliada.
     * @return  funcion. true si se puede realizar, false en caso contrario.
     */
    
    public boolean calculaPrecio() {
        boolean funcion;
        double precioVehiculo;
        double precioExtra;
        
        // comprueba si el campo de matricula está vacio.
        if (vistaReserva.getMatricula().isEmpty()) {
            precioVehiculo = 0;
            funcion = false;
        } else {
            Vehiculo v = VehiculoConsultas.consulta().BuscarporMatricula(vistaReserva.getMatricula());
            precioVehiculo = v.getPrecioDiario();
            funcion = true;
        }
        
        // comprueba si el campo de extra está vacio.
        if (vistaReserva.getIdExtra().isEmpty()) {
            precioExtra = 0;
            funcion = false;
        } else {
            Extras e = ExtrasConsultas.consulta().buscaId(Integer.parseInt(vistaReserva.getIdExtra()));
            precioExtra = e.getPrecioAlquiler();
        }
        
        // creamos una variable con los dias convertidos a milisegundos
        long miliseg = (24 * 60 * 60 * 1000);
        // obtenemos la fecha de recogida del vehiculo en milisegundos
        long fechaRecogida = vistaReserva.getFechaRecogidaMil();
        // idem con la de salida
        long fechaDevolucion = vistaReserva.getFechaDevolucionMil();
        long fechafinal;
        // si la fecha final o la feha de Salida es 1(no se ha introducido fecha de salida) la fecha final es uno
        if (fechaRecogida == 1 || fechaDevolucion == 1) {
            fechafinal = 1;
        } else {
            // en caso contrario la fecha final se tendrá que hacer una operación matemática para calcularse
            fechafinal = (fechaDevolucion - fechaRecogida) / miliseg;
        }
      
        // recoge la tarifa. Si es true será básica, si no será ampliada
        long tarifa;
        // si es básica el precio a sumar será cero, si es ampliada el precio a sumar es de 64 como extra.
        if (vistaReserva.getTarifa() == true) tarifa = 0;
        else tarifa = 64;
        
        double precioFinal = (precioVehiculo*fechafinal) + precioExtra + tarifa;
        
        // le pasamos al textfield de la vista los datos.
        vistaReserva.setCosteTotal(precioFinal);
        return funcion;
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
