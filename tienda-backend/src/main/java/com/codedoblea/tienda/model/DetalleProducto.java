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
    private Color color;
    private UnidadMedida unidad_medida;
    private Producto producto;

    public DetalleProducto() {
    }

    public DetalleProducto(Long iddetalle_producto) {
        this.iddetalle_producto = iddetalle_producto;
    }

    public DetalleProducto(Long iddetalle_producto, Color color, 
            UnidadMedida unidad_medida, Producto producto) {
        this.iddetalle_producto = iddetalle_producto;
        this.color = color;
        this.unidad_medida = unidad_medida;
        this.producto = producto;
    }

    public Long getIddetalle_producto() {
        return iddetalle_producto;
    }

    public void setIddetalle_producto(Long iddetalle_producto) {
        this.iddetalle_producto = iddetalle_producto;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public UnidadMedida getUnidad_medida() {
        return unidad_medida;
    }

    public void setUnidad_medida(UnidadMedida unidad_medida) {
        this.unidad_medida = unidad_medida;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
            
}
