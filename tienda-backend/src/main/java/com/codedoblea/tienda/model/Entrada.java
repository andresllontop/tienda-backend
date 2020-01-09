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
public class Entrada {

    private Long identrada;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate fecha;
    private Proveedor proveedor;
    private Personal personal;

    public Entrada() {
    }

    public Entrada(Long identrada) {
        this.identrada = identrada;
    }

    public Entrada(Long identrada, LocalDate fecha, Proveedor proveedor,
            Personal personal) {
        this.identrada = identrada;
        this.fecha = fecha;
        this.proveedor = proveedor;
        this.personal = personal;
    }

    public Long getIdentrada() {
        return identrada;
    }

    public void setIdentrada(Long identrada) {
        this.identrada = identrada;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Proveedor getProveedor() {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor) {
        this.proveedor = proveedor;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

}
