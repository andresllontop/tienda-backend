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
public class Entrada {

    private Long identrada;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime fecha;
//    private Proveedor proveedor;
    private Personal personal;

    public Entrada() {
    }

    public Entrada(Long identrada) {
        this.identrada = identrada;
    }

    public Entrada(Long identrada, LocalDateTime fecha, Personal personal) {
        this.identrada = identrada;
        this.fecha = fecha;
        this.personal = personal;
    }

  

    public Long getIdentrada() {
        return identrada;
    }

    public void setIdentrada(Long identrada) {
        this.identrada = identrada;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Personal getPersonal() {
        return personal;
    }

    public void setPersonal(Personal personal) {
        this.personal = personal;
    }

}
