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
public class Persona {

    private Long idpersona;
    private String nombre;
    private String apellido_mat;
    private String apellido_pat;
    private Short tipo_documento;
    /*
    1=DNI
    2=CARNET DE EXTRANJERIAS
    3==OTRO
     */
    private Integer documento;
     private Short sexo;
      /*
    1=MASCULINO
    2=FEMENINO
    3==OTRO
     */
    private Integer telefono;
    private String email;
    private String direccion;

    public Persona() {
    }

    public Persona(Long idpersona) {
        this.idpersona = idpersona;
    }

    public Persona(Long idpersona, String nombre, String apellido_mat, 
            String apellido_pat, Short tipo_documento, Short sexo, 
            Integer documento, Integer telefono, String email, String direccion) {
        this.idpersona = idpersona;
        this.nombre = nombre;
        this.apellido_mat = apellido_mat;
        this.apellido_pat = apellido_pat;
        this.tipo_documento = tipo_documento;
        this.sexo = sexo;
        this.documento = documento;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }

    public String getApellido_mat() {
        return apellido_mat;
    }

    public void setApellido_mat(String apellido_mat) {
        this.apellido_mat = apellido_mat;
    }

    public String getApellido_pat() {
        return apellido_pat;
    }

    public void setApellido_pat(String apellido_pat) {
        this.apellido_pat = apellido_pat;
    }

    public Long getIdpersona() {
        return idpersona;
    }

    public void setIdpersona(Long idpersona) {
        this.idpersona = idpersona;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Short getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(Short tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public Integer getDocumento() {
        return documento;
    }

    public void setDocumento(Integer documento) {
        this.documento = documento;
    }

    public Integer getTelefono() {
        return telefono;
    }

    public void setTelefono(Integer telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
    public Short getSexo() {
        return sexo;
    }

    public void setSexo(Short sexo) {
        this.sexo = sexo;
    }

}
