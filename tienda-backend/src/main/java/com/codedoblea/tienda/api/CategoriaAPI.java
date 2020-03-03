/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.api;

import com.codedoblea.tienda.dao.ICategoriaDAO;
import com.codedoblea.tienda.dao.impl.CategoriaDAOImpl;
import com.codedoblea.tienda.model.Categoria;
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
 * @author JamesCarrillo
 */
@Singleton
@Path("/categorias")
@Secured
public class CategoriaAPI {

    private static final Logger LOG = Logger.getLogger(CategoriaAPI.class.getName());
    private final DataSource pool;
    private final ICategoriaDAO categoriaDAO;

    public CategoriaAPI() {
        this.pool = DataSourceTIENDA.getPool();
        this.categoriaDAO = new CategoriaDAOImpl(this.pool);
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
                .entity(this.categoriaDAO.getPagination(parameters))
                .build();
    }

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(Categoria categoria) throws SQLException {
        LOG.info(categoria.toString());
        return Response.status(Response.Status.OK)
                .entity(this.categoriaDAO.add(categoria, ParametersDefault.getParametersDefault()))
                .build();
    }

    @Path("/update")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(Categoria categoria) throws SQLException {
        LOG.info(categoria.toString());
        return Response.status(Response.Status.OK)
                .entity(this.categoriaDAO.update(categoria, ParametersDefault.getParametersDefault()))
                .build();
    }

    @Path("/delete/{id}")
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") Long id) throws SQLException {
        LOG.info(id.toString());
        return Response.status(Response.Status.OK)
                .entity(this.categoriaDAO.delete(id, ParametersDefault.getParametersDefault()))
                .build();
    }

}
