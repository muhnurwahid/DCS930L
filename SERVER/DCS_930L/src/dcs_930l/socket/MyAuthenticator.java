/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dcs_930l.socket;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 *
 * @author Dhan
 */
public class MyAuthenticator extends Authenticator {

    String username;
    String password;
    public MyAuthenticator(String username,String password){
        this.username=username;
        this.password=password;
    }
    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password.toCharArray());
    }
}
