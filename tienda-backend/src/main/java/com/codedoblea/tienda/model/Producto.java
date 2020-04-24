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
public class Producto {

    private Long idproducto;
    private String codigo;
    private String nombre;
    private Double precio_costo;
    private Double ganancia_porcentaje;
    private Double descuento_porcentaje;
    private String descripcion;
    private Short estado;
    /*
1=Activo
0=Inactivo    
     */
    private Short cantidad_minima;
    private Integer cantidad;
    private UnidadMedida unidad_medida;
    private Item categoria;
    private Item marca;
    private Short impuesto;
    private Short indice;
    /*
1=con detalle color
0=sin detalle color    
     */

    public Producto() {
    }

    public Producto(Long idproducto) {
        this.idproducto = idproducto;
    }

    public Producto(Long idproducto, String codigo, String nombre, 
            Double precio_costo,Double ganancia_porcentaje,Double descuento_porcentaje,
            String descripcion, Short estado, Short cantidad_minima,
            UnidadMedida unidad_medida, Item categoria, Item marca,
            Integer cantidad,Short indice,Short impuesto) {
        this.idproducto = idproducto;
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio_costo = precio_costo;
        this.ganancia_porcentaje = ganancia_porcentaje;
        this.descuento_porcentaje = descuento_porcentaje;
        this.descripcion = descripcion;
        this.estado = estado;
        this.cantidad_minima = cantidad_minima;
        this.unidad_medida = unidad_medida;
        this.categoria = categoria;
        this.marca = marca;
        this.cantidad = cantidad;
        this.indice=indice;
        this.impuesto=impuesto;
    }

    public Short getImpuesto() {
        return impuesto;
    }

    public void setImpuesto(Short impuesto) {
        this.impuesto = impuesto;
    }

    public Short getCantidad_minima() {
        return cantidad_minima;
    }

    public void setCantidad_minima(Short cantidad_minima) {
        this.cantidad_minima = cantidad_minima;
    }

    public Long getIdproducto() {
        return idproducto;
    }

    public void setIdproducto(Long idproducto) {
        this.idproducto = idproducto;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Short getEstado() {
        return estado;
    }

    public void setEstado(Short estado) {
        this.estado = estado;
    }

    public UnidadMedida getUnidad_medida() {
        return unidad_medida;
    }

    public void setUnidad_medida(UnidadMedida unidad_medida) {
        this.unidad_medida = unidad_medida;
    }

    public Item getCategoria() {
        return categoria;
    }

    public void setCategoria(Item categoria) {
        this.categoria = categoria;
    }

    public Item getMarca() {
        return marca;
    }

    public void setMarca(Item marca) {
        this.marca = marca;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Short getIndice() {
        return indice;
    }

    public void setIndice(Short indice) {
        this.indice = indice;
    }

    public Double getPrecio_costo() {
        return precio_costo;
    }

    public void setPrecio_costo(Double precio_costo) {
        this.precio_costo = precio_costo;
    }

    public Double getGanancia_porcentaje() {
        return ganancia_porcentaje;
    }

    public void setGanancia_porcentaje(Double ganancia_porcentaje) {
        this.ganancia_porcentaje = ganancia_porcentaje;
    }

    public Double getDescuento_porcentaje() {
        return descuento_porcentaje;
    }

    public void setDescuento_porcentaje(Double descuento_porcentaje) {
        this.descuento_porcentaje = descuento_porcentaje;
    }

}
