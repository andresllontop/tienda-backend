/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.dao.impl;

import com.codedoblea.tienda.dao.IPresentacionDAO;
import com.codedoblea.tienda.model.DetalleEntrada;
import com.codedoblea.tienda.model.DetalleSalida;
import com.codedoblea.tienda.model.Presentacion;
import com.codedoblea.tienda.model.Producto;
import com.codedoblea.tienda.model.UnidadMedida;
import com.codedoblea.tienda.utilities.BeanCrud;
import com.codedoblea.tienda.utilities.BeanPagination;
import com.codedoblea.tienda.utilities.UtilDateApp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author andres
 */
public class PresentacionDAOImpl implements IPresentacionDAO {

    private static final Logger LOG = Logger.getLogger(PresentacionDAOImpl.class.getName());

    private BeanCrud beancrud;

    public PresentacionDAOImpl() {

    }

    @Override
    public BeanPagination getPagination(HashMap<String, Object> parameters, Connection conn)
            throws SQLException {
        BeanPagination beanpagination = new BeanPagination();
        List<Presentacion> list = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs;
        try {
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("SELECT COUNT(PRO.IDPRODUCTO) AS COUNT ");
            sbSQL.append("FROM `presentacion` AS PRE ");
            sbSQL.append("RIGHT JOIN `producto` AS PRO ON PRO.IDPRODUCTO=PRE.IDPRODUCTO ");
            sbSQL.append("WHERE ");
            sbSQL.append("PRO.NOMBRE LIKE CONCAT('%',?,'%') OR ");
            sbSQL.append("PRO.CODIGO LIKE CONCAT('%',?,'%') ");
            pst = conn.prepareStatement(sbSQL.toString());
            pst.setString(1, String.valueOf(parameters.get("FILTER")));
            pst.setString(2, String.valueOf(parameters.get("FILTER")));
            LOG.info(pst.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                beanpagination.setCount_filter(rs.getLong("COUNT"));
                if (rs.getInt("COUNT") > 0) {
                    sbSQL.setLength(0);
                    sbSQL.append("SELECT PRE1.IDPRESENTACION,PRE1.FECHA_VENCIMIENTO,PRE1.TIPO_PRODUCTO,");
                    sbSQL.append("PRE1.EXISTENCIA,PRE1.VALOR_UNITARIO,PRE1.VALOR_TOTAL,PRE1.INVENTARIO_FINAL,");
                    sbSQL.append("PRO.*,");
                    sbSQL.append("UNID.ABREVIATURA ");
                    sbSQL.append("FROM `presentacion` AS PRE1 ");
                    sbSQL.append("JOIN (SELECT max(IDPRESENTACION) AS IDPRESENTACION ");
                    sbSQL.append("FROM `presentacion` ");
                    sbSQL.append("GROUP BY IDPRODUCTO  DESC) AS PRE2 ");
                    sbSQL.append("ON PRE1.IDPRESENTACION=PRE2.IDPRESENTACION ");
                    sbSQL.append("RIGHT JOIN `producto` AS PRO ON PRO.IDPRODUCTO=PRE1.IDPRODUCTO ");
                    sbSQL.append("INNER JOIN `unidad_medida` AS UNID ON UNID.IDUNIDAD_MEDIDA=PRO.IDUNIDAD_MEDIDA ");
                    sbSQL.append("WHERE ");
                    sbSQL.append("PRO.NOMBRE LIKE CONCAT('%',?,'%') OR ");
                    sbSQL.append("PRO.CODIGO LIKE CONCAT('%',?,'%') ");
                    sbSQL.append(String.valueOf(parameters.get("SQL_ORDERS")));
                    sbSQL.append(parameters.get("SQL_PAGINATION"));
                    pst = conn.prepareStatement(sbSQL.toString());
                    pst.setString(1, String.valueOf(parameters.get("FILTER")));
                    pst.setString(2, String.valueOf(parameters.get("FILTER")));
                    LOG.info(pst.toString());
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        Presentacion presentacion = new Presentacion();
                        presentacion.setIdpresentacion(rs.getLong("IDPRESENTACION"));
                        presentacion.setExistencia(rs.getInt("EXISTENCIA"));
                        presentacion.setValor_unitario(rs.getDouble("VALOR_UNITARIO"));
                        presentacion.setValor_total(rs.getDouble("VALOR_TOTAL"));
                        presentacion.setInventario_final(rs.getDouble("INVENTARIO_FINAL"));
                        presentacion.setFecha_vencimiento(UtilDateApp.getLocalDateTime(rs.getTimestamp("FECHA_VENCIMIENTO")));
                        Producto producto = new Producto();
                        producto.setIdproducto(rs.getLong("IDPRODUCTO"));
                        producto.setCodigo(rs.getString("CODIGO"));
                        producto.setNombre(rs.getString("NOMBRE"));
                        producto.setIndice(rs.getShort("INDICE"));
                        producto.setImpuesto(rs.getShort("IMPUESTO"));
                        producto.setDescuento_porcentaje(rs.getDouble("DESCUENTO_PORCENTAJE"));
                        producto.setPrecio_costo(rs.getDouble("PRECIO_COSTO"));
                        producto.setGanancia_porcentaje(rs.getDouble("GANANCIA_PORCENTAJE"));
                        UnidadMedida unidadMedida = new UnidadMedida();
                        unidadMedida.setAbreviatura(rs.getString("ABREVIATURA"));
                        producto.setUnidad_medida(unidadMedida);
                        presentacion.setProducto(producto);
                        list.add(presentacion);
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

        return beancrud;
    }

    @Override
    public BeanCrud add(Presentacion t, HashMap<String, Object> parameters) throws SQLException {
        beancrud = new BeanCrud();

        return beancrud;
    }

    @Override
    public BeanCrud update(Presentacion t, HashMap<String, Object> parameters) throws SQLException {
        beancrud = new BeanCrud();

        return beancrud;
    }

    @Override
    public BeanCrud delete(Long id, HashMap<String, Object> parameters) throws SQLException {
        beancrud = new BeanCrud();

        return beancrud;
    }

    @Override
    public Presentacion getForId(Long id) throws SQLException {
        Presentacion presentacion = new Presentacion();

        return presentacion;
    }

    @Override
    public Long addBeanPresentacionEntrada(List<DetalleEntrada> list, Connection con) throws SQLException {
        Long valor = null;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("INSERT INTO ");
            sSQL.append("`presentacion`(EXISTENCIA,");
            sSQL.append("FECHA_VENCIMIENTO,TIPO_PRODUCTO,IDPRODUCTO,");
            sSQL.append("VALOR_UNITARIO,VALOR_TOTAL,INVENTARIO_FINAL) ");
            sSQL.append("VALUES(?,?,?,?,?,?,?)");
            pst = con.prepareStatement(sSQL.toString());
            for (DetalleEntrada detalleentrada : list) {
                pst.setInt(1, detalleentrada.getPresentacion().getExistencia());
                pst.setTimestamp(2, UtilDateApp.getTimestamp(detalleentrada.getPresentacion().getFecha_vencimiento()));
                pst.setShort(3, detalleentrada.getPresentacion().getTipo_producto());
                if (null == detalleentrada.getPresentacion().getTipo_producto()) {
                    pst.setString(4, null);
                } else {
                    switch (detalleentrada.getPresentacion().getTipo_producto()) {
                        case 1:
                            pst.setLong(4, detalleentrada.getPresentacion().getProducto().getIdproducto());
                            break;
                        case 2:
                            pst.setLong(4, detalleentrada.getPresentacion().getDetalle_producto_color().getIddetalle_producto_color());
                            break;
                        default:
                            pst.setString(4, null);
                            break;
                    }
                }
                pst.setDouble(5, detalleentrada.getPresentacion().getValor_unitario());
                pst.setDouble(6, detalleentrada.getPresentacion().getValor_total());
                pst.setDouble(7, detalleentrada.getPresentacion().getInventario_final());
                LOG.info(pst.toString());
                pst.executeUpdate();
            }
            sSQL.setLength(0);
            sSQL.append("SELECT MAX(IDPRESENTACION) AS MAXIMO FROM ");
            sSQL.append("`presentacion` ");
            pst = con.prepareStatement(sSQL.toString());
            LOG.info(pst.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                valor = rs.getLong("MAXIMO");
            }
        } catch (SQLException ex) {
            throw ex;
        }
        return valor;
    }

    @Override
    public Long addBeanPresentacionSalida(List<DetalleSalida> list, Connection con) throws SQLException {
        Long valor = null;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("INSERT INTO ");
            sSQL.append("`presentacion`(EXISTENCIA,");
            sSQL.append("FECHA_VENCIMIENTO,TIPO_PRODUCTO,IDPRODUCTO,");
            sSQL.append("VALOR_UNITARIO,VALOR_TOTAL,INVENTARIO_FINAL) ");
            sSQL.append("VALUES(?,?,?,?,?,?,?)");
            pst = con.prepareStatement(sSQL.toString());
            for (DetalleSalida detallesalida : list) {
                pst.setInt(1, detallesalida.getPresentacion().getExistencia());
                pst.setTimestamp(2, UtilDateApp.getTimestamp(detallesalida.getPresentacion().getFecha_vencimiento()));
                pst.setShort(3, detallesalida.getPresentacion().getTipo_producto());
                if (null == detallesalida.getPresentacion().getTipo_producto()) {
                    pst.setString(4, null);
                } else {
                    switch (detallesalida.getPresentacion().getTipo_producto()) {
                        case 1:
                            pst.setLong(4, detallesalida.getPresentacion().getProducto().getIdproducto());
                            break;
                        case 2:
                            pst.setLong(4, detallesalida.getPresentacion().getDetalle_producto_color().getIddetalle_producto_color());
                            break;
                        default:
                            pst.setString(4, null);
                            break;
                    }
                }
                pst.setDouble(5, detallesalida.getPresentacion().getValor_unitario());
                pst.setDouble(6, detallesalida.getPresentacion().getValor_total());
                pst.setDouble(7, detallesalida.getPresentacion().getInventario_final());
                LOG.info(pst.toString());
                pst.executeUpdate();
            }
            sSQL.setLength(0);
            sSQL.append("SELECT MAX(IDPRESENTACION) AS MAXIMO FROM ");
            sSQL.append("`presentacion` ");
            pst = con.prepareStatement(sSQL.toString());
            LOG.info(pst.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                valor = rs.getLong("MAXIMO");
            }
        } catch (SQLException ex) {
            throw ex;
        }
        return valor;
    }

}
