/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

/**
 * Crea una nueva clase de tipo Vehiculo que tendrá unos atributos específicos.
 * @author Rafa
 */
public class Vehiculo {
    private int idVehiculo;
    private String matricula;
    private String modelo;
    private String caracteristicas;
    private double precioDiario;
    private String estado;
    private String tipoVehiculo;

    public Vehiculo(int idVehiculo, String matricula, String modelo, String caracteristicas, double precioDiario, String estado, String tipoVehiculo) {
        this.idVehiculo = idVehiculo;
        if (compruebaMatricula(matricula)) {
        this.matricula = matricula;
        } else {
            throw new IllegalArgumentException("La matricula no es válida");
        }
        this.modelo = modelo;
        this.caracteristicas = caracteristicas;
        this.precioDiario = precioDiario;
        this.estado = estado;
        this.tipoVehiculo = tipoVehiculo;
    }

    public Vehiculo() {
    }

    public int getIdVehiculo() {
        return idVehiculo;
    }

    public void setIdVehiculo(int idVehiculo) {
        this.idVehiculo = idVehiculo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(String caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public double getPrecioDiario() {
        return precioDiario;
    }

    public void setPrecioDiario(double precioDiario) {
        this.precioDiario = precioDiario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(String tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }
    
    public boolean compruebaMatricula(String matricula) {
        if (matricula.matches("^\\d{4}[ -]?[[B-Z]&&[^QEIOU]]{3}$")) return true;    
        else return false;
    }
    
}
