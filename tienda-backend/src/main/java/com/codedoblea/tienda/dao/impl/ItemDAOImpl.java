/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.dao.impl;

import com.codedoblea.tienda.dao.IItemDAO;
import com.codedoblea.tienda.dao.SQLCloseable;
import com.codedoblea.tienda.model.Item;
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
public class ItemDAOImpl implements IItemDAO {

    private static final Logger LOG = Logger.getLogger(ItemDAOImpl.class.getName());
    private final DataSource pool;
    private BeanCrud beancrud;

    public ItemDAOImpl(DataSource pool) {
        this.pool = pool;
    }

    @Override
    public BeanPagination getPagination(HashMap<String, Object> parameters, Connection conn)
            throws SQLException {
        BeanPagination beanpagination = new BeanPagination();
        List<Item> list = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs;
        try {
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("SELECT COUNT(IDITEM) AS COUNT FROM ");
            sbSQL.append("`item` WHERE ");
            sbSQL.append("LOWER(NOMBRE) LIKE CONCAT('%',?,'%') ");
            sbSQL.append(String.valueOf(parameters.get("SQL_FILTER")));
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
                    sbSQL.append("`item`  WHERE ");
                    sbSQL.append("LOWER(NOMBRE) LIKE CONCAT('%',?,'%') ");
                    sbSQL.append(String.valueOf(parameters.get("SQL_FILTER")));
                    sbSQL.append(String.valueOf(parameters.get("SQL_ORDERS")));
                    sbSQL.append(parameters.get("SQL_PAGINATION"));
                    pst = conn.prepareStatement(sbSQL.toString());
                    pst.setString(1, String.valueOf(parameters.get("FILTER")));
                    LOG.info(pst.toString());
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        Item item = new Item();
                        item.setIditem(rs.getLong("IDITEM"));
                        item.setNombre(rs.getString("NOMBRE"));
                        item.setIndice(rs.getShort("INDICE"));
                        list.add(item);
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
    public BeanCrud add(Item t, HashMap<String, Object> parameters) throws SQLException {
        beancrud = new BeanCrud();
        PreparedStatement pst;
        ResultSet rs;
        try ( Connection conn = this.pool.getConnection();  SQLCloseable finish
                = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(IDITEM) AS COUNT, MAX(IDITEM) AS MAXIMO FROM ");
            sSQL.append("`item`  WHERE NOMBRE = ? ");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setString(1, t.getNombre());
            LOG.info(pst.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("INSERT INTO ");
                    sSQL.append("`item` (NOMBRE,INDICE) VALUES(?,?)");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setString(1, t.getNombre());
                    pst.setShort(2, t.getIndice());
                    LOG.info(pst.toString());
                    pst.executeUpdate();
                    conn.commit();
                    beancrud.setMessageServer("ok");
//                    
                    sSQL.setLength(0);
                    sSQL.append("SELECT MAX(IDITEM) AS MAXIMO FROM ");
                    sSQL.append("`item`");
                    pst = conn.prepareStatement(sSQL.toString());
                    LOG.info(pst.toString());
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        t.setIditem(rs.getLong("MAXIMO"));
                    }
                    beancrud.setClassGeneric(t);
//                    
                    switch (t.getIndice()) {
                        case 1:
                            parameters.put("SQL_FILTER", "AND INDICE = 1 ");

                            break;
                        default:
                            parameters.put("SQL_FILTER", "AND INDICE = 2 ");
                            break;
                    }
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
    public BeanCrud update(Item t, HashMap<String, Object> parameters) throws SQLException {
        beancrud = new BeanCrud();
        PreparedStatement pst;
        ResultSet rs;
        try ( Connection conn = this.pool.getConnection();  SQLCloseable finish = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(IDITEM) AS COUNT FROM ");
            sSQL.append("`item` WHERE NOMBRE = ? AND IDITEM != ? ");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setString(1, t.getNombre());
            pst.setLong(2, t.getIditem());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("UPDATE ");
                    sSQL.append("`item` SET NOMBRE = ?, INDICE = ? WHERE IDITEM = ?");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setString(1, t.getNombre());
                    pst.setShort(2, t.getIndice());
                    pst.setLong(3, t.getIditem());
                    LOG.info(pst.toString());
                    pst.executeUpdate();
                    conn.commit();
                    beancrud.setMessageServer("ok");
                    switch (t.getIndice()) {
                        case 1:
                            parameters.put("SQL_FILTER", "AND INDICE = 1 ");

                            break;
                        default:
                            parameters.put("SQL_FILTER", "AND INDICE = 2 ");
                            break;
                    }
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
                    sSQL.append("SELECT COUNT(IDITEM) AS COUNT, INDICE FROM ");
                    sSQL.append("`item` WHERE IDITEM = ? ");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setInt(1, id.intValue());
                    LOG.info(pst.toString());
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        if (rs.getInt("COUNT") >= 1) {
                            switch (rs.getShort("INDICE")) {
                                case 1:
                                    parameters.put("SQL_FILTER", "AND INDICE = 1 ");
                                    break;
                                default:
                                    parameters.put("SQL_FILTER", "AND INDICE = 2 ");
                                    break;
                            }
                            sSQL.setLength(0);
                            sSQL.append("DELETE FROM ");
                            sSQL.append("`item`  WHERE IDITEM = ?");
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
    public Item getForId(Long id) throws SQLException {
        Item item = new Item();
        PreparedStatement pst;
        ResultSet rs;
        try ( Connection conn = this.pool.getConnection();  SQLCloseable finish = conn::rollback;) {
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("SELECT COUNT(IDITEM) AS COUNT FROM ");
            sbSQL.append("`item` WHERE ");
            sbSQL.append("IDITEM = ? ");
            pst = conn.prepareStatement(sbSQL.toString());
            pst.setLong(1, id);
            LOG.info(pst.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                item.setIditem(rs.getLong("IDITEM"));
                item.setNombre(rs.getString("NOMBRE"));
            }
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return item;
    }

}
