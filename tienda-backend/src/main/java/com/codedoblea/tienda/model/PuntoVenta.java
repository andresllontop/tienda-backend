/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.model;

/**
 *
 * @author andres
 */
public class PuntoVenta {
    private Long idpunto_venta;
    private String nombre;
    private Establecimiento establecimiento;
    private Almacen almacen;
    
    public PuntoVenta() {
    }

    public PuntoVenta(Long idpunto_venta) {
        this.idpunto_venta = idpunto_venta;
    }

    public PuntoVenta(Long idpunto_venta, String nombre,
            Establecimiento establecimiento, Almacen almacen) {
        this.idpunto_venta = idpunto_venta;
        this.nombre = nombre;
        this.establecimiento = establecimiento;
        this.almacen = almacen;
    }

    public Establecimiento getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(Establecimiento establecimiento) {
        this.establecimiento = establecimiento;
    }

    public Almacen getAlmacen() {
        return almacen;
    }

    public void setAlmacen(Almacen almacen) {
        this.almacen = almacen;
    }

    public Long getIdpunto_venta() {
        return idpunto_venta;
    }

    public void setIdpunto_venta(Long idpunto_venta) {
        this.idpunto_venta = idpunto_venta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    
}
