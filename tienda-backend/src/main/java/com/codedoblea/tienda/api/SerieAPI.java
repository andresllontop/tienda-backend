/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.api;

import com.codedoblea.tienda.dao.ISerieDAO;
import com.codedoblea.tienda.dao.impl.SerieDAOImpl;
import com.codedoblea.tienda.model.Serie;
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
@Path("/series")
@Secured
public class SerieAPI {

    private static final Logger LOG = Logger.getLogger(SerieAPI.class.getName());
    private final DataSource pool;
    private final ISerieDAO serieDAO;

    public SerieAPI() {
        this.pool = DataSourceTIENDA.getPool();
        this.serieDAO = new SerieDAOImpl(this.pool);
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
        parameters.put("SQL_ORDERS", " ORDER BY TIPO_DOCUMENTO ASC ");
        parameters.put("SQL_PAGINATION", " LIMIT " + size + " OFFSET " + (page - 1) * size);
        return Response.status(Response.Status.OK)
                .entity(this.serieDAO.getPagination(parameters))
                .build();
    }

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(Serie serie) throws SQLException {
        LOG.info(serie.toString());
        return Response.status(Response.Status.OK)
                .entity(this.serieDAO.add(serie, ParametersDefault.getParametersDefaultTipoDocumento()))
                .build();
    }

    @Path("/update")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(Serie serie) throws SQLException {
        LOG.info(serie.toString());
        return Response.status(Response.Status.OK)
                .entity(this.serieDAO.update(serie, ParametersDefault.getParametersDefaultTipoDocumento()))
                .build();
    }

    @Path("/delete/{id}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id) throws SQLException {
        LOG.info(id.toString());
        return Response.status(Response.Status.OK)
                .entity(this.serieDAO.delete(id, ParametersDefault.getParametersDefaultTipoDocumento()))
                .build();
    }

}
