/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.dao.impl;

import com.codedoblea.tienda.dao.IColorDetalleProductoDAO;
import com.codedoblea.tienda.dao.IDetalleProductoDAO;
import com.codedoblea.tienda.model.Color;
import com.codedoblea.tienda.model.ColorDetalleProducto;
import com.codedoblea.tienda.model.DetalleProducto;
import com.codedoblea.tienda.model.UnidadMedida;
import com.codedoblea.tienda.model.Producto;
import com.codedoblea.tienda.model.others.BeanDetalleProducto;

import com.codedoblea.tienda.utilities.BeanPagination;

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
public class DetalleProductoDAOImpl implements IDetalleProductoDAO {

    private static final Logger LOG = Logger.getLogger(DetalleProductoDAOImpl.class.getName());

    public DetalleProductoDAOImpl() {
    }

    @Override
    public BeanPagination getPagination(HashMap<String, Object> parameters, Connection con) throws SQLException {
        BeanPagination beanpagination = new BeanPagination();
        List<ColorDetalleProducto> list = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs;
        try {
            StringBuilder sbSQL = new StringBuilder();
            sbSQL.append("SELECT COUNT(PRO.IDPRODUCTO) AS COUNT FROM ");
            sbSQL.append("`producto` AS PRO ");
            sbSQL.append("INNER JOIN `item` AS ITE ON ITE.IDITEM=PRO.IDCATEGORIA ");
            sbSQL.append("WHERE PRO.IDPRODUCTO = ? ");
            pst = con.prepareStatement(sbSQL.toString());
            pst.setString(1, String.valueOf(parameters.get("FILTER")));
            LOG.info(pst.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                beanpagination.setCount_filter(rs.getLong("COUNT"));
                if (rs.getInt("COUNT") > 0) {
                    sbSQL.setLength(0);
                    sbSQL.append("SELECT DET.LONGITUD,DET.IDPRODUCTO,");
                    sbSQL.append("DETCOLOR.*,COL.CODIGO, ");
                    sbSQL.append("PRO.CODIGO AS PROCODIGO,PRO.NOMBRE AS PRONOMBRE, ");
                    sbSQL.append("PRO.PRECIO_COSTO,PRO.DESCUENTO_PORCENTAJE,PRO.GANANCIA_PORCENTAJE, ");
                    sbSQL.append("UNI.ABREVIATURA ");
                    sbSQL.append("FROM `detalle_producto` AS DET ");
                    sbSQL.append("INNER JOIN `producto` AS PRO ON PRO.IDPRODUCTO=DET.IDPRODUCTO ");
                    sbSQL.append("INNER JOIN `unidad_medida` AS UNI ON UNI.IDUNIDAD_MEDIDA=PRO.IDUNIDAD_MEDIDA ");
                    sbSQL.append("INNER JOIN `detalle_producto_color` AS DETCOLOR ON DETCOLOR.IDDETALLE_PRODUCTO=DET.IDDETALLE_PRODUCTO ");
                    sbSQL.append("INNER JOIN `color` AS COL ON COL.IDCOLOR=DETCOLOR.IDCOLOR ");
                    sbSQL.append("WHERE DET.IDPRODUCTO = ? ");
                    pst = con.prepareStatement(sbSQL.toString());
                    pst.setString(1, String.valueOf(parameters.get("FILTER")));
                    LOG.info(pst.toString());
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        Producto producto = new Producto();
                        producto.setIdproducto(rs.getLong("IDPRODUCTO"));
                        producto.setCodigo(rs.getString("PROCODIGO"));
                        producto.setNombre(rs.getString("PRONOMBRE"));
                         producto.setDescuento_porcentaje(rs.getDouble("DESCUENTO_PORCENTAJE"));
                        producto.setPrecio_costo(rs.getDouble("PRECIO_COSTO"));
                        producto.setGanancia_porcentaje(rs.getDouble("GANANCIA_PORCENTAJE"));
                        UnidadMedida unidadMedida = new UnidadMedida();
                        unidadMedida.setAbreviatura(rs.getString("ABREVIATURA"));
                        producto.setUnidad_medida(unidadMedida);
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

    @Override
    public Boolean addBeanDetalleProducto(List<BeanDetalleProducto> list, Long ID, Connection con) throws SQLException {
        List<BeanDetalleProducto> listnew = new ArrayList<>();
        Boolean valor = false;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            Integer IDmaximoDetalle = 0;
            sSQL.append("INSERT INTO ");
            sSQL.append("`detalle_producto`(LONGITUD,");
            sSQL.append("IDPRODUCTO) ");
            sSQL.append("VALUES(?,?)");
            pst = con.prepareStatement(sSQL.toString());
            for (BeanDetalleProducto detalleProducto : list) {
                pst.setString(1, detalleProducto.getDetalle_producto().getLongitud());
                pst.setLong(2, ID);
                pst.executeUpdate();
            }
            sSQL.setLength(0);
            sSQL.append("SELECT MAX(IDDETALLE_PRODUCTO) AS MAXIMOID FROM ");
            sSQL.append("`detalle_producto` ");
            pst = con.prepareStatement(sSQL.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                IDmaximoDetalle = rs.getInt("MAXIMOID");
            }
            Long longitud = Long.parseLong("" + (IDmaximoDetalle - list.size())) + 1;
            for (BeanDetalleProducto detalleProducto : list) {
                detalleProducto.setDetalle_producto(new DetalleProducto(longitud++));
                listnew.add(detalleProducto);
            }
            IColorDetalleProductoDAO colorDetalleProductoDAO = new ColorDetalleProductoDAOImpl();
            if (colorDetalleProductoDAO.add(listnew, con)) {
                valor = true;
            }
            pst.close();
            rs.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return valor;
    }

    @Override
    public Boolean updateBeanDetallePorducto(List<BeanDetalleProducto> list, Connection con) throws SQLException {
        List<BeanDetalleProducto> listnew = new ArrayList<>();
        Boolean valor = false;
        PreparedStatement pst;
        ResultSet rs;
        try {
            con.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("DELETE FROM ");
            sSQL.append("`detalle_producto_color`  WHERE IDDETALLE_PRODUCTO = ? ");
            pst = con.prepareStatement(sSQL.toString());
            for (BeanDetalleProducto detalleProducto : list) {
                if (detalleProducto.getDetalle_producto().getIddetalle_producto() != -1) {
                    pst.setLong(1, detalleProducto.getDetalle_producto().getIddetalle_producto());
                    LOG.info(pst.toString());
                    pst.executeUpdate();
                }
            }

            sSQL.setLength(0);
            sSQL.append("DELETE FROM ");
            sSQL.append("`detalle_producto`  WHERE IDPRODUCTO = ?");
            pst = con.prepareStatement(sSQL.toString());
            pst.setLong(1, list.get(0).getDetalle_producto().getProducto().getIdproducto());
            LOG.info(pst.toString());
            pst.executeUpdate();

            sSQL.setLength(0);
            Integer IDmaximoDetalle = 0;
            sSQL.append("INSERT INTO ");
            sSQL.append("`detalle_producto`(LONGITUD,");
            sSQL.append("IDPRODUCTO) ");
            sSQL.append("VALUES(?,?)");
            pst = con.prepareStatement(sSQL.toString());
            for (BeanDetalleProducto detalleProducto : list) {
                pst.setString(1, detalleProducto.getDetalle_producto().getLongitud());
                pst.setLong(2, list.get(0).getDetalle_producto().getProducto().getIdproducto());
                pst.executeUpdate();
            }

            sSQL.setLength(0);
            sSQL.append("SELECT MAX(IDDETALLE_PRODUCTO) AS MAXIMOID FROM ");
            sSQL.append("`detalle_producto` ");
            pst = con.prepareStatement(sSQL.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                IDmaximoDetalle = rs.getInt("MAXIMOID");
            }
            Long longitud = Long.parseLong("" + (IDmaximoDetalle - list.size())) + 1;
            for (BeanDetalleProducto detalleProducto : list) {
                detalleProducto.setDetalle_producto(new DetalleProducto(longitud++));
                listnew.add(detalleProducto);
            }
            IColorDetalleProductoDAO colorDetalleProductoDAO = new ColorDetalleProductoDAOImpl();
            if (colorDetalleProductoDAO.add(listnew, con)) {
                valor = true;
            }
            pst.close();
            rs.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return valor;
    }
}
