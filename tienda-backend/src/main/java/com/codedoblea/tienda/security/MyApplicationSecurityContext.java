/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.security;

import com.codedoblea.tienda.model.Usuario;
import java.security.Principal;
import javax.ws.rs.core.SecurityContext;
/**
 *
 * @author andres
 */
public class MyApplicationSecurityContext implements SecurityContext {

    private final Usuario usuario;
    private final boolean issecure;

    public MyApplicationSecurityContext(Usuario usuario, Boolean issecure) {
        this.usuario = usuario;
        this.issecure = issecure;
    }

    @Override
    public Principal getUserPrincipal() {
        return usuario;
    }

    @Override
    public boolean isUserInRole(String rol) {
        return this.usuario.getRoles().contains(rol);
    }

    @Override
    public boolean isSecure() {
        return this.issecure;
    }

    @Override
    public String getAuthenticationScheme() {
        return SecurityContext.FORM_AUTH;
    }

}

