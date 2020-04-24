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
public class Salida {

    private Long idsalida;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime fecha;
    private Personal personal;

    public Salida() {
    }

    public Salida(Long idsalida) {
        this.idsalida = idsalida;
    }

    public Salida(Long idsalida, LocalDateTime fecha, Personal personal) {
        this.idsalida = idsalida;
        this.fecha = fecha;
        this.personal = personal;
    }

    public Long getIdsalida() {
        return idsalida;
    }

    public void setIdsalida(Long idsalida) {
        this.idsalida = idsalida;
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
