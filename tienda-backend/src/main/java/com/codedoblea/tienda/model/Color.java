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
public class Color {

    private Long idcolor;
    private String codigo;
    private String nombre;

    public Color() {
    }

    public Color(Long idcolor) {
        this.idcolor = idcolor;
    }

    public Color(Long idcolor, String codigo, String nombre) {
        this.idcolor = idcolor;
        this.codigo = codigo;
        this.nombre = nombre;
    }

    public Long getIdcolor() {
        return idcolor;
    }

    public void setIdcolor(Long idcolor) {
        this.idcolor = idcolor;
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

}
