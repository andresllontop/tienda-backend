/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.model.others;

import com.codedoblea.tienda.model.DetalleEntrada;
import com.codedoblea.tienda.model.Entrada;
import java.util.List;

/**
 *
 * @author andres
 */
public class BeanEntrada {

    private Entrada entrada;

    private List<DetalleEntrada> list;

    public BeanEntrada() {
    }

    public BeanEntrada(Entrada entrada) {
        this.entrada = entrada;
    }

    public Entrada getEntrada() {
        return entrada;
    }

    public void setEntrada(Entrada entrada) {
        this.entrada = entrada;
    }

    public List<DetalleEntrada> getList() {
        return list;
    }

    public void setList(List<DetalleEntrada> list) {
        this.list = list;
    }

}
