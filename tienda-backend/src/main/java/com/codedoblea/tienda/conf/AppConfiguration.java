/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.conf;

import java.util.logging.Logger;
import javax.ws.rs.ApplicationPath;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author andres
 */
@ApplicationPath("/api")
public class AppConfiguration extends ResourceConfig {

    private static final Logger LOG = Logger.getLogger(AppConfiguration.class.getName());

    public AppConfiguration() {
        LOG.info("Inicializando Paquetes");
        packages("com.codedoblea.tienda");
        register(MultiPartFeature.class);
    }

}
