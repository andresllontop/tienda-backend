/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.utilities;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author Andres
 */
public class DataSourceTIENDA {

    private static final Logger LOG = Logger.getLogger(DataSourceTIENDA.class.getName());

    public static DataSource getPool() {
        DataSource pool = null;
        try {
            InitialContext cxt = new InitialContext();
            pool = (DataSource) cxt.lookup(ConstantsTIENDA.LOOKUP + "jdbc/dbtienda");
            LOG.info(ConstantsTIENDA.LOOKUP);
            if (pool != null) {
                LOG.info("DataSource TIENDA Inicializado exitosamente");
            } else {
                LOG.info("Error al Inicializar DataSource TIENDA");
            }
        } catch (NamingException ex) {
            Logger.getLogger(DataSourceTIENDA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return pool;
    }
}
