/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcs_930l.entity;

/**
 *
 * @author Dhan
 */
public class Ipcam extends MyEntity {

    private String ipcam_id;
    private String ipcam_name;
    private String ipcam_url;
    private int ipcam_port_listening;
    private String ipcam_status;
    private String username;
    private String password;

    public String getIpcam_id() {
        return ipcam_id;
    }

    public void setIpcam_id(String ipcam_id) {
        this.ipcam_id = ipcam_id;
    }

    public String getIpcam_name() {
        return ipcam_name;
    }

    public void setIpcam_name(String ipcam_name) {
        this.ipcam_name = ipcam_name;
    }

    public String getIpcam_url() {
        return ipcam_url;
    }

    public void setIpcam_url(String ipcam_url) {
        this.ipcam_url = ipcam_url;
    }

    public int getIpcam_port_listening() {
        return ipcam_port_listening;
    }

    public void setIpcam_port_listening(int ipcam_port_listening) {
        this.ipcam_port_listening = ipcam_port_listening;
    }

    public String getIpcam_status() {
        return ipcam_status;
    }

    public void setIpcam_status(String ipcam_status) {
        this.ipcam_status = ipcam_status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
