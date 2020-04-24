/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.dao.impl;

import com.codedoblea.tienda.dao.ISerieDAO;
import com.codedoblea.tienda.dao.SQLCloseable;
import com.codedoblea.tienda.model.Serie;
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
public class SerieDAOImpl implements ISerieDAO {

    private static final Logger LOG = Logger.getLogger(SerieDAOImpl.class.getName());
    private final DataSource pool;
    private BeanCrud beancrud;

    public SerieDAOImpl(DataSource pool) {
        this.pool = pool;
    }

    @Override
    public BeanPagination getPagination(HashMap<String, Object> parameters, Connection conn)
            throws SQLException {
        BeanPagination beanpagination = new BeanPagination();
        List<Serie> list = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs;
        try {
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("SELECT COUNT(IDSERIE) AS COUNT FROM ");
            sbSQL.append("`serie` WHERE ");
            sbSQL.append("LOWER(TIPO_DOCUMENTO) LIKE CONCAT('%',?,'%') ");
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
                    sbSQL.append("`serie`  WHERE ");
                    sbSQL.append("LOWER(TIPO_DOCUMENTO) LIKE CONCAT('%',?,'%') ");
                    sbSQL.append(String.valueOf(parameters.get("SQL_ORDERS")));
                    sbSQL.append(parameters.get("SQL_PAGINATION"));
                    pst = conn.prepareStatement(sbSQL.toString());
                    pst.setString(1, String.valueOf(parameters.get("FILTER")));
                    LOG.info(pst.toString());
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        Serie serie = new Serie();
                        serie.setIdserie(rs.getLong("IDSERIE"));
                        serie.setTipo_documento(rs.getShort("TIPO_DOCUMENTO"));
                        serie.setNumero_serie(rs.getString("NUMERO_SERIE"));
                        serie.setNumero_inicial(rs.getString("NUMERO_INICIAL"));
                        serie.setNumero_final(rs.getString("NUMERO_FINAL"));
                        serie.setNumero_actual(rs.getString("NUMERO_ACTUAL"));

                        list.add(serie);
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
    public BeanCrud add(Serie t, HashMap<String, Object> parameters) throws SQLException {
        beancrud = new BeanCrud();
        PreparedStatement pst;
        ResultSet rs;
        try ( Connection conn = this.pool.getConnection();  SQLCloseable finish
                = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(IDSERIE) AS COUNT FROM ");
            sSQL.append("`serie`  WHERE NUMERO_SERIE = ? ");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setString(1, t.getNumero_serie());
            LOG.info(pst.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("INSERT INTO `serie` ");
                    sSQL.append("(TIPO_DOCUMENTO,NUMERO_SERIE,NUMERO_INICIAL,NUMERO_FINAL,NUMERO_ACTUAL) ");
                    sSQL.append(" VALUES(?,?,?,?,?)");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setShort(1, t.getTipo_documento());
                    pst.setString(2, t.getNumero_serie());
                    pst.setString(3, t.getNumero_inicial());
                    pst.setString(4, t.getNumero_final());
                    pst.setString(5, t.getNumero_actual());
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
    public BeanCrud update(Serie t, HashMap<String, Object> parameters) throws SQLException {
        beancrud = new BeanCrud();
        PreparedStatement pst;
        ResultSet rs;
        try ( Connection conn = this.pool.getConnection();  SQLCloseable finish = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(IDSERIE) AS COUNT FROM ");
            sSQL.append("`serie` WHERE TIPO_DOCUMENTO = ? AND IDSERIE != ? ");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setShort(1, t.getTipo_documento());
            pst.setLong(2, t.getIdserie());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("UPDATE ");
                    sSQL.append("`serie` SET TIPO_DOCUMENTO = ?,NUMERO_SERIE = ?,");
                    sSQL.append("NUMERO_INICIAL = ?,NUMERO_FINAL = ?,NUMERO_ACTUAL = ? ");
                    sSQL.append(" WHERE IDSERIE = ?");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setShort(1, t.getTipo_documento());
                    pst.setString(2, t.getNumero_serie());
                    pst.setString(3, t.getNumero_inicial());
                    pst.setString(4, t.getNumero_final());
                    pst.setString(5, t.getNumero_actual());
                    pst.setLong(6, t.getIdserie());
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
            sSQL.append("SELECT COUNT(PRO.IDPRODUCTO) AS COUNT ");
            sSQL.append("FROM `producto` as PRO ");
            sSQL.append("WHERE PRO.IDCATEGORIA = ? OR PRO.IDMARCA = ? ");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setInt(1, id.intValue());
            pst.setInt(2, id.intValue());
            LOG.info(pst.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("DELETE FROM ");
                    sSQL.append("`serie`  WHERE IDSERIE = ?");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setInt(1, id.intValue());
                    LOG.info(pst.toString());
                    pst.executeUpdate();
                    conn.commit();
                    beancrud.setMessageServer("ok");
                    beancrud.setBeanPagination(getPagination(parameters, conn));
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
    public Serie getForId(Long id) throws SQLException {
        Serie serie = new Serie();
        PreparedStatement pst;
        ResultSet rs;
        try ( Connection conn = this.pool.getConnection();  SQLCloseable finish = conn::rollback;) {
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("SELECT COUNT(IDSERIE) AS COUNT FROM ");
            sbSQL.append("`serie` WHERE ");
            sbSQL.append("IDSERIE = ? ");
            pst = conn.prepareStatement(sbSQL.toString());
            pst.setLong(1, id);
            LOG.info(pst.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                serie.setIdserie(rs.getLong("IDSERIE"));
              
            }
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return serie;
    }

}
