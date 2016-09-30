package com.ahmad_yani.dcs_930l;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.ahmad_yani.dcs_930l.video.MjpegInputStream;
import com.ahmad_yani.dcs_930l.video.VideoStream;

public class VideoActivity extends Activity {

	VideoStream vs;
	VideoStreamThread vst;
	Thread t;
	String camName;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_video);
		Bundle extras = getIntent().getExtras();
		camName = extras.getString("title");
		int port = Integer.parseInt(extras.getString("port"));
		vs = new VideoStream(this);
		vst = new VideoStreamThread(vs,port);
		t = new Thread(vst);
		t.start();
		setContentView(vs);
	}

	@Override
	public void onBackPressed() {
		vst.berhenti();
	}

	class VideoStreamThread implements Runnable {
		VideoStream vs;
		Socket socket;
		boolean finish = true;
		int port;
		public VideoStreamThread(VideoStream vs,int port) {
			this.vs = vs;
			this.port = port;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (finish) {
				try {
					InetAddress serverAddr = InetAddress
							.getByName(TCPClient.SERVERIP);
					socket = new Socket(serverAddr, port);
					System.out.println("Connected");
					while (true) {
						MjpegInputStream mis = new MjpegInputStream(
								socket.getInputStream());
						while (mis.readMjpegFrame() != null) {
							Bitmap bitmap = mis.readMjpegFrame();
							vs.update(bitmap,camName);
						}
						mis.close();
					}
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					berhenti();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					berhenti();
				}
			}
		}

		public void berhenti() {
			finish = false;
			if(socket!=null){
				try {
					socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			VideoActivity.this.finish();
		}
	}	
}
