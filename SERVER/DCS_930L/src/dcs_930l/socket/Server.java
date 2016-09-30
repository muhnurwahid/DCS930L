/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcs_930l.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dhan
 */
public class Server extends Thread {

    ServerSocket serverSocket = null;
    Socket clientSocket = null;

    int port;
    String url;
    String nama;
    String[] account;
    public Server(String nama, int port, String url,String[] account) {
        this.nama = nama;
        this.port = port;
        this.url = url;
        this.account=account;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getURL() {
        return url;
    }

    public int getPORT() {
        return port;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                clientSocket = serverSocket.accept();
                new ServerHandler(clientSocket, url,account).start();
            } catch (IOException ex) {
                break;
            }
        }
    }

    public void finsih() {
        if (clientSocket != null) {
            try {
                clientSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
