/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.utilities;

/**
 *
 * @author Marc Vilchez
 */
public class ConstantsTIENDA {

    public final static String LOOKUP = "java:comp/env/";
    public final static String PRE_SCHEMA = "rs_";
    public final static String SCHEMA_MANAGER = "rs_manager";
    public final static String FIELD_HEADER_DOCUMENT = "documento";

    public static String getSchemeDB(String ruc) {
        return PRE_SCHEMA + ruc;
    }
    
    
}
