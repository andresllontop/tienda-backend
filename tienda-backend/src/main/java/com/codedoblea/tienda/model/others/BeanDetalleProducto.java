/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.model.others;

import com.codedoblea.tienda.model.DetalleProducto;
import com.codedoblea.tienda.model.ColorDetalleProducto;
import java.util.List;

/**
 *
 * @author andres
 */
public class BeanDetalleProducto {

    private DetalleProducto detalle_producto;
    private List<ColorDetalleProducto> list;

    public BeanDetalleProducto() {
    }

    public BeanDetalleProducto(DetalleProducto detalle_producto, List<ColorDetalleProducto> list) {
        this.detalle_producto = detalle_producto;
        this.list = list;
    }

    public DetalleProducto getDetalle_producto() {
        return detalle_producto;
    }

    public void setDetalle_producto(DetalleProducto detalle_producto) {
        this.detalle_producto = detalle_producto;
    }

    public List<ColorDetalleProducto> getList() {
        return list;
    }

    public void setList(List<ColorDetalleProducto> list) {
        this.list = list;
    }

}
