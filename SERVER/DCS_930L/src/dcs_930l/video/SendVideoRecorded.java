/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcs_930l.video;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dhan
 */
public class SendVideoRecorded extends Thread {

    int SOCKET_PORT = 13267;
    String FILE_TO_SEND;
    FileInputStream fis = null;
    BufferedInputStream bis = null;
    OutputStream os = null;
    ServerSocket servsock = null;
    Socket sock = null;
    boolean finish = true;

    public SendVideoRecorded(String FILE_TO_SEND) {
        this.FILE_TO_SEND = FILE_TO_SEND;        
    }

    @Override
    public void run() {
        try {
            servsock = new ServerSocket(SOCKET_PORT);
            try {
            while (finish) {
                System.out.println("Waiting...");
                try {
                    sock = servsock.accept();
                    System.out.println("Accepted connection : " + sock);
                    File myFile = new File(FILE_TO_SEND);
                    byte[] mybytearray = new byte[(int) myFile.length()];
                    fis = new FileInputStream(myFile);
                    bis = new BufferedInputStream(fis);
                    bis.read(mybytearray, 0, mybytearray.length);
                    os = sock.getOutputStream();
                    System.out.println("Sending " + FILE_TO_SEND + "(" + mybytearray.length + " bytes)");
                    os.write(mybytearray, 0, mybytearray.length);
                    os.flush();
                    finish = false;
                    break;
                } catch (IOException exe) {   
                    System.out.println("Terjadi Kesalahan : "+exe.getMessage());
                    finish = false;
                    break;
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException ex) {
                            Logger.getLogger(SendVideoRecorded.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (os != null) {
                        try {
                            os.close();
                        } catch (IOException ex) {
                            Logger.getLogger(SendVideoRecorded.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (sock != null) {
                        try {
                            sock.close();
                        } catch (IOException ex) {
                            Logger.getLogger(SendVideoRecorded.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        } finally {
            if (servsock != null) {
                try {
                    servsock.close();
                } catch (IOException ex) {
                    Logger.getLogger(SendVideoRecorded.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        } catch (IOException ex) {
            System.out.println("Server port already in USE : "+ex.getMessage());
        }        
    }
}
