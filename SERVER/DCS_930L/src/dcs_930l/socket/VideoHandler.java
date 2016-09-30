/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcs_930l.socket;

import dcs_930l.video.MjpegFrame;
import dcs_930l.video.MjpegInputStream;
import java.io.IOException;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dhan
 */
public class VideoHandler extends Thread {

    Socket socket;
    MjpegInputStream mis;
    MjpegFrame frame;
    URL _url;

    public VideoHandler(Socket socket, String url,String username,String password) {
        this.socket = socket;
        Authenticator.setDefault(new MyAuthenticator(username,password));
        try {
            _url = new URL(url);            
        } catch (MalformedURLException ex) {
            Logger.getLogger(VideoHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
        try {
            mis = new MjpegInputStream(_url.openStream());
        } catch (IOException ex) {
            Logger.getLogger(VideoHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (true) {
            try {
                while ((frame = mis.readMjpegFrame()) != null && socket.isConnected()) {                    
                    socket.getOutputStream().write(frame.getJpegBytes());
                    socket.getOutputStream().flush();
                }
                mis.close();
            } catch (IOException ex) {
                try {
                    mis.close();
                } catch (IOException ex1) {
                    Logger.getLogger(VideoHandler.class.getName()).log(Level.SEVERE, null, ex1);
                }
            }
        }
    }    
}
