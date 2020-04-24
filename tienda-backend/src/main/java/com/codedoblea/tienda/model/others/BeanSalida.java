/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.model.others;

import com.codedoblea.tienda.model.DetalleSalida;
import com.codedoblea.tienda.model.Salida;
import java.util.List;

/**
 *
 * @author andres
 */
public class BeanSalida {

    private Salida salida;

    private List<DetalleSalida> list;

    public BeanSalida() {
    }

    public BeanSalida(Salida salida) {
        this.salida = salida;
    }

    public Salida getSalida() {
        return salida;
    }

    public void setSalida(Salida salida) {
        this.salida = salida;
    }

    public List<DetalleSalida> getList() {
        return list;
    }

    public void setList(List<DetalleSalida> list) {
        this.list = list;
    }

}
