package com.ahmad_yani.dcs_930l;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ahmad_yani.dcs_930l.sessions.AlertDialogManager;

public class LoginActivity extends Activity {

	EditText txtUsername, txtPassword;
	Button btnLogin;
	AlertDialogManager alert = new AlertDialogManager();
	TCPClient mTcpClient = MainActivity.conctTask.getTCPClient();
	ConnectTask cTask = MainActivity.getConnectTask();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		retryConnection();
		txtUsername = (EditText) findViewById(R.id.txtUsername);
		txtPassword = (EditText) findViewById(R.id.txtPassword);
		btnLogin = (Button) findViewById(R.id.btnLogin);

		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String username = txtUsername.getText().toString();
				String password = txtPassword.getText().toString();
				if (username.trim().length() > 0
						&& password.trim().length() > 0) {
					String message = "#LOGIN#" + username + "@" + password;
					mTcpClient = cTask.getTCPClient();
					if (mTcpClient != null) {
						if (!mTcpClient.sendMessage(message)) {
							retryConnection();
						}
					} else {
						Toast.makeText(getApplicationContext(), "TCP NULL",
								Toast.LENGTH_LONG).show();
					}
				} else {
					alert.showAlertDialog(LoginActivity.this, "Login failed..",
							"Please enter username and password", false);
				}
			}
		});

	}

	public void retryConnection(){
		cTask = new ConnectTask();
		cTask.setContext(LoginActivity.this);
		cTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);		
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("Please Wait");
		dialog.setCancelable(false);
		dialog.show();
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {
			public void run() {
				dialog.dismiss();
				if (cTask.getStatus() == AsyncTask.Status.RUNNING && cTask.getTCPClient().getSocket()==null) {
					new AlertDialog.Builder(LoginActivity.this)
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
					if(!cTask.getTCPClient().getSocket().isConnected()){
						new AlertDialog.Builder(LoginActivity.this)
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
		}, MainActivity.TIMEOUT);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent i = new Intent(getApplicationContext(),
					SettingActivity.class);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
