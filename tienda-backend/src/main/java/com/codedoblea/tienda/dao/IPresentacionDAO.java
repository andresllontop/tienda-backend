/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.dao;

import com.codedoblea.tienda.model.DetalleEntrada;
import com.codedoblea.tienda.model.DetalleSalida;
import com.codedoblea.tienda.model.Presentacion;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author andres
 */
public interface IPresentacionDAO extends CRUD<Presentacion> {
    public Long addBeanPresentacionEntrada(List<DetalleEntrada> list, 
            Connection con) throws SQLException;
    public Long addBeanPresentacionSalida(List<DetalleSalida> list, 
            Connection con) throws SQLException;

}
