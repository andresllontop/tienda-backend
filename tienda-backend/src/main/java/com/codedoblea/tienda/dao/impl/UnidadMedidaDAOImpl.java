/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.dao.impl;

import com.codedoblea.tienda.dao.IUnidadMedidaDAO;
import com.codedoblea.tienda.dao.SQLCloseable;
import com.codedoblea.tienda.model.UnidadMedida;
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
public class UnidadMedidaDAOImpl implements IUnidadMedidaDAO {

    private static final Logger LOG = Logger.getLogger(UnidadMedidaDAOImpl.class.getName());
    private final DataSource pool;
    private BeanCrud beancrud;

    public UnidadMedidaDAOImpl(DataSource pool) {
        this.pool = pool;
    }

    @Override
    public BeanPagination getPagination(HashMap<String, Object> parameters, Connection conn)
            throws SQLException {
        BeanPagination beanpagination = new BeanPagination();
        List<UnidadMedida> list = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs;
        try {
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("SELECT COUNT(IDUNIDAD_MEDIDA) AS COUNT FROM ");
            sbSQL.append("`unidad_medida` WHERE ");
            sbSQL.append("LOWER(NOMBRE) LIKE CONCAT('%',?,'%')");
            pst = conn.prepareStatement(sbSQL.toString());
            pst.setString(1, String.valueOf(parameters.get("FILTER")));
            LOG.info(pst.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                beanpagination.setCount_filter(rs.getLong("COUNT"));
                if (rs.getInt("COUNT") > 0) {
                    sbSQL.setLength(0);
                    sbSQL.append("SELECT * FROM ");
                    sbSQL.append("`unidad_medida`  WHERE ");
                    sbSQL.append("LOWER(NOMBRE) LIKE CONCAT('%',?,'%')");
                    sbSQL.append(String.valueOf(parameters.get("SQL_ORDERS")));
                    sbSQL.append(parameters.get("SQL_PAGINATION"));
                    pst = conn.prepareStatement(sbSQL.toString());
                    pst.setString(1, String.valueOf(parameters.get("FILTER")));
                    LOG.info(pst.toString());
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        UnidadMedida unidadMedida = new UnidadMedida();
                        unidadMedida.setIdunidad_medida(rs.getLong("IDUNIDAD_MEDIDA"));
                        unidadMedida.setNombre(rs.getString("NOMBRE"));
                        unidadMedida.setAbreviatura(rs.getString("ABREVIATURA"));
                        list.add(unidadMedida);
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
    public BeanCrud add(UnidadMedida t, HashMap<String, Object> parameters) throws SQLException {
        beancrud = new BeanCrud();
        PreparedStatement pst;
        ResultSet rs;
        try (Connection conn = this.pool.getConnection();
                SQLCloseable finish
                = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(IDUNIDAD_MEDIDA) AS COUNT FROM ");
            sSQL.append("`unidad_medida`  WHERE NOMBRE = ?");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setString(1, t.getNombre());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("INSERT INTO ");
                    sSQL.append("`unidad_medida` (NOMBRE,");
                    sSQL.append("ABREVIATURA) ");
                    sSQL.append("VALUES(?,?) ");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setString(1, t.getNombre());
                    pst.setString(2, t.getAbreviatura());
                    LOG.info(pst.toString());
                    pst.executeUpdate();
                    conn.commit();
                    beancrud.setMessageServer("ok");
                    beancrud.setBeanPagination(getPagination(parameters, conn));
                } else {
                    beancrud.setMessageServer("No se registró, ya existe un UnidadMedida con el Nombre ingresado");
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
    public BeanCrud update(UnidadMedida t, HashMap<String, Object> parameters) throws SQLException {
        beancrud = new BeanCrud();
        PreparedStatement pst;
        ResultSet rs;
        try (Connection conn = this.pool.getConnection(); SQLCloseable finish = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(IDUNIDAD_MEDIDA) AS COUNT FROM ");
            sSQL.append("`unidad_medida` WHERE NOMBRE = ? AND ABREVIATURA = ? ");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setString(1, t.getNombre());
            pst.setString(2, t.getAbreviatura());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("UPDATE ");
                    sSQL.append("`unidad_medida` SET NOMBRE = ?,");
                    sSQL.append(" ABREVIATURA = ? WHERE IDUNIDAD_MEDIDA = ?");
                    pst = conn.prepareStatement(sSQL.toString());
                     pst.setString(1, t.getNombre());
                    pst.setString(2, t.getAbreviatura());
                    pst.setLong(3, t.getIdunidad_medida());
                    LOG.info(pst.toString());
                    pst.executeUpdate();
                    conn.commit();
                    beancrud.setMessageServer("ok");
                    beancrud.setBeanPagination(getPagination(parameters, conn));
                } else {
                    beancrud.setMessageServer("No se modificó, ya existe un Unidad de Medida con los datos ingresados");
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
            sSQL.append("SELECT COUNT(IDDETALLE_PRODUCTO) AS COUNT FROM ");
            sSQL.append("`detalle_producto` WHERE IDUNIDAD_MEDIDA = ?");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setInt(1, id.intValue());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("DELETE FROM ");
                    sSQL.append("`unidad_medida` WHERE IDUNIDAD_MEDIDA = ?");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setInt(1, id.intValue());
                    LOG.info(pst.toString());
                    pst.executeUpdate();
                    conn.commit();
                    beancrud.setMessageServer("ok");
                    beancrud.setBeanPagination(getPagination(parameters, conn));
                } else {
                    beancrud.setMessageServer("No se eliminó, existe un Detalle del Producto asociado a este Unidad de Medida");
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
    public UnidadMedida getForId(Long id) throws SQLException {
        UnidadMedida unidadMedida = new UnidadMedida();
        PreparedStatement pst;
        ResultSet rs;
        try (Connection conn = this.pool.getConnection();
                SQLCloseable finish = conn::rollback;) {
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("SELECT COUNT(IDUNIDAD_MEDIDA) AS COUNT FROM ");
            sbSQL.append("`unidad_medida` WHERE ");
            sbSQL.append("IDUNIDAD_MEDIDA = ? ");
            pst = conn.prepareStatement(sbSQL.toString());
            pst.setLong(1, id);
            LOG.info(pst.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                unidadMedida.setIdunidad_medida(rs.getLong("IDUNIDAD_MEDIDA"));
                unidadMedida.setNombre(rs.getString("NOMBRE"));
            }
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return unidadMedida;
    }

}

