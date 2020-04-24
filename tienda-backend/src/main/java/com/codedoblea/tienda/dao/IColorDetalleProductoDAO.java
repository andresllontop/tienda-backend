/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.dao;

import com.codedoblea.tienda.model.others.BeanDetalleProducto;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


/**
 *
 * @author andres
 */
public interface IColorDetalleProductoDAO {
  public Boolean add(List<BeanDetalleProducto> list, Connection con) throws SQLException;

}
