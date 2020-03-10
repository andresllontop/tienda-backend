/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.dao.impl;

import com.codedoblea.tienda.dao.IProductoDAO;
import com.codedoblea.tienda.dao.SQLCloseable;
import com.codedoblea.tienda.model.Categoria;
import com.codedoblea.tienda.model.Producto;
import com.codedoblea.tienda.model.SubCategoria;
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
public class ProductoDAOImpl implements IProductoDAO {

    private static final Logger LOG = Logger.getLogger(ProductoDAOImpl.class.getName());
    private final DataSource pool;
    private BeanCrud beancrud;

    public ProductoDAOImpl(DataSource pool) {
        this.pool = pool;
    }

    @Override
    public BeanPagination getPagination(HashMap<String, Object> parameters, Connection conn)
            throws SQLException {
        BeanPagination beanpagination = new BeanPagination();
        List<Producto> list = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs;
        try {
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("SELECT COUNT(pro.IDPRODUCTO) AS COUNT FROM ");
            sbSQL.append("`producto` AS pro ");
            sbSQL.append("INNER JOIN `categoria` AS cat ON cat.IDCATEGORIA=pro.IDCATEGORIA ");
            sbSQL.append("INNER JOIN `subcategoria` AS subcat ON subcat.IDSUBCATEGORIA=pro.IDSUBCATEGORIA ");
            sbSQL.append("WHERE ");
            sbSQL.append(String.valueOf(parameters.get("SQL_FILTER")));
            sbSQL.append("LIKE CONCAT('%',?,'%') ");
            pst = conn.prepareStatement(sbSQL.toString());
            pst.setString(1, String.valueOf(parameters.get("FILTER")));
            LOG.info(pst.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                beanpagination.setCount_filter(rs.getLong("COUNT"));
                if (rs.getInt("COUNT") > 0) {
                    sbSQL.setLength(0);
                    sbSQL.append("SELECT pro.*, cat.NOMBRE AS CATEGORIA,subcat.NOMBRE AS SUBCATEGORIA ");
                    sbSQL.append("FROM  `producto` AS pro ");
                    sbSQL.append("INNER JOIN `categoria` AS cat ON cat.IDCATEGORIA=pro.IDCATEGORIA ");
                    sbSQL.append("INNER JOIN `subcategoria` AS subcat ON subcat.IDSUBCATEGORIA=pro.IDSUBCATEGORIA ");
                    sbSQL.append("WHERE ");
                    sbSQL.append(String.valueOf(parameters.get("SQL_FILTER")));
                    sbSQL.append("LIKE CONCAT('%',?,'%') ");
                    sbSQL.append(String.valueOf(parameters.get("SQL_ORDERS")));
                    sbSQL.append(parameters.get("SQL_PAGINATION"));
                    pst = conn.prepareStatement(sbSQL.toString());
                    pst.setString(1, String.valueOf(parameters.get("FILTER")));
                    LOG.info(pst.toString());
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        Producto producto = new Producto();
                        producto.setIdproducto(rs.getLong("IDPRODUCTO"));
                        producto.setCodigo(rs.getString("CODIGO"));
                        producto.setNombre(rs.getString("NOMBRE"));
                        producto.setPrecio(rs.getDouble("PRECIO"));
                        producto.setEstado(rs.getShort("ESTADO"));
                        producto.setDescripcion(rs.getString("DESCRIPCION"));
                        producto.setCategoria(new Categoria(rs.getLong("IDCATEGORIA"),
                                rs.getString("CATEGORIA")));
                        producto.setSubcategoria(new SubCategoria(rs.getLong("IDSUBCATEGORIA"),
                                rs.getString("SUBCATEGORIA")));
                        list.add(producto);
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
    public BeanCrud add(Producto t, HashMap<String, Object> parameters) throws SQLException {
        beancrud = new BeanCrud();
        PreparedStatement pst;
        ResultSet rs;
        try ( Connection conn = this.pool.getConnection();  SQLCloseable finish
                = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(IDPRODUCTO) AS COUNT FROM ");
            sSQL.append("`producto`  WHERE CODIGO = ?");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setString(1, t.getCodigo());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("INSERT INTO ");
                    sSQL.append("`producto` (CODIGO,NOMBRE,PRECIO,DESCRIPCION,");
                    sSQL.append("ESTADO,IDCATEGORIA,IDSUBCATEGORIA)");
                    sSQL.append(" VALUES(?,?,?,?,?,?,?)");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setString(1, t.getCodigo());
                    pst.setString(2, t.getNombre());
                    pst.setDouble(3, t.getPrecio());
                    pst.setString(4, t.getDescripcion());
                    pst.setShort(5, t.getEstado());
                    pst.setLong(6, t.getCategoria().getIdcategoria());
                    pst.setLong(7, t.getSubcategoria().getIdsubcategoria());
                    pst.executeUpdate();
                    conn.commit();
                    beancrud.setMessageServer("ok");
                    parameters.put("SQL_FILTER", "LOWER(pro.CODIGO) ");
                    parameters.put("SQL_ORDERS", " ORDER BY pro.CODIGO ASC ");
                    beancrud.setBeanPagination(getPagination(parameters, conn));
                } else {
                    beancrud.setMessageServer("No se registró, ya existe un Producto con el nombre ingresado");
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
    public BeanCrud update(Producto t, HashMap<String, Object> parameters) throws SQLException {
        beancrud = new BeanCrud();
        PreparedStatement pst;
        ResultSet rs;
        try ( Connection conn = this.pool.getConnection();  SQLCloseable finish = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(IDPRODUCTO) AS COUNT FROM ");
            sSQL.append("`producto` WHERE NOMBRE = ? ");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setString(1, t.getNombre());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("UPDATE ");
                    sSQL.append("`producto`  SET CODIGO = ? ,");
                    sSQL.append(" NOMBRE = ?, PRECIO = ? ,");
                    sSQL.append(" DESCRIPCION = ? ");
                    sSQL.append("WHERE IDPRODUCTO = ?");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setString(1, t.getCodigo());
                    pst.setString(2, t.getNombre());
                    pst.setDouble(3, t.getPrecio());
                    pst.setString(4, t.getDescripcion());
                    pst.setLong(5, t.getIdproducto());
                    LOG.info(pst.toString());
                    pst.executeUpdate();
                    conn.commit();
                    beancrud.setMessageServer("ok");
                    parameters.put("SQL_FILTER", "LOWER(pro.CODIGO) ");
                    parameters.put("SQL_ORDERS", " ORDER BY pro.CODIGO ASC ");
                    beancrud.setBeanPagination(getPagination(parameters, conn));
                } else {
                    beancrud.setMessageServer("No se modificó, ya existe un Producto con el nombre ingresado");
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
            sSQL.append("SELECT COUNT(IDDETALLE_PRODUCTO) AS COUNT FROM ");
            sSQL.append("`detalle_producto`  WHERE IDPRODUCTO = ?");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setInt(1, id.intValue());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("DELETE FROM ");
                    sSQL.append("`producto`  WHERE IDPRODUCTO = ?");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setInt(1, id.intValue());
                    LOG.info(pst.toString());
                    pst.executeUpdate();
                    conn.commit();
                    beancrud.setMessageServer("ok");
                    parameters.put("SQL_FILTER", "LOWER(pro.CODIGO) ");
                    parameters.put("SQL_ORDERS", " ORDER BY pro.CODIGO ASC ");
                    beancrud.setBeanPagination(getPagination(parameters, conn));
                } else {
                    beancrud.setMessageServer("No se eliminó, existe un Detalle asociado al Producto");
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
    public Producto getForId(Long id) throws SQLException {
        Producto producto = new Producto();
        PreparedStatement pst;
        ResultSet rs;
        try ( Connection conn = this.pool.getConnection();  SQLCloseable finish = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("SELECT COUNT(IDPRODUCTO) AS COUNT FROM ");
            sbSQL.append("`producto` WHERE ");
            sbSQL.append("IDPRODUCTO = ? ");
            pst = conn.prepareStatement(sbSQL.toString());
            pst.setLong(1, id);
            LOG.info(pst.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                producto.setIdproducto(rs.getLong("IDPRODUCTO"));
                producto.setCodigo(rs.getString("CODIGO"));
                producto.setNombre(rs.getString("NOMBRE"));
                producto.setPrecio(rs.getDouble("PRECIO"));
                producto.setEstado(rs.getShort("ESTADO"));
                producto.setDescripcion(rs.getString("DESCRIPCION"));

            }
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return producto;
    }

    @Override
    public BeanCrud getForCodigo(String codigo) throws SQLException {
        beancrud = new BeanCrud<Producto>();
        PreparedStatement pst;
        ResultSet rs;
        try ( Connection conn = this.pool.getConnection();  SQLCloseable finish = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("SELECT COUNT(IDPRODUCTO) AS COUNT FROM ");
            sbSQL.append("`producto` WHERE ");
            sbSQL.append("CODIGO = ? ");
            pst = conn.prepareStatement(sbSQL.toString());
            pst.setString(1, codigo);
            LOG.info(pst.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") > 0) {
                    sbSQL.setLength(0);
                    sbSQL.append("SELECT * FROM ");
                    sbSQL.append("`producto` WHERE ");
                    sbSQL.append("CODIGO = ? ");
                    pst = conn.prepareStatement(sbSQL.toString());
                    pst.setString(1, codigo);
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        Producto producto = new Producto();
                        producto.setIdproducto(rs.getLong("IDPRODUCTO"));
                        producto.setCodigo(rs.getString("CODIGO"));
                        producto.setNombre(rs.getString("NOMBRE"));
                        producto.setPrecio(rs.getDouble("PRECIO"));
                        producto.setEstado(rs.getShort("ESTADO"));
                        producto.setDescripcion(rs.getString("DESCRIPCION"));
                        beancrud.setMessageServer("El código del producto ingresado ya se encuentra registrado.");
                        beancrud.setClassGeneric(producto);
                    }
                } else {
                    beancrud.setMessageServer("noexiste");
                }
            }
            rs.close();
            pst.close();
        } catch (SQLException ex) {
            LOG.info(ex.toString());
            throw ex;
        }
        return beancrud;
    }

}
