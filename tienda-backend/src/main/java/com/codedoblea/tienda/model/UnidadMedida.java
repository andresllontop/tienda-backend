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
public class UnidadMedida {
    private Long idunidad_medida;
    private String abreviatura;
    private String nombre;

    public UnidadMedida() {
    }

    public UnidadMedida(Long idunidad_medida) {
        this.idunidad_medida = idunidad_medida;
    }

    public UnidadMedida(Long idunidad_medida, String abreviatura, String nombre) {
        this.idunidad_medida = idunidad_medida;
        this.abreviatura = abreviatura;
        this.nombre = nombre;
    }

    public Long getIdunidad_medida() {
        return idunidad_medida;
    }

    public void setIdunidad_medida(Long idunidad_medida) {
        this.idunidad_medida = idunidad_medida;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
