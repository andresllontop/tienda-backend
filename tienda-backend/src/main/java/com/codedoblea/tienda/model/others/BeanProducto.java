/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.model.others;

import com.codedoblea.tienda.model.Producto;
import java.util.List;

/**
 *
 * @author andres
 */
public class BeanProducto {
    private Producto producto;
    private List<BeanDetalleProducto> list;

    public BeanProducto(Producto producto, List<BeanDetalleProducto> list) {
        this.producto = producto;
        this.list = list;
    }

    public BeanProducto() {
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public List<BeanDetalleProducto> getList() {
        return list;
    }

    public void setList(List<BeanDetalleProducto> list) {
        this.list = list;
    }
    
}
