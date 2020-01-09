/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import java.time.LocalDate;

/**
 *
 * @author andres
 */
public class Presentacion {

    private Long idpresentacion;
    private Integer existencia;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate fecha_vencimiento;
    private DetalleProducto detalleproducto;

    public Presentacion() {
    }

    public Presentacion(Long idpresentacion) {
        this.idpresentacion = idpresentacion;
    }

    public Presentacion(Long idpresentacion, Integer existencia, 
            LocalDate fecha_vencimiento, DetalleProducto detalleproducto) {
        this.idpresentacion = idpresentacion;
        this.existencia = existencia;
        this.fecha_vencimiento = fecha_vencimiento;
        this.detalleproducto = detalleproducto;
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

    public LocalDate getFecha_vencimiento() {
        return fecha_vencimiento;
    }

    public void setFecha_vencimiento(LocalDate fecha_vencimiento) {
        this.fecha_vencimiento = fecha_vencimiento;
    }

    public DetalleProducto getDetalleproducto() {
        return detalleproducto;
    }

    public void setDetalleproducto(DetalleProducto detalleproducto) {
        this.detalleproducto = detalleproducto;
    }

}
