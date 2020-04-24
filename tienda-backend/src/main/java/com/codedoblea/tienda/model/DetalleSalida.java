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
public class DetalleSalida {

    private Long iddetalle_salida;
    private Integer cantidad;
    private Short precio;
    private Salida salida;
    private Presentacion presentacion;

    public DetalleSalida(Long iddetalle_salida, 
            Integer cantidad, Short precio,
            Salida salida, Presentacion presentacion) {
        this.iddetalle_salida = iddetalle_salida;
        this.cantidad = cantidad;
        this.precio = precio;
        this.salida = salida;
        this.presentacion = presentacion;
    }

    public DetalleSalida(Long iddetalle_salida) {
        this.iddetalle_salida = iddetalle_salida;
    }

    public DetalleSalida() {
    }

    public Presentacion getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(Presentacion presentacion) {
        this.presentacion = presentacion;
    }

    public Long getIddetalle_salida() {
        return iddetalle_salida;
    }

    public void setIddetalle_salida(Long iddetalle_salida) {
        this.iddetalle_salida = iddetalle_salida;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Salida getSalida() {
        return salida;
    }

    public void setSalida(Salida salida) {
        this.salida = salida;
    }

    public Short getPrecio() {
        return precio;
    }

    public void setPrecio(Short precio) {
        this.precio = precio;
    }
}
