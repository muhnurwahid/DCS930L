/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcs_930l.model;

import dcs_930l.config.Koneksi;
import dcs_930l.entity.Users;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dhan
 */
public class M_Users extends MyModel {

    public boolean insert(Users user) {
        boolean status_insert = false;
        String query = "insert into users(user_id,user_name,user_pass,created_at) values(?,?,?,?)";
        PreparedStatement ps = null;
        try {
            ps = Koneksi.getKoneksi().prepareStatement(query);
            ps.setString(1, user.getUser_id());
            ps.setString(2, user.getUser_name());
            ps.setString(3, user.getUser_pass());
            ps.setTimestamp(4, user.getCreated_at());
            status_insert = ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(M_Users.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                    Koneksi.getKoneksi().close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(M_Users.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return status_insert;
    }

    public boolean update(Users user) {
        boolean status_update = false;
        String query = "update users set user_name=?,user_pass=?,updated_at=? where user_id=?";
        PreparedStatement ps = null;
        try {
            ps = Koneksi.getKoneksi().prepareStatement(query);
            ps.setString(1, user.getUser_name());
            ps.setString(2, user.getUser_pass());
            ps.setTimestamp(3, user.getUpdated_at());
            ps.setString(4, user.getUser_id());
            status_update = ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(M_Users.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                    Koneksi.getKoneksi().close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(M_Users.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return status_update;
    }

    public static boolean cek_login(String username, String password) {
        boolean login = false;
        try {
            Statement select = Koneksi.getKoneksi().createStatement();
            ResultSet rs = select.executeQuery("select * from users where user_name='" + username + "' and user_pass='" + password + "'");
            while (rs.next()) {               
                login = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(M_Users.class.getName()).log(Level.SEVERE, null, ex);
        }
        return login;
    }
}
