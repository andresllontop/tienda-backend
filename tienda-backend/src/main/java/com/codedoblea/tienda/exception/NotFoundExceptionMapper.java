/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.exception;

import java.time.LocalDateTime;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author andres
 */
//@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    /*404*/
    @Override
    public Response toResponse(NotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorDetails(LocalDateTime.now(),
                        "Recurso no encontrado",
                        "No se encontró el recurso solicitado. "
                                + e.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}
