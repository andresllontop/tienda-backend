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
public class ArchivoProducto {
    private Long idarchivo_producto;
    private String url;
    private String nombre;
    private Producto producto;

    public ArchivoProducto() {
    }

    public ArchivoProducto(Long idarchivo_producto) {
        this.idarchivo_producto = idarchivo_producto;
    }

    public ArchivoProducto(Long idarchivo_producto, String url, 
            String nombre, Producto producto) {
        this.idarchivo_producto = idarchivo_producto;
        this.url = url;
        this.nombre = nombre;
        this.producto = producto;
    }

    public Long getIdarchivo_producto() {
        return idarchivo_producto;
    }

    public void setIdarchivo_producto(Long idarchivo_producto) {
        this.idarchivo_producto = idarchivo_producto;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
}
