package com.ahmad_yani.dcs_930l;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ahmad_yani.dcs_930l.database.ServerHandler;
import com.ahmad_yani.dcs_930l.entity.Server;
import com.ahmad_yani.dcs_930l.sessions.AlertDialogManager;
import com.ahmad_yani.dcs_930l.sessions.SessionManager;

public class MainActivity extends Activity {

	public static final int TIMEOUT = 3000;
	
	AlertDialogManager alert = new AlertDialogManager();
	SessionManager session;
	public static ConnectTask conctTask = null;
	TCPClient mTcpClient = null;
	ListView listCamera;

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		session = new SessionManager(getApplicationContext());
		session.checkLogin();
		
		ServerHandler serverHandler;
		List<Server> listServer;
		serverHandler = new ServerHandler(this);
		listServer = serverHandler.getAllServers();
		if (listServer.size() > 0) {
			Server _server = listServer.get(0);
			TCPClient.SERVERIP = _server.getIpAddress();
			TCPClient.SERVERPORT = Integer.parseInt(_server.getPort());
		}

		listCamera = (ListView) findViewById(R.id.listCamera);
		listCamera
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						// TODO Auto-generated method stub
						mTcpClient = conctTask.getTCPClient();
						if (!mTcpClient.sendMessage("#START#"
								+ listCamera.getAdapter().getItem(position))) {
							if (conctTask.isCancelled()) {
								conctTask = new ConnectTask();
								conctTask
										.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
							}
							Toast.makeText(getApplicationContext(),
									"Cannot Send Message!!!\nTry Again",
									Toast.LENGTH_LONG).show();
						}
					}
				});
		
		retryConnection();

	}
	
	public void retryConnection(){
		conctTask = new ConnectTask();
		conctTask.setContext(MainActivity.this);
		conctTask.setListView(listCamera);
		conctTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);		
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("Please Wait");
		dialog.setCancelable(false);
		dialog.show();
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				dialog.dismiss();
				if (conctTask.getStatus() == AsyncTask.Status.RUNNING && conctTask.getTCPClient().getSocket()==null) {
					new AlertDialog.Builder(MainActivity.this)
							.setTitle("Error..!")
							.setMessage("Koneksi Gagal")
							.setCancelable(false)
							.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									
								}
							})
							.setPositiveButton("Retry",
									new DialogInterface.OnClickListener() {
										@Override
										public void onClick(
												DialogInterface dialogInterface,
												int i) {
											retryConnection();
										}
									}).show();
				}else{
					if(!conctTask.getTCPClient().getSocket().isConnected()){
						new AlertDialog.Builder(MainActivity.this)
						.setTitle("Error..!")
						.setMessage("Koneksi Gagal")
						.setCancelable(false)
						.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								
							}
						})
						.setPositiveButton("Retry",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(
											DialogInterface dialogInterface,
											int i) {
										retryConnection();
									}
								}).show();
					}
				}
			}
		}, TIMEOUT);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static ConnectTask getConnectTask() {
		return conctTask;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_refresh) {
			retryConnection();
		} else if (id == R.id.action_recorded) {
			Intent intent = new Intent(MainActivity.this,
					ListAllRekamanActivity.class);
			startActivity(intent);
		} else if (id == R.id.action_settings) {
			Intent i = new Intent(getApplicationContext(),
					SettingActivity.class);
			startActivity(i);
		} else if (id == R.id.action_logout) {
			session.logoutUser();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		try {
			System.out.println("onDestroy.");
			if (mTcpClient != null) {
				mTcpClient.stopClient();
			}
			conctTask.cancel(true);
			conctTask = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}
}
