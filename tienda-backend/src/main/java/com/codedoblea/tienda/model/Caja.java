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
public class Caja {

    private Long idcaja;
    private String nombre;
    private Establecimiento establecimiento;

    public Caja() {
    }

    public Caja(Long idcaja) {
        this.idcaja = idcaja;
    }

    public Caja(Long idcaja, String nombre, Establecimiento establecimiento) {
        this.idcaja = idcaja;
        this.nombre = nombre;
        this.establecimiento = establecimiento;
    }

    public Long getIdcaja() {
        return idcaja;
    }

    public void setIdcaja(Long idcaja) {
        this.idcaja = idcaja;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Establecimiento getEstablecimiento() {
        return establecimiento;
    }

    public void setEstablecimiento(Establecimiento establecimiento) {
        this.establecimiento = establecimiento;
    }

}
