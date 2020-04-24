/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.exception;

import java.time.LocalDateTime;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author andres
 */
//@Provider
public class InternalServerErrorExceptionMapper implements javax.ws.rs.ext.ExceptionMapper<Exception> {

    /*500*/
    @Override
    public Response toResponse(Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorDetails(LocalDateTime.now(),
                        "Error Interno en el servidor ",
                        e.toString()))
                .type(MediaType.APPLICATION_JSON).build();
    }

}
