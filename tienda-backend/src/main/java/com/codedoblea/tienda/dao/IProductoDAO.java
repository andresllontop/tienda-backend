/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.dao;

import com.codedoblea.tienda.model.Producto;
import com.codedoblea.tienda.utilities.BeanCrud;
import java.sql.SQLException;

/**
 *
 * @author andres
 */
public interface IProductoDAO extends CRUD<Producto> {
    public BeanCrud getForCodigo(String codigo) throws SQLException;
}
