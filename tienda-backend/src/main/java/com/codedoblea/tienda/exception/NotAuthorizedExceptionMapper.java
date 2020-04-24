/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.exception;

import java.time.LocalDateTime;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author andres
 */
//@Provider
public class NotAuthorizedExceptionMapper implements ExceptionMapper<NotAuthorizedException> {

    /*401*/
    @Override
    public Response toResponse(NotAuthorizedException e) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new ErrorDetails(LocalDateTime.now(), 
                        "Recurso no autorizado", 
                        "Usted no se ah autenticado para acceder al recurso solicitado. " + e.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

}
