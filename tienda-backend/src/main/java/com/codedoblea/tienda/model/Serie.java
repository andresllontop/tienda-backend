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
public class Serie {

    private Long idserie;
    private Short tipo_documento;
    /*
     1=BOLETA;
     2=FACTURA;
     3=TICKET;
     4=OTRO;
     */
    private String numero_serie;
    private String numero_inicial;
    private String numero_final;
    private String numero_actual;

    public Serie() {
    }

    public Serie(Long idserie) {
        this.idserie = idserie;
    }

    public Serie(Long idserie, Short tipo_documento, String numero_serie, String numero_inicial, String numero_final, String numero_actual) {
        this.idserie = idserie;
        this.tipo_documento = tipo_documento;
        this.numero_serie = numero_serie;
        this.numero_inicial = numero_inicial;
        this.numero_final = numero_final;
        this.numero_actual = numero_actual;
    }

    public Long getIdserie() {
        return idserie;
    }

    public void setIdserie(Long idserie) {
        this.idserie = idserie;
    }

    public Short getTipo_documento() {
        return tipo_documento;
    }

    public void setTipo_documento(Short tipo_documento) {
        this.tipo_documento = tipo_documento;
    }

    public String getNumero_serie() {
        return numero_serie;
    }

    public void setNumero_serie(String numero_serie) {
        this.numero_serie = numero_serie;
    }

    public String getNumero_inicial() {
        return numero_inicial;
    }

    public void setNumero_inicial(String numero_inicial) {
        this.numero_inicial = numero_inicial;
    }

    public String getNumero_final() {
        return numero_final;
    }

    public void setNumero_final(String numero_final) {
        this.numero_final = numero_final;
    }

    public String getNumero_actual() {
        return numero_actual;
    }

    public void setNumero_actual(String numero_actual) {
        this.numero_actual = numero_actual;
    }

}
