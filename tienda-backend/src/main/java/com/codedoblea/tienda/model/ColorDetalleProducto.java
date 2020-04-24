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
public class ColorDetalleProducto {

    private Long iddetalle_producto_color;
    private Color idcolor;
    private Integer cantidad;
    private DetalleProducto detalle_producto;

    public ColorDetalleProducto() {
    }

    public ColorDetalleProducto(Long iddetalle_producto_color, Color idcolor, Integer cantidad, DetalleProducto detalle_producto) {
        this.iddetalle_producto_color = iddetalle_producto_color;
        this.idcolor = idcolor;
        this.cantidad = cantidad;
        this.detalle_producto = detalle_producto;
    }

    public ColorDetalleProducto(Long iddetalle_producto_color) {
        this.iddetalle_producto_color = iddetalle_producto_color;
    }

    public Long getIddetalle_producto_color() {
        return iddetalle_producto_color;
    }

    public void setIddetalle_producto_color(Long iddetalle_producto_color) {
        this.iddetalle_producto_color = iddetalle_producto_color;
    }

    public Color getIdcolor() {
        return idcolor;
    }

    public void setIdcolor(Color idcolor) {
        this.idcolor = idcolor;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public DetalleProducto getDetalle_producto() {
        return detalle_producto;
    }

    public void setDetalle_producto(DetalleProducto detalle_producto) {
        this.detalle_producto = detalle_producto;
    }

   

}
