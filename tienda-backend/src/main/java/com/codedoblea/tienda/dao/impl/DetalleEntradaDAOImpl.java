/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.dao.impl;

import com.codedoblea.tienda.dao.IDetalleEntradaDAO;
import com.codedoblea.tienda.model.DetalleEntrada;
import com.codedoblea.tienda.utilities.BeanPagination;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author andres
 */
public class DetalleEntradaDAOImpl implements IDetalleEntradaDAO {

    private static final Logger LOG = Logger.getLogger(ProductoDAOImpl.class.getName());

    public DetalleEntradaDAOImpl() {

    }

    @Override
    public BeanPagination getPagination(HashMap<String, Object> parameters, Connection conn) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Boolean addBeanDetalleEntrada(List<DetalleEntrada> list, Long ID,Long IDPre, Connection con) throws SQLException {
        Boolean valor = false;
        Integer valorContador=Integer.parseInt(IDPre.toString())-list.size()+1;
        PreparedStatement pst;
        try {
            con.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("INSERT INTO ");
            sSQL.append("`detalle_entrada`(CANTIDAD,");
            sSQL.append("IDPRESENTACION,IDENTRADA,PRECIO) ");
            sSQL.append("VALUES(?,?,?,?)");
            pst = con.prepareStatement(sSQL.toString());
            for (DetalleEntrada detalleentrada : list) {
                pst.setInt(1, detalleentrada.getCantidad());
                pst.setLong(2, valorContador++);
                pst.setLong(3, ID);
                pst.setDouble(4, detalleentrada.getPrecio());
                LOG.info(pst.toString());
                pst.executeUpdate();
            }
            valor = true;
        } catch (SQLException ex) {
            throw ex;
        }
        return valor;
    }

    @Override
    public Boolean updateBeanDetalleEntrada(List<DetalleEntrada> list, Connection con) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
