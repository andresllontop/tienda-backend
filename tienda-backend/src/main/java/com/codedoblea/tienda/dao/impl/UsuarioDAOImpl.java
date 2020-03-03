/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codedoblea.tienda.dao.impl;

import com.codedoblea.tienda.dao.IUsuarioDAO;
import com.codedoblea.tienda.dao.SQLCloseable;
import com.codedoblea.tienda.model.Perfil;
import com.codedoblea.tienda.model.Usuario;
import com.codedoblea.tienda.utilities.BeanCrud;
import com.codedoblea.tienda.utilities.BeanPagination;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author andres
 */
public class UsuarioDAOImpl implements IUsuarioDAO {

    private static final Logger LOG = Logger.getLogger(UsuarioDAOImpl.class.getName());

    private DataSource pool;

    public UsuarioDAOImpl() {
    }

    public UsuarioDAOImpl(DataSource pool) {
        this.pool = pool;
    }

    @Override
    public BeanPagination getPagination(HashMap<String, Object> parameters, Connection conn)
            throws SQLException {
        BeanPagination beanPagination = new BeanPagination();
        List<Usuario> list = new ArrayList<>();
        PreparedStatement pst;
        ResultSet rs;
        try {
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(IDUSUARIO) AS COUNT FROM ");
            sSQL.append("`usuario` WHERE ");
            sSQL.append("LOGIN = ? ");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setString(1, String.valueOf(parameters.get("FILTER")));
            LOG.info(pst.toString());
            rs = pst.executeQuery();
            while (rs.next()) {
                beanPagination.setCount_filter(rs.getLong("COUNT"));
                if (rs.getInt("COUNT") == 1) {
                    sSQL.setLength(0);
                    sSQL.append("SELECT US.*,");
                    sSQL.append("PE.NOMBRE AS PER_NOMBRE ");
                    sSQL.append("FROM `usuario` AS US ");
                    sSQL.append("LEFT JOIN `perfil` AS PE ON PE.IDPERFIL=US.IDPERFIL ");
                    sSQL.append("WHERE ");
                    sSQL.append("LOGIN = ? ");
                    sSQL.append(String.valueOf(parameters.get("SQL_ORDERS")));
                    sSQL.append(parameters.get("SQL_PAGINATION"));
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setString(1, String.valueOf(parameters.get("FILTER")));
                    LOG.info(pst.toString());
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        Usuario usuario = new Usuario();
                        usuario.setIdusuario(rs.getLong("IDUSUARIO"));
                        usuario.setUsuario(rs.getString("USUARIO"));
                        usuario.setLogin(rs.getString("LOGIN"));
                        usuario.setPass(rs.getString("PASS"));
                        usuario.setEstado(rs.getInt("ESTADO"));
                        usuario.setTipo_usuario(rs.getShort("TIPO_USUARIO"));
                        usuario.setFoto(rs.getString("FOTO"));
                        usuario.setPerfil(new Perfil(rs.getInt("IDPERFIL"), rs.getString("PER_NOMBRE")));
                        list.add(usuario);
                    }
                    beanPagination.setList(list);
                } 
            }

            rs.close();
            pst.close();
        } catch (SQLException ex) {
            throw ex;
        }
        return beanPagination;
    }

    @Override
    public BeanCrud getPagination(HashMap<String, Object> parameters) throws SQLException {
        BeanCrud beanCrud = new BeanCrud();
        try (Connection conn = pool.getConnection()) {
            beanCrud.setBeanPagination(getPagination(parameters, conn));
        } catch (SQLException e) {
            throw e;
        }
        return beanCrud;
    }

    @Override
    public BeanCrud add(Usuario t, HashMap<String, Object> parameters) throws SQLException {
        BeanCrud beanCrud = new BeanCrud();
        PreparedStatement pst;
        ResultSet rs;
        try (Connection conn = this.pool.getConnection(); SQLCloseable finish = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(US.IDUSUARIO) AS COUNT FROM ");
            sSQL.append("`usuario` AS US ");
            sSQL.append("LEFT JOIN `personal` as PE on PE.IDUSUARIO=US.IDUSUARIO ");
            sSQL.append("WHERE PE.DNI = ? OR LOGIN = ? ");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setString(1, t.getLogin());
            pst.setString(2, t.getLogin());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("INSERT INTO ");
                    sSQL.append("`usuario`(USUARIO, LOGIN, PASS, ESTADO, TIPO_USUARIO, FOTO, IDPERFIL, TIPO_PERFIL) VALUES(?,?,?,?,?,?,?,?)");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setString(1, t.getUsuario());
                    pst.setString(2, t.getLogin());
                    pst.setString(3, t.getPass());
                    pst.setInt(4, t.getEstado());
                    pst.setShort(5, t.getTipo_usuario());
                    pst.setString(6, t.getFoto());
                    pst.setString(7, null);
                    pst.setShort(8, t.getTipo_perfil());
                    LOG.info(pst.toString());
                    pst.executeUpdate();
                    conn.commit();
                    beanCrud.setMessageServer("ok");
                    parameters.put("FILTER", t.getLogin());
                    parameters.put("SQL_ORDERS", " ORDER BY USUARIO ASC ");
                    beanCrud.setBeanPagination(getPagination(parameters, conn));

                } else {
                    beanCrud.setMessageServer("No se registró, ya existe un Usuario con el DNI ingresado");
                }
            }
            rs.close();
            pst.close();

        } catch (SQLException e) {
            throw e;
        }
        return beanCrud;
    }

    @Override
    public BeanCrud update(Usuario t, HashMap<String, Object> parameters) throws SQLException {
        BeanCrud beanCrud = new BeanCrud();
        PreparedStatement pst;
        ResultSet rs;
        try (Connection conn = this.pool.getConnection(); SQLCloseable finish = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(IDUSUARIO) AS COUNT FROM ");
            sSQL.append("`usuario` WHERE LOGIN = ? AND IDUSUARIO != ?");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setString(1, t.getLogin());
            pst.setLong(2, t.getIdusuario());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("UPDATE ");
                    sSQL.append("`usuario` SET USUARIO = ?, LOGIN = ?, PASS = ?, ");
                    sSQL.append("ESTADO = ?, TIPO_USUARIO = ?, FOTO = ?, ");
                    sSQL.append("TIPO_PERFIL = ? ");
                    sSQL.append("WHERE IDUSUARIO = ?");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setString(1, t.getUsuario());
                    pst.setString(2, t.getLogin());
                    pst.setString(3, t.getPass());
                    pst.setInt(4, t.getEstado());
                    pst.setShort(5, t.getTipo_usuario());
                    pst.setString(6, t.getFoto());
                    pst.setInt(7, t.getTipo_perfil());
                    pst.setLong(8, t.getIdusuario());
                    LOG.info(pst.toString());
                    pst.executeUpdate();
                    conn.commit();
                    beanCrud.setMessageServer("ok");
                } else {
                    beanCrud.setMessageServer("No se modificó, ya existe un Usuario con el nombre ingresado");
                }
            }
            rs.close();
            pst.close();

        } catch (SQLException e) {
            throw e;
        }
        return beanCrud;
    }

    @Override
    public BeanCrud delete(Long id, HashMap<String, Object> parameters) throws SQLException {
        BeanCrud beanCrud = new BeanCrud();
        PreparedStatement pst;
        ResultSet rs;
        try (Connection conn = this.pool.getConnection(); SQLCloseable finish = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(IDUSUARIO) AS COUNT FROM ");
            sSQL.append("`personal` WHERE IDUSUARIO = ?");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setInt(1, id.intValue());
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("DELETE FROM ");
                    sSQL.append("`usuario` WHERE IDUSUARIO = ?");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setInt(1, id.intValue());
                    LOG.info(pst.toString());
                    pst.executeUpdate();
                    conn.commit();
                    beanCrud.setMessageServer("ok");
                    beanCrud.setBeanPagination(getPagination(parameters, conn));
                } else {
                    beanCrud.setMessageServer("No se eliminó, existe un Personal asociado a este Usuario");
                }
            }
            rs.close();
            pst.close();

        } catch (SQLException e) {
            throw e;
        }
        return beanCrud;
    }

    @Override
    public Usuario getForId(Long id) throws SQLException {
        Usuario usuario = null;
        PreparedStatement pst;
        ResultSet rs;
        try (Connection conn = this.pool.getConnection(); SQLCloseable finish = conn::rollback;) {
            conn.setAutoCommit(false);
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT COUNT(IDUSUARIO) AS COUNT FROM ");
            sSQL.append("`usuario` WHERE IDUSUARIO = ?");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setLong(1, id);
            rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getInt("COUNT") != 0) {
                    sSQL.setLength(0);
                    sSQL.append("SELECT US.*,");
                    sSQL.append("PE.NOMBRE AS PER_NOMBRE ");
                    sSQL.append("FROM `usuario` AS US ");
                    sSQL.append("LEFT JOIN `perfil` AS PE ON PE.IDPERFIL=US.IDPERFIL ");
                    sSQL.append("WHERE ");
                    sSQL.append("IDUSUARIO = ?");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setLong(1, id);
                    rs = pst.executeQuery();
                    while (rs.next()) {
                        usuario = new Usuario();
                        usuario.setIdusuario(rs.getLong("IDUSUARIO"));
                        usuario.setLogin(rs.getString("LOGIN"));
                        usuario.setUsuario(rs.getString("USUARIO"));
                        usuario.setTipo_usuario(rs.getShort("TIPO_USUARIO"));
                        usuario.setTipo_perfil(rs.getShort("TIPO_PERFIL"));
                        usuario.setFoto(rs.getString("FOTO"));
                        usuario.setPerfil(new Perfil(rs.getInt("IDPERFIL"), rs.getString("PER_NOMBRE")));
                        usuario.setEstado(rs.getInt("ESTADO"));
                    }

                }
            }
            rs.close();
            pst.close();

        } catch (SQLException e) {
            throw e;
        }

        return usuario;
    }

    @Override
    public Usuario getUserForLogin(String login) throws SQLException {
        Usuario usuario = null;
        try (Connection conn = this.pool.getConnection()) {
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("SELECT * FROM `usuario` where login = ?");
            try (PreparedStatement pst = conn.prepareStatement(sSQL.toString())) {
                pst.setString(1, login);
                LOG.info(sSQL.toString());
                try (ResultSet rs = pst.executeQuery()) {
                     
                    if (rs.next()) {
                        usuario = new Usuario();
                        usuario.setIdusuario(rs.getLong("IDUSUARIO"));
                        usuario.setLogin(login);
                        usuario.setUsuario(rs.getString("USUARIO"));
                        usuario.setPass(rs.getString("PASS"));
                        usuario.setTipo_usuario(rs.getShort("TIPO_USUARIO"));
                        usuario.setTipo_perfil(rs.getShort("TIPO_PERFIL"));
                        usuario.setFoto(rs.getString("FOTO"));
                        //usuario.setPerfil(new Perfil(rs.getInt("IDPERFIL")));
                        usuario.setEstado(rs.getInt("ESTADO"));
                    }
                }
            }
        } catch (SQLException e) {
            throw e;
        }
       
        return usuario;
    }

    @Override
    public Long addUser(Usuario usuario, Connection conn) throws SQLException {
        Long idusuario = -1L;
        StringBuilder sSQL = new StringBuilder();
        sSQL.append("SELECT COUNT(IDUSUARIO) AS COUNT ");
        sSQL.append("FROM `usuario` WHERE LOGIN = ?");
        PreparedStatement pst = conn.prepareStatement(sSQL.toString());
        try {
            pst.setString(1, usuario.getLogin());
            pst.executeQuery();
            LOG.info(pst.toString());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                if (rs.getInt("COUNT") == 0) {
                    sSQL.setLength(0);
                    sSQL.append("INSERT INTO ");
                    sSQL.append("`usuario`(USUARIO, LOGIN, PASS, ESTADO, TIPO_USUARIO, FOTO, IDPERFIL, TIPO_PERFIL) VALUES(?,?,?,?,?,?,?,?)");
                    pst = conn.prepareStatement(sSQL.toString());
                    pst.setString(1, usuario.getUsuario());
                    pst.setString(2, usuario.getLogin());
                    pst.setString(3, usuario.getPass());
                    pst.setInt(4, usuario.getEstado());
                    pst.setShort(5, usuario.getTipo_usuario());
                    pst.setString(6, usuario.getFoto());
                    pst.setString(7, null);
                    pst.setShort(8, usuario.getTipo_perfil());
                    LOG.info(pst.toString());
                    pst.executeUpdate();
                    sSQL.setLength(0);
                    sSQL.append("SELECT MAX(IDUSUARIO) AS IDUSUARIO FROM `usuario`");
                    pst = conn.prepareStatement(sSQL.toString());
                    rs = pst.executeQuery();
                    if (rs.next()) {
                        idusuario = rs.getLong("IDUSUARIO");
                    }
                }
            }
            rs.close();
            pst.close();

        } catch (SQLException e) {
            throw e;
        }
        return idusuario;
    }

    @Override
    public BeanCrud updatePerfil(Usuario usuario) throws SQLException {
        BeanCrud beanCrud = new BeanCrud();
        StringBuilder sSQL = new StringBuilder();
        PreparedStatement pst;
        try (Connection conn = this.pool.getConnection(); SQLCloseable finish = conn::rollback;) {
            conn.setAutoCommit(false);
            /*
            sSQL.append("SELECT COUNT(IDUSUARIO) AS COUNT FROM `usuario` WHERE LOGIN = ?");
            pst = conn.prepareStatement(sSQL.toString());
            try ( ResultSet rs = pst.executeQuery()) {

            }
             */
            sSQL.setLength(0);
            sSQL.append("UPDATE `usuario` SET ");
            sSQL.append("USUARIO = ?,");
            sSQL.append("PASS = ? ");
            sSQL.append("WHERE IDUSUARIO = ?");
            pst = conn.prepareStatement(sSQL.toString());
            pst.setString(1, usuario.getUsuario());
            pst.setString(2, usuario.getPass());
            pst.setLong(3, usuario.getIdusuario());
            pst.executeUpdate();
            conn.commit();
            beanCrud.setMessageServer("ok");
            pst.close();
        } catch (SQLException e) {
            throw e;
        }
        return beanCrud;
    }

    @Override
    public String updateFotoPerfil(Usuario usuario) throws SQLException {
        String res = "";
        StringBuilder sSQL = new StringBuilder();
        try (Connection conn = this.pool.getConnection(); SQLCloseable finish = conn::rollback;) {
            conn.setAutoCommit(false);
            sSQL.append("UPDATE `usuario` SET FOTO = ? WHERE IDUSUARIO = ?");
            try (PreparedStatement pst = conn.prepareStatement(sSQL.toString())) {
                pst.setString(1, usuario.getFoto());
                pst.setLong(2, usuario.getIdusuario());
                pst.executeUpdate();
                conn.commit();
                res = "ok";
            }
        } catch (SQLException e) {
            throw e;
        }
        return res;
    }

    @Override
    public String resetClave(String login, String encript) throws SQLException {
        String res = "ok";
        try (Connection conn = this.pool.getConnection()) {
            StringBuilder sSQL = new StringBuilder();
            sSQL.append("UPDATE `usuario` ");
            sSQL.append("SET PASS = ? WHERE LOGIN = ?");
            try (PreparedStatement pst = conn.prepareStatement(sSQL.toString())) {
                pst.setString(1, encript);
                pst.setString(2, login);
                pst.executeUpdate();
                res = "ok";
            }
        } catch (SQLException e) {
            throw e;
        }
        return res;
    }

}
