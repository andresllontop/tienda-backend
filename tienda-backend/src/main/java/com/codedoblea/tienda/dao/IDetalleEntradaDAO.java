/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.dao;

import com.codedoblea.tienda.model.DetalleEntrada;
import com.codedoblea.tienda.utilities.BeanPagination;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author andres
 */
public interface IDetalleEntradaDAO {

    public BeanPagination getPagination(HashMap<String, Object> parameters, Connection conn) throws SQLException;

    public Boolean addBeanDetalleEntrada(List<DetalleEntrada> list, Long ID,Long IDPre, Connection con) throws SQLException;

    public Boolean updateBeanDetalleEntrada(List<DetalleEntrada> list, Connection con) throws SQLException;

}
