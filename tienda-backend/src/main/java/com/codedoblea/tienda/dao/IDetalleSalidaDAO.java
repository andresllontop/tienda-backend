/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.dao;

import com.codedoblea.tienda.model.DetalleSalida;
import com.codedoblea.tienda.utilities.BeanPagination;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author andres
 */
public interface IDetalleSalidaDAO {

    public BeanPagination getPagination(HashMap<String, Object> parameters, Connection conn) throws SQLException;

    public Boolean addBeanDetalleSalida(List<DetalleSalida> list, Long ID,Long IDPre, Connection con) throws SQLException;

    public Boolean updateBeanDetalleSalida(List<DetalleSalida> list, Connection con) throws SQLException;

}
