/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.dao.impl;

import com.codedoblea.tienda.dao.IColorDAO;
import com.codedoblea.tienda.dao.SQLCloseable;
import com.codedoblea.tienda.model.Color;
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
public class ColorDAOImpl implements IColorDAO {

    private static final Logger LOG = Logger.getLogger(ColorDAOImpl.class.getName());
    private final DataSource pool;
    private BeanCrud beancrud;

    public ColorDAOImpl(DataSource pool) {
        this.pool = pool;
    }

    @Override
    public BeanPagination getPagination(HashMap<String, Object> parameters, Connection conn)
            throws SQLException {
        BeanPagination beanpagination = new BeanPagination();
        List<Color> list = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs;
        try {
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("SELECT COUNT(IDCOLOR) AS COUNT FROM ");
            sbSQL.append("`color` WHERE ");
            sbSQL.append("LOWER(CODIGO) LIKE CONCAT('%',?,'%')");
            pst = conn.prepareStatement(sbSQL.toString());
            pst.setString(1, String.valueOf(parameters.get("FILTER")));
            LOG.info(pst.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                beanpagination.setCount_filter(rs.getLong("COUNT"));
                if (rs.getInt("COUNT") > 0) {
                    sbSQL.setLength(0);
                    sbSQL.append("SELECT * FROM ");
                    sbSQL.append("`color`  WHERE ");
                    sbSQL.append("LOWER(CODIGO) LIKE CONCAT('%',?,'%')");
                    sbSQL.append(String.valueOf(parameters.get("SQL_ORDERS")));
                    pst = conn.prepareStatement(sbSQL.toString());
                    pst.setString(1, String.valueOf(parameters.get("FILTER")));
                    LOG.info(pst.toString());
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        Color color = new Color();
                        color.setIdcolor(rs.getLong("IDCOLOR"));
                        color.setNombre(rs.getString("NOMBRE"));
                        color.setCodigo(rs.getString("CODIGO"));
                        list.add(color);
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
        try (Connection conn = pool.getConnection()) {
            beancrud.setBeanPagination(getPagination(parameters, conn));
        } catch (SQLException e) {
            throw e;
        }
        return beancrud;
    }

    @Override
    public BeanCrud add(Color t, HashMap<String, Object> parameters) throws SQLException {
        beancrud = new BeanCrud();
        PreparedStatement pst;
        ResultSet rs;
        try (Connection conn = this.pool.getConnection();
                SQLCloseable finish
                = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(IDPERSONAL) AS COUNT FROM ");
            sSQL.append("`color`  WHERE NOMBRE = ?");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setString(1, t.getNombre());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("INSERT INTO ");
                    sSQL.append("`color` (NOMBRE,");
                    sSQL.append("CODIGO) ");
                    sSQL.append("VALUES(?,?) ");
                    pst = conn.prepareStatement(sSQL.toString());
                     pst.setString(1, t.getNombre());
                    pst.setString(2, t.getCodigo());
                    LOG.info(pst.toString());
                    pst.executeUpdate();
                    conn.commit();
                    beancrud.setMessageServer("ok");

                    beancrud.setBeanPagination(getPagination(parameters, conn));
                } else {
                    beancrud.setMessageServer("No se registró, ya existe un Color con el DNI ingresado");
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
    public BeanCrud update(Color t, HashMap<String, Object> parameters) throws SQLException {
        beancrud = new BeanCrud();
        PreparedStatement pst;
        ResultSet rs;
        try (Connection conn = this.pool.getConnection(); SQLCloseable finish = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(IDPERSONAL) AS COUNT FROM ");
            sSQL.append("`color` WHERE CODIGO = ? AND IDPERSONAL != ?");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setString(1, t.getCodigo());
            pst.setLong(2, t.getIdcolor());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("UPDATE ");
                    sSQL.append("`color` SET NOMBRE = ?, APELLIDOS = ?,");
                    sSQL.append(" TIPO_DOCUMENTO = ?,DOCUMENTO = ?,");
                    sSQL.append(" TELEFONO = ?,EMAIL = ?,");
                    sSQL.append(" DIRECCION = ? WHERE IDPERSONAL = ?");
                    pst = conn.prepareStatement(sSQL.toString());
                     pst.setString(1, t.getNombre());
                    pst.setLong(8, t.getIdcolor());
                    LOG.info(pst.toString());
                    pst.executeUpdate();
                    conn.commit();
                    beancrud.setMessageServer("ok");
                    beancrud.setBeanPagination(getPagination(parameters, conn));
                } else {
                    beancrud.setMessageServer("No se modificó, ya existe un Color con el nombre ingresado");
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
        try (Connection conn = this.pool.getConnection(); SQLCloseable finish = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(IDENTRADA) AS COUNT FROM ");
            sSQL.append("`entrada`  WHERE IDPERSONAL = ?");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setInt(1, id.intValue());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("DELETE FROM ");
                    sSQL.append("`color` WHERE IDPERSONAL = ?");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setInt(1, id.intValue());
                    LOG.info(pst.toString());
                    pst.executeUpdate();
                    conn.commit();
                    beancrud.setMessageServer("ok");
                    beancrud.setBeanPagination(getPagination(parameters, conn));
                } else {
                    beancrud.setMessageServer("No se eliminó, existe una Entrada asociado a este Color");
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
    public Color getForId(Long id) throws SQLException {
        Color color = new Color();
        PreparedStatement pst;
        ResultSet rs;
        try (Connection conn = this.pool.getConnection();
                SQLCloseable finish = conn::rollback;) {
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("SELECT COUNT(IDPERSONAL) AS COUNT FROM ");
            sbSQL.append("`color` WHERE ");
            sbSQL.append("IDPERSONAL = ? ");
            pst = conn.prepareStatement(sbSQL.toString());
            pst.setLong(1, id);
            LOG.info(pst.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                color.setIdcolor(rs.getLong("IDPERSONAL"));
                color.setNombre(rs.getString("NOMBRE"));
            }
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return color;
    }

}
