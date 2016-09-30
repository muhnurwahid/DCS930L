/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcs_930l.socket;

import dcs_930l.model.M_Ipcam;
import dcs_930l.model.M_Users;
import dcs_930l.video.SendVideoRecorded;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dhan
 */
public class ClientHandler extends Thread {

    PrintWriter out;
    BufferedReader in;
    Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        System.out.println("Accepted New Client : " + socket.getInetAddress().getHostAddress());
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        out.println("#LIST#"+M_Ipcam.getSingleListCam());
        out.flush();
        while (true) {
            try {
                String input = in.readLine();
                if (input == null) {
                    return;
                }
                if (input.startsWith("#LOGIN#")) {
                    int length = "#LOGIN#".length();
                    String msg = input.substring(length, input.length());
                    String[] array = msg.split("@");
                    if (array.length == 2) {
                        if (M_Users.cek_login(array[0],array[1])) {
                            out.println("#LOGIN#true@Success Login");
                        } else {
                            out.println("#LOGIN#false@Failed Login");
                        }
                    } else {
                        out.println("#LOGIN#3#false@Request Not Found");
                    }
                }else if(input.equals("#LISTALLREKAMAN#")){
                    out.println("#LISTALLREKAMAN#"+M_Ipcam.getAllListRekaman());
                }else if(input.startsWith("#LISTDATEREKAMAN#")){
                    int length = "#LISTDATEREKAMAN#".length();
                    String camName = input.substring(length, input.length());
                    out.println("#LISTDATEREKAMAN#"+M_Ipcam.getDateListRekaman(camName));
                }else if(input.startsWith("#LISTTIMEREKAMAN#")){
                    int length = "#LISTTIMEREKAMAN#".length();
                    String msg = input.substring(length, input.length());
                    String[] array = msg.split("@");
                    out.println("#LISTTIMEREKAMAN#"+M_Ipcam.getTimeListRekaman(array[0], array[1]));
                }else if(input.startsWith("#PLAYREKAMAN#")){
                    int length = "#PLAYREKAMAN#".length();
                    String file_path = input.substring(length, input.length());
                    new SendVideoRecorded(file_path).start();
                    String[] path = file_path.split("/");           
                    out.println("#PLAYREKAMAN#"+path[path.length-1]+"@"+M_Ipcam.getFileSize(file_path));
                }else {
                    if (input.startsWith("#START#")) {
                        int length = "#START#".length();
                        String camName = input.substring(length, input.length());
                        int getPort = M_Ipcam.getPortListening(camName);
                        String response = "#START#"+camName+"@"+getPort;
                        out.println(response);
                    }else{
                        System.out.println("Request : "+input);
                        System.out.println("Request Not Found");
                    }
                }
                out.flush();
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
                break;
            }
        }        
    }
}
