/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.dao.impl;

import com.codedoblea.tienda.dao.IPersonalDAO;
import com.codedoblea.tienda.dao.SQLCloseable;
import com.codedoblea.tienda.model.Personal;
import com.codedoblea.tienda.model.Usuario;
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
                        list.add(new Personal(new Usuario(rs.getLong("IDUSUARIO")),
                                rs.getShort("ESTADO"), rs.getLong("IDPERSONAL"),
                                rs.getString("NOMBRE"), rs.getString("APELLIDO_MAT"),
                                rs.getString("APELLIDO_PAT"), rs.getShort("TIPO_DOCUMENTO"),
                                rs.getShort("SEXO"), rs.getInt("DOCUMENTO"), rs.getInt("TELEFONO"),
                                rs.getString("EMAIL"), rs.getString("DIRECCION")));
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
    public BeanCrud add(Personal t, HashMap<String, Object> parameters) throws SQLException {
        beancrud = new BeanCrud();
        PreparedStatement pst;
        ResultSet rs;
        try ( Connection conn = this.pool.getConnection();  SQLCloseable finish
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
                    sSQL.append("`personal` (NOMBRE,APELLIDO_PAT,APELLIDO_MAT,");
                    sSQL.append("TIPO_DOCUMENTO,DOCUMENTO,SEXO,ESTADO,");
                    sSQL.append("TELEFONO,EMAIL,DIRECCION,IDCARGO,IDUSUARIO) ");
                    sSQL.append("VALUES(?,?,?,?,?,?,?,?,?,?,?,?) ");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setString(1, t.getNombre());
                    pst.setString(2, t.getApellido_pat());
                    pst.setString(3, t.getApellido_mat());
                    pst.setShort(4, t.getTipo_documento());
                    pst.setInt(5, t.getDocumento());
                    pst.setShort(6, t.getSexo());
                    pst.setShort(7, t.getEstado());
                    pst.setInt(8, t.getTelefono());
                    pst.setString(9, t.getEmail());
                    pst.setString(10, t.getDireccion());
                    pst.setLong(11, 1);
                    pst.setLong(12, t.getUsuario().getIdusuario());
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
        try ( Connection conn = this.pool.getConnection();  SQLCloseable finish = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(IDPERSONAL) AS COUNT FROM ");
            sSQL.append("`personal` WHERE DOCUMENTO = ? AND IDPERSONAL != ?");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setInt(1, t.getDocumento());
            pst.setLong(2, t.getIdpersona());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("UPDATE ");
                    sSQL.append("`personal` SET NOMBRE = ?, APELLIDO_PAT = ?,");
                    sSQL.append(" TIPO_DOCUMENTO = ?,DOCUMENTO = ?,");
                    sSQL.append(" TELEFONO = ?,EMAIL = ?,");
                    sSQL.append(" DIRECCION = ? WHERE IDPERSONAL = ?");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setString(1, t.getNombre());
                    pst.setString(2, t.getApellido_pat());
                    pst.setShort(3, t.getTipo_documento());
                    pst.setInt(4, t.getDocumento());
                    pst.setInt(5, t.getTelefono());
                    pst.setString(6, t.getEmail());
                    pst.setString(7, t.getDireccion());
                    pst.setLong(8, t.getIdpersona());
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
        try ( Connection conn = this.pool.getConnection();  SQLCloseable finish = conn::rollback;) {
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
        try ( Connection conn = this.pool.getConnection();  SQLCloseable finish = conn::rollback;) {
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("SELECT COUNT(IDPERSONAL) AS COUNT FROM ");
            sbSQL.append("`personal` WHERE ");
            sbSQL.append("IDPERSONAL = ? ");
            pst = conn.prepareStatement(sbSQL.toString());
            pst.setLong(1, id);
            LOG.info(pst.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                personal.setIdpersona(rs.getLong("IDPERSONAL"));
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
