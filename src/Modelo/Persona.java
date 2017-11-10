/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 * Crea una nueva clase de tipo Persona que tendrá unos atributos específicos.
 * @author Rafa
 */
public class Persona {
    
    private String dni;
    private String nombre;
    private String apellidos;
    private String direccion;
    private int telefono;
    private String email;

    public Persona() {
    }

    public Persona(String dni, String nombre, String apellidos, String direccion, int telefono, String email) {
        if (comprobarDni(dni)) {
        this.dni = dni;
        } else {
            throw new IllegalArgumentException("El NIF no es válido, introduzca otro");
        }
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.direccion = direccion;
        this.telefono = telefono;
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    } 
    
    /**
     * Método que comprueba que un dni es correcto
     * @param dni
     * @return boolean true si la comprobación es correcta, falso en caso contrario
     */
    public static boolean comprobarDni(String dni){   
        boolean resultado = false;
        // Guardamos todas las posibles letras de un DNI en una variable
        String letraValida = "TRWAGMYFPDXBNJZSQVHLCKET";

        // creamos una variable numero que convierte a entero la parte numerica
        // lo recoge restandole 1 a la longitud del DNI y recogiendo con un substring
        // la otra parte, después lo convierte a entero con parseInt
        int numero = Integer.parseInt(dni.substring(0, dni.length() - 1));
        char letra = Character.toUpperCase(dni.charAt(dni.length() - 1));
        
        if ((numero > 999999 && numero < 99999999) && (letra >= 65 && letra <= 90)) {
            // crea una variable para comprobar el número de la Letra
            int numLetra = (int) numero % 23;
            
            // comprueba que lo obtenido en la variable no sea igual a la comprobación anterior de la letra
            if (letraValida.charAt(numLetra) != letra) {
                resultado = false;
            } else {
                resultado = true;
            }
        }
        return resultado;
   }
    
}
