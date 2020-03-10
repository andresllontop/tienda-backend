/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.api;

import com.codedoblea.tienda.dao.IProductoDAO;
import com.codedoblea.tienda.dao.impl.ProductoDAOImpl;
import com.codedoblea.tienda.model.Producto;
import com.codedoblea.tienda.security.annotation.Secured;
import com.codedoblea.tienda.utilities.DataSourceTIENDA;
import com.codedoblea.tienda.utilities.ParametersDefault;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.inject.Singleton;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author andres
 */
@Singleton
@Path("/productos")
@Secured
public class ProductoAPI {

    private static final Logger LOG = Logger.getLogger(ProductoAPI.class.getName());
    private final DataSource pool;
    private final IProductoDAO productoDAO;

    public ProductoAPI() {
        this.pool = DataSourceTIENDA.getPool();
        this.productoDAO = new ProductoDAOImpl(this.pool);
    }

    @Path("/paginate")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response paginate(
            @QueryParam("nombre") String nombre,
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size) throws Exception {
        HashMap<String, Object> parameters = new HashMap<>();
        switch (Integer.parseInt(nombre.substring(nombre.length() - 1))) {
            case 1:
                parameters.put("SQL_FILTER", "LOWER(pro.NOMBRE) ");
                parameters.put("SQL_ORDERS", " ORDER BY pro.NOMBRE ASC ");
                break;
            case 2:
                 parameters.put("SQL_FILTER","LOWER(cat.NOMBRE) ");
                 parameters.put("SQL_ORDERS", " ORDER BY cat.NOMBRE ASC ");
                break;
            case 3:
                 parameters.put("SQL_FILTER", "PRECIO ");
                 parameters.put("SQL_ORDERS", " ORDER BY pro.PRECIO ASC ");
                break;
            default:
                 parameters.put("SQL_FILTER", "LOWER(pro.CODIGO) ");
                 parameters.put("SQL_ORDERS", " ORDER BY pro.CODIGO ASC ");
                break;
        }
        parameters.put("FILTER", nombre.substring(0, nombre.length() - 1).toLowerCase());
        parameters.put("SQL_PAGINATION", " LIMIT " + size + " OFFSET " + (page - 1) * size);
        return Response.status(Response.Status.OK)
                .entity(this.productoDAO.getPagination(parameters))
                .build();
    }

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(Producto producto) throws SQLException {
        LOG.info(producto.toString());
        return Response.status(Response.Status.OK)
                .entity(this.productoDAO.add(producto, ParametersDefault.getParametersDefault()))
                .build();
    }

    @Path("/update")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(Producto producto) throws SQLException {
        LOG.info(producto.toString());
        return Response.status(Response.Status.OK)
                .entity(this.productoDAO.update(producto, ParametersDefault.getParametersDefault()))
                .build();
    }

    @Path("/delete/{id}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id) throws SQLException {
        LOG.info(id.toString());
        return Response.status(Response.Status.OK)
                .entity(this.productoDAO.delete(id, ParametersDefault.getParametersDefault()))
                .build();
    }

    @Path("/model")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response model(
            @QueryParam("codigo") String codigo) throws Exception {
        return Response.status(Response.Status.OK)
                .entity(this.productoDAO.getForCodigo(codigo))
                .build();
    }
}
