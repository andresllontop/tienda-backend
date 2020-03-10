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
public class SubCategoria {
    private Long idsubcategoria;
    private String nombre;
    private Categoria categoria;

    public SubCategoria() {
    }

    public SubCategoria(Long idsubcategoria) {
        this.idsubcategoria = idsubcategoria;
    }

    public SubCategoria(Long idsubcategoria, String nombre) {
        this.idsubcategoria = idsubcategoria;
        this.nombre = nombre;
    }

    public Long getIdsubcategoria() {
        return idsubcategoria;
    }

    public void setIdsubcategoria(Long idsubcategoria) {
        this.idsubcategoria = idsubcategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
    
}
