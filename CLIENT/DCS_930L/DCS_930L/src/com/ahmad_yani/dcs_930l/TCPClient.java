package com.ahmad_yani.dcs_930l;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import android.util.Log;

public class TCPClient {

	private String serverMessage;
	public static String SERVERIP = "192.168.8.101";
	public static int SERVERPORT = 1990;
	private OnMessageReceived mMessageListener = null;
	private boolean mRun = false;
	private PrintWriter out = null;
	private BufferedReader in = null;
	Socket socket;
	
	public TCPClient(final OnMessageReceived listener) {
		mMessageListener = listener;
	}

	public boolean sendMessage(String message) {
		if (out != null && !out.checkError()) {
			out.println(message);
			out.flush();
			return true;
		}else{
			return false;
		}
	}

	public void stopClient() {
		mRun = false;
	}

	public void run() {
		try {
			InetAddress serverAddr = InetAddress.getByName(SERVERIP);			
			socket = new Socket(serverAddr, SERVERPORT);
			try {
				out = new PrintWriter(new BufferedWriter(
						new OutputStreamWriter(socket.getOutputStream())), true);
				in = new BufferedReader(new InputStreamReader(
						socket.getInputStream()));
				mRun=true;
				while (mRun) {
					serverMessage = in.readLine();
					if (serverMessage != null && mMessageListener != null) {
						mMessageListener.messageReceived(serverMessage);
					}
					serverMessage = null;
				}
			} catch (Exception e) {
				Log.e("TCP SI Error", "SI: Error", e);
				e.printStackTrace();
			} finally {
				socket.close();
			}

		} catch (Exception e) {
			Log.e("TCP SI Error", "SI: Error", e);
		}
	}
	
	public interface OnMessageReceived {
		public void messageReceived(String message);
	}
	
	public Socket getSocket(){
		return socket;
	}
}