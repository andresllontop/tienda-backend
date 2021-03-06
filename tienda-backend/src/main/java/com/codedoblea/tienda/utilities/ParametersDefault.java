/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.utilities;

import java.util.HashMap;

/**
 *
 * @author andres
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
    public static HashMap<String, Object> getParametersDefaultTipoDocumento() {
        HashMap<String, Object> parameters = new HashMap<>();
        parameters.put("FILTER", "");
        parameters.put("SQL_ORDERS", " ORDER BY TIPO_DOCUMENTO ASC ");
        parameters.put("SQL_PAGINATION", " LIMIT 10 OFFSET 0");
        return parameters;
    }

    

}
