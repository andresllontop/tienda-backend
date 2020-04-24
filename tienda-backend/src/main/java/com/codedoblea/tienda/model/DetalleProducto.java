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
public class DetalleProducto {

    private Long iddetalle_producto;
    private String longitud;
    private Producto producto;

    public DetalleProducto() {
    }

    public DetalleProducto(Long iddetalle_producto) {
        this.iddetalle_producto = iddetalle_producto;
    }

    public DetalleProducto(Long iddetalle_producto, String longitud, Producto producto) {
        this.iddetalle_producto = iddetalle_producto;
        this.longitud = longitud;
        this.producto = producto;
    }

    public Long getIddetalle_producto() {
        return iddetalle_producto;
    }

    public void setIddetalle_producto(Long iddetalle_producto) {
        this.iddetalle_producto = iddetalle_producto;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    

}
