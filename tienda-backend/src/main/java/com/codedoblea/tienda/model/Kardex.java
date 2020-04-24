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
public class Kardex {

    private Long idkardex;
    private Integer existencia;
    private Double valor;
    private Short tipo_operacion;
    /*
    1=ENTRADA
    2=SALIDA
    */
    private Entrada entrada;
    private Salida salida;

    public Kardex() {
    }

    public Kardex(Long idkardex, Integer existencia, Double valor,
            Short tipo_operacion, Entrada entrada, Salida salida) {
        this.idkardex = idkardex;
        this.existencia = existencia;
        this.valor = valor;
        this.tipo_operacion = tipo_operacion;
        this.entrada = entrada;
        this.salida = salida;
    }

    public Kardex(Long idkardex) {
        this.idkardex = idkardex;
    }

    public Long getIdkardex() {
        return idkardex;
    }

    public void setIdkardex(Long idkardex) {
        this.idkardex = idkardex;
    }

    public Integer getExistencia() {
        return existencia;
    }

    public void setExistencia(Integer existencia) {
        this.existencia = existencia;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Short getTipo_operacion() {
        return tipo_operacion;
    }

    public void setTipo_operacion(Short tipo_operacion) {
        this.tipo_operacion = tipo_operacion;
    }

    public Entrada getEntrada() {
        return entrada;
    }

    public void setEntrada(Entrada entrada) {
        this.entrada = entrada;
    }

    public Salida getSalida() {
        return salida;
    }

    public void setSalida(Salida salida) {
        this.salida = salida;
    }

}
