/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import java.security.Principal;
import java.util.List;

/**
 *
 * @author andres
 */
public class Usuario implements Principal {

    private Long idusuario;
    private String usuario;
    private String login;
    @JsonProperty(access = Access.WRITE_ONLY)
    private String pass;
    private Integer estado;
    private Short tipo_usuario;
    /*
    1 = ogbu
    2 = atendido
     */
    private Short tipo_perfil;
    /*
    //PARA USUARIOS OGBU
    0 = SA - SISTEMAS
    1 = ADMIN => TODAS LAS AREAS
    2 = farmacia
    3 = odontologia
    4 = obtetricia
    5 = social
    6 = comedor universitario
    7 = deporte
    100 = invitado(solo el inicio)
    10 = todo el servicio medico (enfermeria, medicina, farmacia, obstetricia)
    11 = psicopedagogia (medicina, psicopedagogia, social) 
    
    //>>PARA USUARIOS ATENDIDO
    1000 = CACHIMBOS
    1100 = NORMAL
    1110 = OTRO
     */
    private String foto;
    //@JsonIgnore
    private Perfil perfil;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<String> roles;

    public Usuario() {
    }

    public Usuario(Long idusuario) {
        this.idusuario = idusuario;
    }

    public Usuario(String usuario, String login) {
        this.usuario = usuario;
        this.login = login;
    }

    public Long getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Long idusuario) {
        this.idusuario = idusuario;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    public Short getTipo_usuario() {
        return tipo_usuario;
    }

    public void setTipo_usuario(Short tipo_usuario) {
        this.tipo_usuario = tipo_usuario;
    }

    public Short getTipo_perfil() {
        return tipo_perfil;
    }

    public void setTipo_perfil(Short tipo_perfil) {
        this.tipo_perfil = tipo_perfil;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public Perfil getPerfil() {
        return perfil;
    }

    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String getName() {
        return this.usuario;
    }

    @Override
    public String toString() {
        return "Usuario{" + "idusuario=" + idusuario + ", usuario=" + usuario + ", login=" + login + ", pass=" + pass + ", estado=" + estado + ", tipo_usuario=" + tipo_usuario + ", foto=" + foto + ", perfil=" + perfil + '}';
    }

}
