/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcs_930l.video;

import dcs_930l.socket.MyAuthenticator;
import dcs_930l.view.MainView;
import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Dhan
 */
public class RecordIpCameraThread extends Thread {
    long SIZE = 500000000L;//1 MB 1.000.000 BYTES, 1000MB = 1 GB; DEFAULT 500 MB
    Date date;
    Calendar cal;
    SimpleDateFormat formatter;
    SimpleDateFormat formatter1;
    String old_time;
    String new_time;
    RecordIPCamera rc;
    int start;
    String name;
    String url;
    String[] account;
    MainView mainView;

    boolean started = true;

    public RecordIpCameraThread(MainView mainView, String name, String url, String[] account) {
        date = new Date();
        cal = Calendar.getInstance();
        formatter = new SimpleDateFormat("mm");
        formatter1 = new SimpleDateFormat("ss");
        old_time = formatter.format(cal.getTime());
        new_time = formatter.format(cal.getTime());
        start = Integer.parseInt(formatter1.format(cal.getTime()));
        this.name = name;
        this.url = url;
        this.account = account;
        this.mainView = mainView;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    @Override
    public void run() {
        File videoFile = new File("video/"+name);
        File imageFile = new File("image/"+name);
        try {
            Authenticator.setDefault(new MyAuthenticator(account[0], account[1]));
            URL urle = new URL(url);
            urle.openStream();
            rc = new RecordIPCamera(mainView, name, url, account);
            rc.recordFile(start);
            while (started) {
                if (rc.getStatusRecorded() == false) {
                    started = false;
                    break;
                }                
                if (videoFile.isDirectory()) {
                    long fileSize = dcs_930l.DCS_930L.getSize(videoFile.getPath());
                    System.out.println(dcs_930l.DCS_930L.getSize(videoFile.getPath()) + ":" + SIZE);
                    if (fileSize >= SIZE) {
                        System.out.println("DELETE");
                        dcs_930l.DCS_930L.walk(videoFile.getPath());//video
                        dcs_930l.DCS_930L.walk(imageFile.getPath());//image                  
                    }
                }
                
                date = new Date();
                cal.setTimeInMillis(date.getTime());
                new_time = formatter.format(cal.getTime());
                if (!old_time.equals(new_time)) {
                    System.out.println("START");
                    old_time = new_time;
                    start = Integer.parseInt(formatter1.format(cal.getTime()));
                    rc.recordFile(start);
                }
            }
        } catch (MalformedURLException ex) {
            JOptionPane.showMessageDialog(null, "HOST NOT FOUND " + name);
            mainView.deleteCamIfNotFound(url, name);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Cannot Record Ip Camera " + name);
            mainView.deleteCamIfNotFound(url, name);
        }
    }
}
