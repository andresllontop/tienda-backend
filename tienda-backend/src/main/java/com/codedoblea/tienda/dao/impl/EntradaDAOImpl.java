/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.dao.impl;

import com.codedoblea.tienda.dao.IDetalleEntradaDAO;
import com.codedoblea.tienda.dao.impl.DetalleEntradaDAOImpl;
import com.codedoblea.tienda.dao.IEntradaDAO;
import com.codedoblea.tienda.dao.IPresentacionDAO;
import com.codedoblea.tienda.model.others.BeanEntrada;
import com.codedoblea.tienda.dao.SQLCloseable;
import com.codedoblea.tienda.utilities.BeanCrud;
import com.codedoblea.tienda.utilities.BeanPagination;
import com.codedoblea.tienda.utilities.UtilDateApp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author andres
 */
public class EntradaDAOImpl implements IEntradaDAO {

    private static final Logger LOG = Logger.getLogger(ProductoDAOImpl.class.getName());
    private final DataSource pool;
    private BeanCrud beancrud;
    private IDetalleEntradaDAO detalleEntradaDAO;
    private IPresentacionDAO presentacionDAO;

    public EntradaDAOImpl(DataSource pool) {
        this.pool = pool;
    }

    @Override
    public BeanCrud getForCodigo(String codigo) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BeanPagination getPagination(HashMap<String, Object> parameters, Connection conn) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BeanCrud getPagination(HashMap<String, Object> parameters) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BeanCrud delete(Long id, HashMap<String, Object> parameters) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BeanCrud updateBeanEntrada(BeanEntrada beanentrada) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public BeanCrud addBeanEntrada(BeanEntrada beanentrada) throws SQLException {
        beancrud = new BeanCrud();
        PreparedStatement pst;
        ResultSet rs;
        try ( Connection conn = this.pool.getConnection();  SQLCloseable finish
                = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(IDENTRADA) AS COUNT,FECHA FROM ");
            sSQL.append("`entrada`  WHERE FECHA = ?");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setTimestamp(1, UtilDateApp.getTimestamp(beanentrada.getEntrada().getFecha()));
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("INSERT INTO ");
                    sSQL.append("`entrada` (FECHA,IDPERSONAL)");
                    sSQL.append(" VALUES(?,?)");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setTimestamp(1, UtilDateApp.getTimestamp(beanentrada.getEntrada().getFecha()));
                    pst.setLong(2, beanentrada.getEntrada().getPersonal().getIdpersona());
                    LOG.info(pst.toString());
                    pst.executeUpdate();
                    sSQL.setLength(0);
                    sSQL.append("SELECT MAX(IDENTRADA) AS MAXIMOENTRA FROM ");
                    sSQL.append("`entrada` ");
                    pst = conn.prepareStatement(sSQL.toString());
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        presentacionDAO = new PresentacionDAOImpl();
                        Long MaximoID=presentacionDAO.addBeanPresentacionEntrada(beanentrada.getList(), conn);
                        if ( MaximoID!= null) {
                            detalleEntradaDAO = new DetalleEntradaDAOImpl();
                            if (detalleEntradaDAO.addBeanDetalleEntrada(beanentrada.getList(),
                                    rs.getLong("MAXIMOENTRA"),MaximoID, conn)) {
                                conn.commit();
                                beancrud.setMessageServer("ok");
                            } else {
                                beancrud.setMessageServer("No se registró detalle entrada");
                            }

                        } else {
                            beancrud.setMessageServer("No se registró presentaciòn");
                        }

                    }
                } else {
                    beancrud.setMessageServer("No se registró, ya existe una Entrada con la fecha ingresada");
                }
            }
            pst.close();
            rs.close();
        } catch (SQLException e) {
            throw e;
        }
        return beancrud;
    }

}
