/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.api;

import com.codedoblea.tienda.dao.IPuntoVentaDAO;
import com.codedoblea.tienda.dao.impl.PuntoVentaDAOImpl;
import com.codedoblea.tienda.model.PuntoVenta;
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
@Path("/puntoventas")
@Secured
public class PuntoVentaAPI {

    private static final Logger LOG = Logger.getLogger(PuntoVentaAPI.class.getName());
    private final DataSource pool;
    private final IPuntoVentaDAO puntoVentaDAO;

    public PuntoVentaAPI() {
        this.pool = DataSourceTIENDA.getPool();
        this.puntoVentaDAO = new PuntoVentaDAOImpl(this.pool);
    }
    @Path("/paginate")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response paginate(
            @QueryParam("nombre") String nombre,
            @QueryParam("page") Integer page,
            @QueryParam("size") Integer size) throws Exception {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("FILTER", nombre.toLowerCase());
        parameters.put("SQL_ORDERS", " ORDER BY NOMBRE ASC ");
        parameters.put("SQL_PAGINATION", " LIMIT " + size + " OFFSET " + (page - 1) * size);
        return Response.status(Response.Status.OK)
                .entity(this.puntoVentaDAO.getPagination(parameters))
                .build();
    }

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(PuntoVenta puntoVenta) throws SQLException {
        LOG.info(puntoVenta.toString());
        return Response.status(Response.Status.OK)
                .entity(this.puntoVentaDAO.add(puntoVenta, ParametersDefault.getParametersDefault()))
                .build();
    }

    @Path("/update")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(PuntoVenta puntoVenta) throws SQLException {
        LOG.info(puntoVenta.toString());
        return Response.status(Response.Status.OK)
                .entity(this.puntoVentaDAO.update(puntoVenta, ParametersDefault.getParametersDefault()))
                .build();
    }

    @Path("/delete/{id}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id) throws SQLException {
        LOG.info(id.toString());
        return Response.status(Response.Status.OK)
                .entity(this.puntoVentaDAO.delete(id, ParametersDefault.getParametersDefault()))
                .build();
    }

}
