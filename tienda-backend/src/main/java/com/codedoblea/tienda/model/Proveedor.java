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
public class Proveedor {
    private Long idproveedor;
    private String nombre;
    private Short tipo_documento;
    /*
    1=DNI
    2=RUC
    3=OTRO
    */
    private String documento;
    private Integer telefono;
    private String email;
    private String direccion;

    public Proveedor() {
    }

    public Proveedor(Long idproveedor) {
        this.idproveedor = idproveedor;
    }

    public Proveedor(Long idproveedor, String nombre, Short tipo_documento, 
            String documento, Integer telefono, String email, String direccion) {
        this.idproveedor = idproveedor;
        this.nombre = nombre;
        this.tipo_documento = tipo_documento;
        this.documento = documento;
        this.telefono = telefono;
        this.email = email;
        this.direccion = direccion;
    }

    public Long getIdproveedor() {
        return idproveedor;
    }

    public void setIdproveedor(Long idproveedor) {
        this.idproveedor = idproveedor;
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

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
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
    
}
