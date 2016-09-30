/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcs_930l.model;

import dcs_930l.config.Koneksi;
import dcs_930l.entity.Ipcam;
import java.io.File;
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
public class M_Ipcam extends MyModel {

    public boolean insert(Ipcam ipcam) {
        boolean status_insert = false;
        String query = "insert into ipcam(ipcam_id,ipcam_name,ipcam_url,ipcam_port_listening,ipcam_status,username,password,created_at) values(?,?,?,?,?,?,?,?)";
        PreparedStatement ps = null;
        try {
            ps = Koneksi.getKoneksi().prepareStatement(query);
            ps.setString(1, ipcam.getIpcam_id());
            ps.setString(2, ipcam.getIpcam_name());
            ps.setString(3, ipcam.getIpcam_url());
            ps.setInt(4, ipcam.getIpcam_port_listening());
            ps.setString(5, ipcam.getIpcam_status());
            ps.setString(6, ipcam.getUsername());
            ps.setString(7, ipcam.getPassword());
            ps.setTimestamp(8, ipcam.getCreated_at());
            status_insert = ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(M_Ipcam.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                    Koneksi.getKoneksi().close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(M_Ipcam.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return status_insert;
    }

    public boolean update(Ipcam ipcam) {
        boolean status_update = false;
        String query = "update ipcam set ipcam_name=?,ipcam_url=?,ipcam_port_listening=?,ipcam_status=?,username=?,password=?,updated_at=? where ipcam_id=?";
        PreparedStatement ps = null;
        try {
            ps = Koneksi.getKoneksi().prepareStatement(query);
            ps.setString(1, ipcam.getIpcam_name());
            ps.setString(2, ipcam.getIpcam_url());
            ps.setInt(3, ipcam.getIpcam_port_listening());
            ps.setString(4, ipcam.getIpcam_status());
            ps.setString(5, ipcam.getUsername());
            ps.setString(6, ipcam.getPassword());
            ps.setTimestamp(7, ipcam.getUpdated_at());
            ps.setString(8, ipcam.getIpcam_id());
            status_update = ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(M_Ipcam.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                    Koneksi.getKoneksi().close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(M_Ipcam.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return status_update;
    }

    public boolean updateStatus(Ipcam ipcam) {
        boolean status_update = false;
        String query = "update ipcam set ipcam_status=?,updated_at=? where ipcam_id=?";
        PreparedStatement ps = null;
        try {
            ps = Koneksi.getKoneksi().prepareStatement(query);
            ps.setString(1, ipcam.getIpcam_status());
            ps.setTimestamp(2, ipcam.getUpdated_at());
            ps.setString(3, ipcam.getIpcam_id());
            status_update = ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            Logger.getLogger(M_Ipcam.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                    Koneksi.getKoneksi().close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(M_Ipcam.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return status_update;
    }

    public static String getSingleListCam() {
        String listCam = "";
        try {
            Statement select = Koneksi.getKoneksi().createStatement();
            ResultSet rs = select.executeQuery("select ipcam_name from ipcam where ipcam_status='Started'");
            while (rs.next()) {
                listCam += rs.getString(1) + "@";
            }
            if (listCam.length() > 0) {
                listCam = listCam.substring(0, listCam.length() - 1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(M_Users.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listCam;
    }

    public static String getAllListRekaman() {
        String listCam = "";
        File file = new File("video");
        String[] myFiles;
        if (file.isDirectory()) {
            myFiles = file.list();
            for (String myFile1 : myFiles) {
                File myFile = new File(file, myFile1);
                if (myFile.isDirectory()) {
                    listCam += myFile.getName() + "@";
                }
            }
        }
        listCam = listCam.substring(0, listCam.length() - 1);
        return listCam;
    }

    public static String getDateListRekaman(String camName) {
        String listDate = "";
        File file = new File("video/" + camName);
        String[] myFiles;
        if (file.isDirectory()) {
            myFiles = file.list();
            for (String myFile1 : myFiles) {
                File myFile = new File(file, myFile1);
                if (myFile.isDirectory()) {
                    listDate += myFile.getName() + "@";
                }
            }
        }
        if (listDate.length() > 0) {
            listDate = listDate.substring(0, listDate.length() - 1);
        }
        return listDate;
    }

    public static String getTimeListRekaman(String camName, String date) {
        String listTime = "";
        File file = new File("video/" + camName + "/" + date);
        String[] myFiles;
        if (file.isDirectory()) {
            myFiles = file.list();
            for (String myFile1 : myFiles) {
                File myFile = new File(file, myFile1);
                if (myFile.isFile()) {
                    listTime += myFile.getName() + "@";
                }
            }
        }
        listTime = listTime.substring(0, listTime.length() - 1);
        return listTime;
    }

    public static long getFileSize(String filepath) {
        File file = new File(filepath);
        return file.length();
    }

    public static int getPortListening(String camName) {
        int portListening = 10000;
        try {
            Statement select = Koneksi.getKoneksi().createStatement();
            ResultSet rs = select.executeQuery("select ipcam_port_listening from ipcam where ipcam_name='" + camName + "'");
            while (rs.next()) {
                portListening = rs.getInt(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(MyModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        return portListening;
    }
    
}
