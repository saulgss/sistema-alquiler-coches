/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Modelo;

import java.sql.Date;

/**
 * Crea una nueva clase de tipo Pago que tendrá unos atributos específicos.
 * @author Rafa
 */
public class Pago {
    private int numFactura;
    private Reserva reserva;
    private String tipoPago;
    private double iva;
    private double totalPago;
    private Date fechaEmision;
    private Date fechaPago;

    public Pago() {
    }

    public Pago(int numFactura, Reserva reserva, String tipoPago, double IVA, double totalPago, Date fechaEmision, Date fechaPago) {
        this.numFactura = numFactura;
        this.reserva = reserva;
        this.tipoPago = tipoPago;
        this.iva = IVA;
        this.totalPago = totalPago;
        this.fechaEmision = fechaEmision;
        this.fechaPago = fechaPago;
    }

    public int getNumFactura() {
        return numFactura;
    }

    public void setNumFactura(int numFactura) {
        this.numFactura = numFactura;
    }

    public Reserva getReserva() {
        return reserva;
    }

    public void setReserva(Reserva reserva) {
        this.reserva = reserva;
    }

    public String getTipoPago() {
        return tipoPago;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public double getTotalPago() {
        return totalPago;
    }

    public void setTotalPago(double totalPago) {
        this.totalPago = totalPago;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Date getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(Date fechaPago) {
        this.fechaPago = fechaPago;
    }
  
}
