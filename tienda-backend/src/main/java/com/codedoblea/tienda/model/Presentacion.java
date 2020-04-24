/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;

/**
 *
 * @author andres
 */
public class Presentacion {

    private Long idpresentacion;
    private Integer existencia;
    private Double valor_unitario;
    private Double valor_total;
    private Double inventario_final;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy hh:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime fecha_vencimiento;
    private Short tipo_producto;
     /*
    1=producto
    2=detalle_producto_colo
    */
    private Producto producto;
    private ColorDetalleProducto detalle_producto_color;

    public Presentacion() {
    }

    public Presentacion(Long idpresentacion) {
        this.idpresentacion = idpresentacion;
    }

    public Presentacion(Long idpresentacion, Integer existencia,
            LocalDateTime fecha_vencimiento, Short tipo_producto,
            Producto producto, ColorDetalleProducto detalle_producto_color) {
        this.idpresentacion = idpresentacion;
        this.existencia = existencia;
        this.fecha_vencimiento = fecha_vencimiento;
        this.tipo_producto = tipo_producto;
        this.producto = producto;
        this.detalle_producto_color = detalle_producto_color;
    }

    public Double getInventario_final() {
        return inventario_final;
    }

    public void setInventario_final(Double inventario_final) {
        this.inventario_final = inventario_final;
    }

    public Double getValor_unitario() {
        return valor_unitario;
    }

    public void setValor_unitario(Double valor_unitario) {
        this.valor_unitario = valor_unitario;
    }

    public Double getValor_total() {
        return valor_total;
    }

    public void setValor_total(Double valor_total) {
        this.valor_total = valor_total;
    }

    public Short getTipo_producto() {
        return tipo_producto;
    }

    public void setTipo_producto(Short tipo_producto) {
        this.tipo_producto = tipo_producto;
    }

    public Long getIdpresentacion() {
        return idpresentacion;
    }

    public void setIdpresentacion(Long idpresentacion) {
        this.idpresentacion = idpresentacion;
    }

    public Integer getExistencia() {
        return existencia;
    }

    public void setExistencia(Integer existencia) {
        this.existencia = existencia;
    }

    public LocalDateTime getFecha_vencimiento() {
        return fecha_vencimiento;
    }

    public void setFecha_vencimiento(LocalDateTime fecha_vencimiento) {
        this.fecha_vencimiento = fecha_vencimiento;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public ColorDetalleProducto getDetalle_producto_color() {
        return detalle_producto_color;
    }

    public void setDetalle_producto_color(ColorDetalleProducto detalle_producto_color) {
        this.detalle_producto_color = detalle_producto_color;
    }

}
