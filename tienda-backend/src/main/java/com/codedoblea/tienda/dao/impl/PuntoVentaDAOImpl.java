/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.dao.impl;

import com.codedoblea.tienda.dao.IPuntoVentaDAO;
import com.codedoblea.tienda.dao.SQLCloseable;
import com.codedoblea.tienda.model.PuntoVenta;
import com.codedoblea.tienda.utilities.BeanCrud;
import com.codedoblea.tienda.utilities.BeanPagination;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author andres
 */
public class PuntoVentaDAOImpl implements IPuntoVentaDAO {

    private static final Logger LOG = Logger.getLogger(PuntoVentaDAOImpl.class.getName());
    private final DataSource pool;
    private BeanCrud beancrud;

    public PuntoVentaDAOImpl(DataSource pool) {
        this.pool = pool;
    }

    @Override
    public BeanPagination getPagination(HashMap<String, Object> parameters, Connection conn)
            throws SQLException {
        BeanPagination beanpagination = new BeanPagination();
        List<PuntoVenta> list = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs;
        try {
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("SELECT COUNT(IDPUNTO_VENTA) AS COUNT FROM ");
            sbSQL.append("`punto_venta` WHERE ");
            sbSQL.append("LOWER(NOMBRE) LIKE CONCAT('%',?,'%') ");
            sbSQL.append(parameters.get("SQL_PAGINATION"));
            pst = conn.prepareStatement(sbSQL.toString());
            pst.setString(1, String.valueOf(parameters.get("FILTER")));
            LOG.info(pst.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                beanpagination.setCount_filter(rs.getLong("COUNT"));
                if (rs.getInt("COUNT") > 0) {
                    sbSQL.setLength(0);
                    sbSQL.append("SELECT * FROM ");
                    sbSQL.append("`punto_venta`  WHERE ");
                    sbSQL.append("LOWER(NOMBRE) LIKE CONCAT('%',?,'%') ");
                    sbSQL.append(String.valueOf(parameters.get("SQL_ORDERS")));
                    sbSQL.append(parameters.get("SQL_PAGINATION"));
                    pst = conn.prepareStatement(sbSQL.toString());
                    pst.setString(1, String.valueOf(parameters.get("FILTER")));
                    LOG.info(pst.toString());
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        PuntoVenta puntoVenta = new PuntoVenta();
                        puntoVenta.setIdpunto_venta(rs.getLong("IDPUNTO_VENTA"));
                        puntoVenta.setNombre(rs.getString("NOMBRE"));
                        list.add(puntoVenta);
                    }
                }
            }
            beanpagination.setList(list);
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return beanpagination;
    }

    @Override
    public BeanCrud getPagination(HashMap<String, Object> parameters) throws SQLException {
        beancrud = new BeanCrud();
        try ( Connection conn = pool.getConnection()) {
            beancrud.setBeanPagination(getPagination(parameters, conn));
        } catch (SQLException e) {
            throw e;
        }
        return beancrud;
    }

