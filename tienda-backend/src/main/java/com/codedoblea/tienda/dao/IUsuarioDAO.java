/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.dao;

import com.codedoblea.tienda.utilities.BeanCrud;
import com.codedoblea.tienda.model.Usuario;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author andres
 */
public interface IUsuarioDAO extends CRUD<Usuario> {

    Usuario getUserForLogin(String login) throws SQLException;

    Long addUser(Usuario usuario, Connection conn) throws SQLException;

    BeanCrud updatePerfil(Usuario usuario) throws SQLException;
    
    String updateFotoPerfil(Usuario usuario) throws SQLException;
    
    String resetClave(String login, String encript) throws SQLException;
    
}
