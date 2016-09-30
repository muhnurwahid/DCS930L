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
public class ServerHandler extends Thread {

    Socket socket;
    MjpegInputStream mis;
    MjpegFrame frame;
    URL _url;

    public ServerHandler(Socket socket, String url, String[] account) {
        this.socket = socket;
        Authenticator.setDefault(new MyAuthenticator(account[0], account[1]));
        try {
            _url = new URL(url);

        } catch (MalformedURLException ex) {
            Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            mis = new MjpegInputStream(_url.openStream());
            while (true) {
                try {
                    while ((frame = mis.readMjpegFrame()) != null) {
                        socket.getOutputStream().write(frame.getJpegBytes());
                        socket.getOutputStream().flush();
                    }
                    mis.close();
                } catch (IOException ex) {
                    try {
                        socket.close();
                        mis.close();
                    } catch (IOException ex1) {
                        Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Error reading video : "+ex.getMessage());
        }

    }
}
