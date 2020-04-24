/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.api;

import com.codedoblea.tienda.dao.IColorDAO;
import com.codedoblea.tienda.dao.impl.ColorDAOImpl;
import com.codedoblea.tienda.model.Color;
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
@Path("/colores")
@Secured

public class ColorAPI {

    private static final Logger LOG = Logger.getLogger(ColorAPI.class.getName());
    private final DataSource pool;
    private final IColorDAO colorDAO;

    public ColorAPI() {
        this.pool = DataSourceTIENDA.getPool();
        this.colorDAO = new ColorDAOImpl(this.pool);
    }
    @Path("/paginate")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response paginate(
            @QueryParam("nombre") String nombre
            ) throws Exception {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("FILTER", nombre.toLowerCase());
        parameters.put("SQL_ORDERS", " ORDER BY IDCOLOR ASC ");
        return Response.status(Response.Status.OK)
                .entity(this.colorDAO.getPagination(parameters))
                .build();
    }

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(Color color) throws SQLException {
        LOG.info(color.toString());
        return Response.status(Response.Status.OK)
                .entity(this.colorDAO.add(color, ParametersDefault.getParametersDefault()))
                .build();
    }

    @Path("/update")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(Color color) throws SQLException {
        LOG.info(color.toString());
        return Response.status(Response.Status.OK)
                .entity(this.colorDAO.update(color, ParametersDefault.getParametersDefault()))
                .build();
    }

    @Path("/delete/{id}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id) throws SQLException {
        LOG.info(id.toString());
        return Response.status(Response.Status.OK)
                .entity(this.colorDAO.delete(id, ParametersDefault.getParametersDefault()))
                .build();
    }

}

