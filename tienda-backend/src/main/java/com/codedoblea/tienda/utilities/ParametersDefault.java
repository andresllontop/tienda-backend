/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ogbu.unprg.sisbu.utilities;

import java.util.HashMap;

/**
 *
 * @author JamesCarrillo
 */
public class ParametersDefault {

    /**
     *
     * CLASE PARA EL REGISTRO DE PARÁMETROS DE PAGINACIÓN POR DEFECTO POR CADA
     * TABLA CREAR UN MÉTODO CON EL NOMBRE getParametersTabla()
     *
     * @return
     */
    public static HashMap<String, Object> getParametersDefault() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("FILTER", "");
        parameters.put("SQL_ORDERS", " ORDER BY NOMBRE ASC ");
        parameters.put("SQL_PAGINATION", " LIMIT 10 OFFSET 0");
        return parameters;
    }

    public static HashMap<String, Object> getParametersDefaultUsuario() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("FILTER", "");
        parameters.put("SQL_ORDERS", " ORDER BY LOGIN ASC ");
        parameters.put("SQL_PAGINATION", " LIMIT 10 OFFSET 0");
        return parameters;
    }

    public static HashMap<String, Object> getParametersDefaultPersonal() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("DNI", "");
        parameters.put("ESTADO", -1);
        parameters.put("SQL_ORDERS", " ORDER BY NOMBRE ASC ");
        parameters.put("SQL_PAGINATION", " LIMIT 10 OFFSET 0");
        return parameters;
    }

    public static HashMap<String, Object> getParametersDefaultComida() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("FILTER", "");
        parameters.put("SQL_ORDERS", " ORDER BY DESCRIPCION ASC ");
        parameters.put("SQL_PAGINATION", " LIMIT 10 OFFSET 0");
        return parameters;
    }

    public static HashMap<String, Object> getParametersDefaultMenuSemanal() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("FILTER_FECHAI", "");
        parameters.put("FILTER_FECHAF", "");
        parameters.put("SQL_ORDERS", " ORDER BY FECHAI ASC ");
        parameters.put("SQL_PAGINATION", " LIMIT 10 OFFSET 0");
        return parameters;
    }

    public static HashMap<String, Object> getParametersDefaultMenuDetalle() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("FILTER", null);
        parameters.put("SQL_ORDERS", " ORDER BY INDICE ASC ");
        parameters.put("SQL_PAGINATION", " LIMIT 10 OFFSET 0");
        return parameters;
    }
    
    public static HashMap<String, Object> getParametersDefaultCA() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("FILTER", "");
        parameters.put("SQL_ORDERS", " ORDER BY NOMBRE DESC ");
        parameters.put("SQL_PAGINATION", " LIMIT 10 OFFSET 0");
        return parameters;
    }

}
