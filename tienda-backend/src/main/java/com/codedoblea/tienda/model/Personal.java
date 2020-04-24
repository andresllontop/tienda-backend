/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.model;

/**
 *
 * @author andres
 */
public class Personal extends Persona{
//    private Cargo cargo;
    private Usuario usuario;
    private Short estado;

    public Personal() {
         super();
    }

    public Personal(Usuario usuario, Short estado, Long idpersona, String nombre, 
            String apellido_mat, String apellido_pat, Short tipo_documento, 
            Short sexo, Integer documento, Integer telefono, String email,
            String direccion) {
        super(idpersona, nombre, apellido_mat, apellido_pat, tipo_documento, 
                sexo, documento, telefono, email, direccion);
        this.usuario = usuario;
        this.estado = estado;
    }

      public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Short getEstado() {
        return estado;
    }

    public void setEstado(Short estado) {
        this.estado = estado;
    }
    
}
