/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.dao.impl;

import com.codedoblea.tienda.dao.IPersonalDAO;
import com.codedoblea.tienda.dao.SQLCloseable;
import com.codedoblea.tienda.model.Personal;
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
public class PersonalDAOImpl implements IPersonalDAO {

    private static final Logger LOG = Logger.getLogger(PersonalDAOImpl.class.getName());
    private final DataSource pool;
    private BeanCrud beancrud;

    public PersonalDAOImpl(DataSource pool) {
        this.pool = pool;
    }

    @Override
    public BeanPagination getPagination(HashMap<String, Object> parameters, Connection conn)
            throws SQLException {
        BeanPagination beanpagination = new BeanPagination();
        List<Personal> list = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs;
        try {
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("SELECT COUNT(IDPERSONAL) AS COUNT FROM ");
            sbSQL.append("`personal` WHERE ");
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
                    sbSQL.append("`personal`  WHERE ");
                    sbSQL.append("LOWER(NOMBRE) LIKE CONCAT('%',?,'%')");
                    sbSQL.append(String.valueOf(parameters.get("SQL_ORDERS")));
                    sbSQL.append(parameters.get("SQL_PAGINATION"));
                    pst = conn.prepareStatement(sbSQL.toString());
                    pst.setString(1, String.valueOf(parameters.get("FILTER")));
                    LOG.info(pst.toString());
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        Personal personal = new Personal();
                        personal.setIdpersonal(rs.getLong("IDPERSONAL"));
                        personal.setNombre(rs.getString("NOMBRE"));
                        personal.setTipo_documento(rs.getShort("TIPO_DOCUMENTO"));
                        personal.setDocumento(rs.getInt("DOCUMENTO"));
                        personal.setTelefono(rs.getInt("TELEFONO"));
                        personal.setEmail(rs.getString("EMAIL"));
                        personal.setDireccion(rs.getString("DIRECCION"));
                        list.add(personal);
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
    public BeanCrud add(Personal t, HashMap<String, Object> parameters) throws SQLException {
        beancrud = new BeanCrud();
        PreparedStatement pst;
        ResultSet rs;
        try (Connection conn = this.pool.getConnection();
                SQLCloseable finish
                = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(IDPERSONAL) AS COUNT FROM ");
            sSQL.append("`personal`  WHERE NOMBRE = ?");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setString(1, t.getNombre());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("INSERT INTO ");
                    sSQL.append("`personal` (NOMBRE,");
                    sSQL.append("TIPO_DOCUMENTO,DOCUMENTO,");
                    sSQL.append("TELEFONO,EMAIL,DIRECCION) ");
                    sSQL.append("VALUES(?,?,?,?,?,?) ");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setString(1, t.getNombre());
                    pst.setShort(2, t.getTipo_documento());
                    pst.setInt(3, t.getDocumento());
                    pst.setInt(4, t.getTelefono());
                    pst.setString(5, t.getEmail());
                    pst.setString(6, t.getDireccion());
                    LOG.info(pst.toString());
                    pst.executeUpdate();
                    conn.commit();
                    beancrud.setMessageServer("ok");

                    beancrud.setBeanPagination(getPagination(parameters, conn));
                } else {
                    beancrud.setMessageServer("No se registró, ya existe un Personal con el DNI ingresado");
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
    public BeanCrud update(Personal t, HashMap<String, Object> parameters) throws SQLException {
        beancrud = new BeanCrud();
        PreparedStatement pst;
        ResultSet rs;
        try (Connection conn = this.pool.getConnection(); SQLCloseable finish = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(IDPERSONAL) AS COUNT FROM ");
            sSQL.append("`personal` WHERE NOMBRE = ? ");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setString(1, t.getNombre());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("UPDATE ");
                    sSQL.append("`personal` SET NOMBRE = ?,");
                    sSQL.append(" TIPO_DOCUMENTO = ?,DOCUMENTO = ?,");
                    sSQL.append(" TELEFONO = ?,EMAIL = ?,");
                    sSQL.append(" DIRECCION = ? WHERE IDPERSONAL = ?");
                    pst = conn.prepareStatement(sSQL.toString());
                     pst.setString(1, t.getNombre());
                    pst.setShort(2, t.getTipo_documento());
                    pst.setInt(3, t.getDocumento());
                    pst.setInt(4, t.getTelefono());
                    pst.setString(5, t.getEmail());
                    pst.setString(6, t.getDireccion());
                    pst.setLong(7, t.getIdpersonal());
                    LOG.info(pst.toString());
                    pst.executeUpdate();
                    conn.commit();
                    beancrud.setMessageServer("ok");
                    beancrud.setBeanPagination(getPagination(parameters, conn));
                } else {
                    beancrud.setMessageServer("No se modificó, ya existe un Personal con el nombre ingresado");
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
                    sSQL.append("`personal` WHERE IDPERSONAL = ?");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setInt(1, id.intValue());
                    LOG.info(pst.toString());
                    pst.executeUpdate();
                    conn.commit();
                    beancrud.setMessageServer("ok");
                    beancrud.setBeanPagination(getPagination(parameters, conn));
                } else {
                    beancrud.setMessageServer("No se eliminó, existe una Entrada asociado a este Personal");
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
    public Personal getForId(Long id) throws SQLException {
        Personal personal = new Personal();
        PreparedStatement pst;
        ResultSet rs;
        try (Connection conn = this.pool.getConnection();
                SQLCloseable finish = conn::rollback;) {
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("SELECT COUNT(IDPERSONAL) AS COUNT FROM ");
            sbSQL.append("`personal` WHERE ");
            sbSQL.append("IDPERSONAL = ? ");
            pst = conn.prepareStatement(sbSQL.toString());
            pst.setLong(1, id);
            LOG.info(pst.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                personal.setIdpersonal(rs.getLong("IDPERSONAL"));
                personal.setNombre(rs.getString("NOMBRE"));
            }
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return personal;
    }

}
