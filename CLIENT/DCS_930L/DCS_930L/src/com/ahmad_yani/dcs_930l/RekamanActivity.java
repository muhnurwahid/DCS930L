package com.ahmad_yani.dcs_930l;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.VideoView;

public class RekamanActivity extends Activity {

	int FILE_SIZE = 0;
	int bytesRead;
	int current = 0;
	FileOutputStream fos = null;
	BufferedOutputStream bos = null;
	Socket sock = null;
	VideoView videoPlayer;
	File videoFiles;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_rekaman);
		Bundle bundle = getIntent().getExtras();
		String file_name = bundle.getString("file_name");
		FILE_SIZE = 5000000;
		videoPlayer = (VideoView) findViewById(R.id.videoViewRekaman);
		File file = new File(Environment.getExternalStorageDirectory(),
				file_name);
		if (file.isFile()) {
			playVideo(file);
		} else {
			new VideoDownloader(file_name).execute();
		}
	}

	public void playVideo(File file) {
		Uri uri = Uri.fromFile(file);
		videoPlayer.setVideoURI(uri);
		videoPlayer.start();
	}

	@Override
	public void onBackPressed() {
		videoPlayer.stopPlayback();
		if (videoFiles != null) {
			if (videoFiles.isFile()) {
				videoFiles.delete();				
			} else {
				Toast.makeText(getApplicationContext(), "ERROR DELETE FILES",
						Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(getApplicationContext(), "CANNOT DELETE FILES",
					Toast.LENGTH_SHORT).show();
		}
		super.onBackPressed();
	}

	class VideoDownloader extends AsyncTask<String, Integer, Void> {
		String file_name;

		public VideoDownloader(String file_name) {
			this.file_name = file_name;
		}

		@Override
		protected Void doInBackground(String... params) {
			File file = new File(Environment.getExternalStorageDirectory(),
					file_name);
			try {
				InetAddress serverAddr = InetAddress
						.getByName(TCPClient.SERVERIP);
				sock = new Socket(serverAddr, 13267);
				System.out.println("Connecting...");

				// receive file
				byte[] mybytearray = new byte[FILE_SIZE];
				System.out.println("File size : " + mybytearray.length
						+ " bytes");
				InputStream is = sock.getInputStream();
				fos = new FileOutputStream(file);
				bos = new BufferedOutputStream(fos);
				bytesRead = is.read(mybytearray, 0, mybytearray.length);
				current = bytesRead;

				do {
					bytesRead = is.read(mybytearray, current,
							(mybytearray.length - current));
					if (bytesRead >= 0)
						current += bytesRead;
				} while (bytesRead > -1);

				bos.write(mybytearray, 0, current);
				bos.flush();
				System.out.println("File " + file + " downloaded (" + current
						+ " bytes read)");
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (fos != null)
					try {
						fos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				if (bos != null)
					try {
						bos.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				if (sock != null)
					try {
						sock.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void aVoid) {
			super.onPostExecute(aVoid);
			videoFiles = new File(Environment.getExternalStorageDirectory(),
					file_name);
			if (videoFiles.isFile()) {
				playVideo(videoFiles);
			}
		}
	}
}
