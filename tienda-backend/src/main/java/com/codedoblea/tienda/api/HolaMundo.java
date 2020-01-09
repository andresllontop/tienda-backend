/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 *
 * @author andres
 */
@Path("/saludos")
public class HolaMundo {

    @Path("/saludar")
    @GET
    public String getsaludo() {
        return "holi boli";
    }

}
