/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.dao;

import com.codedoblea.tienda.utilities.BeanCrud;
import com.codedoblea.tienda.utilities.BeanPagination;
import com.codedoblea.tienda.model.others.BeanEntrada;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 *
 * @author andres
 */
public interface IEntradaDAO {
    public BeanCrud getForCodigo(String codigo) throws SQLException;
    public BeanPagination getPagination(HashMap<String, Object> parameters, Connection conn) throws SQLException;
    public BeanCrud getPagination(HashMap<String, Object> parameters) throws SQLException;
    public BeanCrud delete(Long id, HashMap<String, Object> parameters) throws SQLException;
    public BeanCrud updateBeanEntrada(BeanEntrada beanentrada) throws SQLException;
    public BeanCrud addBeanEntrada(BeanEntrada beanentrada) throws SQLException;
}
