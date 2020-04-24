/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.dao.impl;

import com.codedoblea.tienda.dao.IColorDetalleProductoDAO;
import com.codedoblea.tienda.model.ColorDetalleProducto;
import com.codedoblea.tienda.model.others.BeanDetalleProducto;
import com.codedoblea.tienda.utilities.BeanCrud;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.SQLException;
import java.util.List;

import java.util.logging.Logger;

/**
 *
 * @author andres
 */
public class ColorDetalleProductoDAOImpl implements IColorDetalleProductoDAO {

    private static final Logger LOG = Logger.getLogger(ColorDetalleProductoDAOImpl.class.getName());

    public ColorDetalleProductoDAOImpl() {

    }

    @Override
    public Boolean add(List<BeanDetalleProducto> list, Connection con) throws SQLException {
        Boolean valor = false;
        PreparedStatement pst;
        con.setAutoCommit(false);
        StringBuilder sSQL = new StringBuilder();
        try {
            sSQL.append("INSERT INTO ");
            sSQL.append("`detalle_producto_color`(IDCOLOR,");
            sSQL.append("CANTIDAD,IDDETALLE_PRODUCTO) ");
            sSQL.append("VALUES(?,?,?)");
            pst = con.prepareStatement(sSQL.toString());
            for (BeanDetalleProducto detalleProducto : list) {
                for (ColorDetalleProducto colorDetalleProducto : detalleProducto.getList()) {
                    pst.setLong(1, colorDetalleProducto.getIdcolor().getIdcolor());
                    pst.setInt(2, colorDetalleProducto.getCantidad());
                    pst.setLong(3, detalleProducto.getDetalle_producto().getIddetalle_producto());
                    pst.executeUpdate();
                }
            }
            pst.close();
            valor = true;
        } catch (SQLException ex) {
            throw ex;
        }
        return valor;
    }

}