    @Override
    public BeanCrud add(PuntoVenta t, HashMap<String, Object> parameters) throws SQLException {
        beancrud = new BeanCrud();
        PreparedStatement pst;
        ResultSet rs;
        try ( Connection conn = this.pool.getConnection();  SQLCloseable finish
                = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(IDPUNTO_VENTA) AS COUNT FROM ");
            sSQL.append("`punto_venta`  WHERE NOMBRE = ? ");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setString(1, t.getNombre());
            LOG.info(pst.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("INSERT INTO ");
                    sSQL.append("`punto_venta` (NOMBRE) VALUES(?)");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setString(1, t.getNombre());
                    LOG.info(pst.toString());
                    pst.executeUpdate();
                    conn.commit();
                    beancrud.setMessageServer("ok");                                
                    beancrud.setBeanPagination(getPagination(parameters, conn));
                } else {
                    beancrud.setMessageServer("No se registró, ya existe un elemento con el nombre ingresado");
                }
            }
            pst.close();
            rs.close();
        } catch (SQLException e) {
            throw e;
        }
        return beancrud;
    }

    @Override
    public BeanCrud update(PuntoVenta t, HashMap<String, Object> parameters) throws SQLException {
        beancrud = new BeanCrud();
        PreparedStatement pst;
        ResultSet rs;
        try ( Connection conn = this.pool.getConnection();  SQLCloseable finish = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(IDPUNTO_VENTA) AS COUNT FROM ");
            sSQL.append("`punto_venta` WHERE NOMBRE = ? AND IDPUNTO_VENTA != ? ");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setString(1, t.getNombre());
            pst.setLong(2, t.getIdpunto_venta());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("UPDATE ");
                    sSQL.append("`punto_venta` SET NOMBRE = ? WHERE IDPUNTO_VENTA = ?");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setString(1, t.getNombre());
                    pst.setLong(2, t.getIdpunto_venta());
                    LOG.info(pst.toString());
                    pst.executeUpdate();
                    conn.commit();
                    beancrud.setMessageServer("ok");
                    beancrud.setBeanPagination(getPagination(parameters, conn));
                } else {
                    beancrud.setMessageServer("No se modificó, ya existe un elemento con el nombre ingresado");
                }
            }
            pst.close();
            rs.close();
        } catch (SQLException e) {
            throw e;
        }
        return beancrud;
    }

    @Override
    public BeanCrud delete(Long id, HashMap<String, Object> parameters) throws SQLException {
        beancrud = new BeanCrud();
        PreparedStatement pst;
        ResultSet rs;
        try ( Connection conn = this.pool.getConnection();  SQLCloseable finish = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(IDPUNTO_VENTA_SERIE) AS COUNT ");
            sSQL.append("FROM `punto_venta_serie` ");
            sSQL.append("WHERE IDPUNTO_VENTA_SERIE = ? ");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setInt(1, id.intValue());
            pst.setInt(2, id.intValue());
            LOG.info(pst.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("SELECT COUNT(IDPUNTO_USUARIO) AS COUNT FROM ");
                    sSQL.append("`punto_usuario` WHERE IDPUNTO_USUARIO = ? ");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setInt(1, id.intValue());
                    LOG.info(pst.toString());
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        if (rs.getInt("COUNT") == 1) {
                            sSQL.setLength(0);
                            sSQL.append("DELETE FROM ");
                            sSQL.append("`punto_venta`  WHERE IDPUNTO_VENTA = ?");
                            pst = conn.prepareStatement(sSQL.toString());
                            pst.setInt(1, id.intValue());
                            LOG.info(pst.toString());
                            pst.executeUpdate();
                            conn.commit();
                            beancrud.setMessageServer("ok");

                            beancrud.setBeanPagination(getPagination(parameters, conn));
                        } else {
                            beancrud.setMessageServer("No se eliminó, No existe el elemento");
                        }
                    }

                } else {
                    beancrud.setMessageServer("No se eliminó, existe un Producto asociado");
                }
            }
            pst.close();
            rs.close();
        } catch (SQLException e) {
            throw e;
        }
        return beancrud;
    }

    @Override
    public PuntoVenta getForId(Long id) throws SQLException {
        PuntoVenta puntoVenta = new PuntoVenta();
        PreparedStatement pst;
        ResultSet rs;
        try ( Connection conn = this.pool.getConnection();  SQLCloseable finish = conn::rollback;) {
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("SELECT COUNT(IDPUNTO_VENTA) AS COUNT FROM ");
            sbSQL.append("`punto_venta` WHERE ");
            sbSQL.append("IDPUNTO_VENTA = ? ");
            pst = conn.prepareStatement(sbSQL.toString());
            pst.setLong(1, id);
            LOG.info(pst.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                puntoVenta.setIdpunto_venta(rs.getLong("IDPUNTO_VENTA"));
                puntoVenta.setNombre(rs.getString("NOMBRE"));
            }
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return puntoVenta;
    }

}
