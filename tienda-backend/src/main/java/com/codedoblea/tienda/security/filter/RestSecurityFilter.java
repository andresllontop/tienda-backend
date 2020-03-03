package com.codedoblea.tienda.security.filter;
        
import com.codedoblea.tienda.security.annotation.Secured;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.impl.crypto.MacProvider;
import java.io.IOException;
import java.security.Key;
import java.util.logging.Logger;
import javax.annotation.Priority;
import javax.sql.DataSource;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.ext.Provider;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author andres
 */
@Provider
@Secured
@Priority(Priorities.AUTHENTICATION)
public class RestSecurityFilter implements ContainerRequestFilter {

    private static final Logger LOG = Logger.getLogger(RestSecurityFilter.class.getName());
    public static final Key KEY = MacProvider.generateKey();

    private DataSource pool;
     public RestSecurityFilter() {
        LOG.info("INICIALIZANDO TIENDA SECURITY");
       
    }

    @Override
    public void filter(ContainerRequestContext request) throws IOException {
        try {
            LOG.info("Inicializando Filter");
            
            String authorizationHeader = request.getHeaderString(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring("Bearer".length()).trim();
                Jws<Claims> claims = Jwts.parser().setSigningKey(KEY).parseClaimsJws(token);
                LOG.info("ok 1"+claims.toString());
            } else {
                 LOG.info("no autorizado");
                throw new NotAuthorizedException("Bearer");
            }
        } catch (ExpiredJwtException | MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException e) {
            LOG.info("Ingresó exception token");
            throw new NotAuthorizedException("Bearer");
        }
    }

  

}
