/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.dao;

import com.codedoblea.tienda.model.others.BeanDetalleProducto;
import com.codedoblea.tienda.utilities.BeanPagination;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author andres
 */
public interface IDetalleProductoDAO {

    public BeanPagination getPagination(HashMap<String, Object> parameters, Connection conn) throws SQLException;

    public Boolean addBeanDetalleProducto(List<BeanDetalleProducto> list, Long ID, Connection con) throws SQLException;

    public Boolean updateBeanDetallePorducto(List<BeanDetalleProducto> list, Connection con) throws SQLException;

}
