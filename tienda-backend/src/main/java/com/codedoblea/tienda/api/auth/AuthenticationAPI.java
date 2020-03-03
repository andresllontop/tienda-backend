/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.api.auth;

import com.codedoblea.tienda.security.filter.RestSecurityFilter;
import com.codedoblea.tienda.dao.IUsuarioDAO;
import com.codedoblea.tienda.dao.impl.UsuarioDAOImpl;
import com.codedoblea.tienda.model.Usuario;
import com.codedoblea.tienda.utilities.DataSourceTIENDA;
import com.codedoblea.tienda.utilities.StringEncrypt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Logger;
import javax.inject.Singleton;
import javax.sql.DataSource;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author andres
 */
@Singleton
@Path("/authentication")
public class AuthenticationAPI {

    private static final Logger LOG = Logger.getLogger(AuthenticationAPI.class.getName());
    private final DataSource pool;
    private final IUsuarioDAO usuarioDAO;

    private final String key = "92AE31A79FEEB2A3"; //llave-parametro 1 
    private final String iv = "0123456789ABCDEF"; // vector de inicialización parametro 2

    public AuthenticationAPI() {
        this.pool = DataSourceTIENDA.getPool();
        this.usuarioDAO = new UsuarioDAOImpl(this.pool);
    }

    @Path("/login")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(@FormParam("login") String login,
            @FormParam("password") String password) throws Exception {
        Usuario usuario;
        HashMap<String, Object> JSON = new HashMap<>();
        String message_server = "";
        LOG.info(login.toString());
        LOG.info(password.toString());
        usuario = this.usuarioDAO.getUserForLogin(login);
        if (usuario != null) {
            if (StringEncrypt.decrypt(key, iv, usuario.getPass()).equals(password)) {
                switch (usuario.getEstado()) {
                    case 1:
                        //HABILITADO
                        message_server = "ok";
                        break;
                    case 2:
                        //DESABILITADO
                        JSON.put("type_message", "3");
                        message_server = "Acceso denegado. El Usuario ingresado está inhabilitado.";
                        break;
                }
                if (message_server.equals("ok")) {
                    JSON.put("token", issueToken(usuario));
                    JSON.put("usuario", usuario);
                    LOG.info(usuario.toString());
                }
            } else {
                JSON.put("type_message", "2");
                message_server = "La Contraseña ingresada es incorrecta.";
            }
        } else {
            JSON.put("type_message", "1");
            message_server = "El Usuario ingresado no existe.";
        }
        JSON.put("message_server", message_server);
        LOG.info(JSON.toString());
        return Response.status(Response.Status.OK).entity(JSON).build();
    }

    @Path("/reset")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response reset(@FormParam("login") String login,
            @FormParam("password") String password) throws Exception {
        Usuario usuario;
        HashMap<String, Object> JSON = new HashMap<>();
        String message_server = "";
        if (this.usuarioDAO.resetClave(login, StringEncrypt.encrypt(key, iv, password)).equals("ok")) {
             message_server = "ok";
        } else {
            message_server = "No se Actualizó la contraseña.";
        }

        JSON.put("message_server", message_server);
        LOG.info(JSON.toString());
        return Response.status(Response.Status.OK).entity(JSON).build();
    }

    private String issueToken(Usuario usuario) {
        String jwtToken = Jwts.builder()
                .claim("user_name", usuario.getUsuario())
                .claim("type_user_redpos", usuario.getTipo_usuario())
                //.claim("resources", usuario.getResources())
                //.claim("roles", "")
                .setSubject(usuario.getIdusuario().toString())
                .setIssuer("http://localhost/tienda-frontend/")
                .setIssuedAt(new Date())
                .setExpiration(toDate(LocalDateTime.now().plusMinutes(720)))
                .signWith(SignatureAlgorithm.HS512, RestSecurityFilter.KEY)
                .compact();
        return jwtToken;
    }

    private Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
