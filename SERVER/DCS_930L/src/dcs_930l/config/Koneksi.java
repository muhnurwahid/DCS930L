/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcs_930l.config;

/**
 *
 * @author muhnurwahid
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author M Azhari
 */
public class Koneksi {

    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost/ipcam";
    private static final String USER = "root";
    private static final String PASS = "";
    private static Connection CONNECTION = null;

    public static Connection getKoneksi() {
        try {
            Class.forName(DRIVER);
            CONNECTION = DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException ex) {
            CONNECTION = null;
            JOptionPane.showMessageDialog(null, "Database Tidak Ditemukan");
        }
        return CONNECTION;
    }
}
