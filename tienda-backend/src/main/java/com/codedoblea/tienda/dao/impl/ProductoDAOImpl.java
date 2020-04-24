/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.dao.impl;

import com.codedoblea.tienda.dao.IDetalleProductoDAO;
import com.codedoblea.tienda.dao.IProductoDAO;
import com.codedoblea.tienda.dao.SQLCloseable;
import com.codedoblea.tienda.model.Color;
import com.codedoblea.tienda.model.Item;
import com.codedoblea.tienda.model.Producto;
import com.codedoblea.tienda.model.DetalleProducto;
import com.codedoblea.tienda.model.ColorDetalleProducto;
import com.codedoblea.tienda.model.UnidadMedida;
import com.codedoblea.tienda.model.others.BeanProducto;
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
    private IDetalleProductoDAO detalleProductoDAO;

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
            sbSQL.append("SELECT COUNT(PRO.IDPRODUCTO) AS COUNT FROM ");
            sbSQL.append("`producto` AS PRO ");
            sbSQL.append("INNER JOIN `item` AS ITE ON ITE.IDITEM=PRO.IDCATEGORIA ");
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
                    sbSQL.append("SELECT PRO.*,");
                    sbSQL.append("CAT.NOMBRE AS CATEGORIA,");
                    sbSQL.append("MAR.NOMBRE AS MARCA,");
                    sbSQL.append("UNI.NOMBRE AS UNIDAD_MEDIDA,UNI.ABREVIATURA ");
                    sbSQL.append("FROM  `producto` AS PRO ");
                    sbSQL.append("INNER JOIN `item` AS CAT ON CAT.IDITEM = PRO.IDCATEGORIA ");
                    sbSQL.append("INNER JOIN `item` AS MAR ON MAR.IDITEM = PRO.IDMARCA ");
                    sbSQL.append("INNER JOIN `unidad_medida` AS UNI ON UNI.IDUNIDAD_MEDIDA=PRO.IDUNIDAD_MEDIDA ");
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
                        producto.setDescuento_porcentaje(rs.getDouble("DESCUENTO_PORCENTAJE"));
                        producto.setPrecio_costo(rs.getDouble("PRECIO_COSTO"));
                        producto.setGanancia_porcentaje(rs.getDouble("GANANCIA_PORCENTAJE"));
                        producto.setEstado(rs.getShort("ESTADO"));
                        producto.setCantidad(rs.getInt("CANTIDAD"));
                        producto.setIndice(rs.getShort("INDICE"));
                        producto.setImpuesto(rs.getShort("IMPUESTO"));
                        producto.setCantidad_minima(rs.getShort("CANTIDAD_MINIMA"));
                        producto.setUnidad_medida(new UnidadMedida(rs.getLong("IDUNIDAD_MEDIDA"),
                                rs.getString("ABREVIATURA"), rs.getString("UNIDAD_MEDIDA")));
                        producto.setDescripcion(rs.getString("DESCRIPCION"));
                        producto.setCategoria(new Item(rs.getLong("IDCATEGORIA"),
                                rs.getString("CATEGORIA"), Short.parseShort("1")));
                        producto.setMarca(new Item(rs.getLong("IDMARCA"),
                                rs.getString("MARCA"), Short.parseShort("2")));
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
            switch (String.valueOf(parameters.get("INDICE"))) {
                case "1":
                    beancrud.setBeanPagination(getPaginationBeanProducto(parameters, conn));
                    break;
                case "2":
                    detalleProductoDAO = new DetalleProductoDAOImpl();
                    beancrud.setBeanPagination(detalleProductoDAO.getPagination(parameters, conn));
                    break;
                default:
                    beancrud.setBeanPagination(getPagination(parameters, conn));
                    break;
            }

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
                        producto.setPrecio_costo(rs.getDouble("PRECIO_COSTO"));
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

    @Override
    public BeanCrud addBeanProducto(BeanProducto beanproducto) throws SQLException {
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
            pst.setString(1, beanproducto.getProducto().getCodigo());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("INSERT INTO ");
                    sSQL.append("`producto` (CODIGO,NOMBRE,PRECIO_COSTO,DESCRIPCION,");
                    sSQL.append("ESTADO,CANTIDAD_MINIMA,IDCATEGORIA,IDMARCA,");
                    sSQL.append("IDUNIDAD_MEDIDA,CANTIDAD,INDICE,");
                    sSQL.append("GANANCIA_PORCENTAJE,DESCUENTO_PORCENTAJE,IMPUESTO)");
                    sSQL.append(" VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setString(1, beanproducto.getProducto().getCodigo());
                    pst.setString(2, beanproducto.getProducto().getNombre());
                    pst.setDouble(3, beanproducto.getProducto().getPrecio_costo());
                    pst.setString(4, beanproducto.getProducto().getDescripcion());
                    pst.setShort(5, beanproducto.getProducto().getEstado());
                    pst.setShort(6, beanproducto.getProducto().getCantidad_minima());
                    pst.setLong(7, beanproducto.getProducto().getCategoria().getIditem());
                    pst.setLong(8, beanproducto.getProducto().getMarca().getIditem());
                    pst.setLong(9, beanproducto.getProducto().getUnidad_medida().getIdunidad_medida());
                    pst.setInt(10, beanproducto.getProducto().getCantidad());
                    pst.setShort(11, beanproducto.getProducto().getIndice());
                    pst.setDouble(12, beanproducto.getProducto().getGanancia_porcentaje());
                    pst.setDouble(13, beanproducto.getProducto().getDescuento_porcentaje());
                    pst.setShort(14, beanproducto.getProducto().getImpuesto());
                    pst.executeUpdate();
                    sSQL.setLength(0);
                    sSQL.append("SELECT MAX(IDPRODUCTO) AS MAXIMOPRO FROM ");
                    sSQL.append("`producto` ");
                    pst = conn.prepareStatement(sSQL.toString());
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        detalleProductoDAO = new DetalleProductoDAOImpl();
                        if (beanproducto.getList().size() > 0) {
                            if (detalleProductoDAO.addBeanDetalleProducto(beanproducto.getList(),
                                    rs.getLong("MAXIMOPRO"), conn)) {
                                conn.commit();
                                beancrud.setMessageServer("ok");
                            } else {
                                beancrud.setMessageServer("No se registró");
                            }
                        } else {
                            conn.commit();
                            beancrud.setMessageServer("ok");
                        }
                    }
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
    public BeanCrud updateBeanProducto(BeanProducto beanproducto) throws SQLException {
        beancrud = new BeanCrud();
        PreparedStatement pst;
        ResultSet rs;
        try ( Connection conn = this.pool.getConnection();  SQLCloseable finish
                = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(IDPRODUCTO) AS COUNT FROM ");
            sSQL.append("`producto` WHERE (NOMBRE = ? OR CODIGO = ?) AND IDPRODUCTO != ?");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setString(1, beanproducto.getProducto().getNombre());
            pst.setString(2, beanproducto.getProducto().getCodigo());
            pst.setLong(3, beanproducto.getProducto().getIdproducto());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("UPDATE ");
                    sSQL.append("`producto`  SET CODIGO = ? , ");
                    sSQL.append("NOMBRE = ?, PRECIO_COSTO = ? , ");
                    sSQL.append("DESCRIPCION = ?, CANTIDAD_MINIMA = ?, ESTADO = ?, ");
                    sSQL.append("CANTIDAD = ?, INDICE = ?, IDUNIDAD_MEDIDA = ?, ");
                    sSQL.append("IDCATEGORIA = ?, IDMARCA = ?, GANANCIA_PORCENTAJE = ?,");
                    sSQL.append("DESCUENTO_PORCENTAJE = ? , IMPUESTO = ? ");
                    sSQL.append("WHERE IDPRODUCTO = ?");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setString(1, beanproducto.getProducto().getCodigo());
                    pst.setString(2, beanproducto.getProducto().getNombre());
                    pst.setDouble(3, beanproducto.getProducto().getPrecio_costo());
                    pst.setString(4, beanproducto.getProducto().getDescripcion());
                    pst.setShort(5, beanproducto.getProducto().getCantidad_minima());
                    pst.setShort(6, beanproducto.getProducto().getEstado());
                    pst.setInt(7, beanproducto.getProducto().getCantidad());
                    pst.setShort(8, beanproducto.getProducto().getIndice());
                    pst.setLong(9, beanproducto.getProducto().getUnidad_medida().getIdunidad_medida());
                    pst.setLong(10, beanproducto.getProducto().getCategoria().getIditem());
                    pst.setLong(11, beanproducto.getProducto().getMarca().getIditem());
                    pst.setDouble(12, beanproducto.getProducto().getGanancia_porcentaje());
                    pst.setDouble(13, beanproducto.getProducto().getDescuento_porcentaje());
                    pst.setShort(14, beanproducto.getProducto().getImpuesto());
                    pst.setLong(15, beanproducto.getProducto().getIdproducto());
                    LOG.info(pst.toString());
                    pst.executeUpdate();
                    if (beanproducto.getList().size() > 0) {
                        detalleProductoDAO = new DetalleProductoDAOImpl();
                        if (detalleProductoDAO.updateBeanDetallePorducto(beanproducto.getList(),
                                conn)) {
                            conn.commit();
                            beancrud.setMessageServer("ok");
                        } else {
                            beancrud.setMessageServer("No se registró");
                        }
                    } else {
                        conn.commit();
                        beancrud.setMessageServer("ok");
                    }

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

    private BeanPagination getPaginationBeanProducto(HashMap<String, Object> parameters, Connection conn)
            throws SQLException {
        BeanPagination beanpagination = new BeanPagination();
        List<ColorDetalleProducto> list = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs;
        try {
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("SELECT COUNT(PRO.IDPRODUCTO) AS COUNT FROM ");
            sbSQL.append("`producto` AS PRO ");
            sbSQL.append("INNER JOIN `item` AS ITE ON ITE.IDITEM=PRO.IDCATEGORIA ");
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
                    sbSQL.append("SELECT PRO.CODIGO,PRO.IDPRODUCTO,");
                    sbSQL.append("PRO.NOMBRE,PRO.PRECIO_COMPRA,PRO.CANTIDAD AS TOTAL,");
                    sbSQL.append("DET.LONGITUD,DET.IDPRODUCTO,DET.LONGITUD,");
                    sbSQL.append("DETCOLOR.*,COL.CODIGO ");
                    sbSQL.append("FROM  `producto` AS PRO ");
                    sbSQL.append("LEFT JOIN `detalle_producto` AS DET ON DET.IDPRODUCTO=PRO.IDPRODUCTO ");
                    sbSQL.append("LEFT JOIN `detalle_producto_color` AS DETCOLOR ON DETCOLOR.IDDETALLE_PRODUCTO=DET.IDDETALLE_PRODUCTO ");
                    sbSQL.append("LEFT JOIN `color` AS COL ON COL.IDCOLOR=DETCOLOR.IDCOLOR ");
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
                        producto.setPrecio_costo(rs.getDouble("PRECIO_COSTO"));
                        producto.setCantidad(rs.getInt("TOTAL"));
                        DetalleProducto detalleproducto = new DetalleProducto();
                        detalleproducto.setProducto(producto);
                        detalleproducto.setIddetalle_producto(rs.getLong("IDDETALLE_PRODUCTO"));
                        detalleproducto.setLongitud(rs.getString("LONGITUD"));
                        ColorDetalleProducto colordetalleproducto = new ColorDetalleProducto();
                        colordetalleproducto.setIddetalle_producto_color(rs.getLong("IDDETALLE_PRODUCTO_COLOR"));
                        colordetalleproducto.setDetalle_producto(detalleproducto);
                        colordetalleproducto.setCantidad(rs.getInt("CANTIDAD"));
                        colordetalleproducto.setIdcolor(new Color(rs.getLong("IDCOLOR"), rs.getString("CODIGO"), null));
                        list.add(colordetalleproducto);

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

}
