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
public class DetalleEntrada {
    private Long iddetalle_entrada;
    private Integer cantidad;
    private Double precio;
    private Entrada entrada;
    private Presentacion presentacion;

    public DetalleEntrada(Long iddetalle_entrada, Integer cantidad, Double precio, 
            Entrada entrada, Presentacion presentacion) {
        this.iddetalle_entrada = iddetalle_entrada;
        this.cantidad = cantidad;
        this.precio = precio;
        this.entrada = entrada;
        this.presentacion = presentacion;
    }

    public DetalleEntrada(Long iddetalle_entrada) {
        this.iddetalle_entrada = iddetalle_entrada;
    }

    public DetalleEntrada() {
    }

    public Long getIddetalle_entrada() {
        return iddetalle_entrada;
    }

    public void setIddetalle_entrada(Long iddetalle_entrada) {
        this.iddetalle_entrada = iddetalle_entrada;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Entrada getEntrada() {
        return entrada;
    }

    public void setEntrada(Entrada entrada) {
        this.entrada = entrada;
    }

    public Presentacion getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(Presentacion presentacion) {
        this.presentacion = presentacion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
    
}
