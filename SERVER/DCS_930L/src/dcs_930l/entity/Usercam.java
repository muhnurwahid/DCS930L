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
public class Usercam extends MyEntity {

    private String usercam_id;
    private Users user;
    private Ipcam ipcam;

    public String getUsercam_id() {
        return usercam_id;
    }

    public void setUsercam_id(String usercam_id) {
        this.usercam_id = usercam_id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Ipcam getIpcam() {
        return ipcam;
    }

    public void setIpcam(Ipcam ipcam) {
        this.ipcam = ipcam;
    }

}
