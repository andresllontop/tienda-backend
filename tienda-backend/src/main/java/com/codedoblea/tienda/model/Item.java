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
public class Item {
    private Long iditem;
    private String nombre;
    private Short indice;

    public Item() {
    }

    public Item(Long iditem) {
        this.iditem = iditem;
    }

    public Item(Long idItem, String nombre, Short indice) {
        this.iditem = idItem;
        this.nombre = nombre;
        this.indice = indice;
    }

    public Short getIndice() {
        return indice;
    }

    public void setIndice(Short indice) {
        this.indice = indice;
    }

    public Long getIditem() {
        return iditem;
    }

    public void setIditem(Long iditem) {
        this.iditem = iditem;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
}
