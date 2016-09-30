package com.ahmad_yani.dcs_930l;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ahmad_yani.dcs_930l.sessions.AlertDialogManager;
import com.ahmad_yani.dcs_930l.sessions.SessionManager;

public class ConnectTask extends AsyncTask<String, String, TCPClient> {

	String response = "";
	TCPClient mTcpClient = null;
	Activity activity;
	ListView listView;
	ProgressDialog dialog;
	
	public void setContext(Activity activity) {
		this.activity = activity;
	}

	public void setListView(ListView listView) {
		this.listView = listView;
	}
	
	@Override
	protected TCPClient doInBackground(String... message) {
		if (isCancelled())
			return null;
		mTcpClient = new TCPClient(new TCPClient.OnMessageReceived() {
			@Override
			// here the messageReceived method is implemented
			public void messageReceived(String message) {
				try {
					publishProgress(message);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		mTcpClient.run();
		return null;
	}

	@Override
	protected void onProgressUpdate(String... values) {
		super.onProgressUpdate(values);
		response = values[0];
		if (response.startsWith("#LOGIN#")) {
			int length = "#LOGIN#".length();
			String res = response.substring(length, response.length());
			String result[] = res.split("@");
			if (result.length == 2) {
				if (result[0].equals("true")) {
					SessionManager session = new SessionManager(activity);
					session.createLoginSession("Login", "Succes");
					activity.finish();
				} else {
					AlertDialogManager alert = new AlertDialogManager();
					alert.showAlertDialog(activity, "Gagal Login..",
							"Username/Password tidak ditemukan", false);
				}
			}

		} else if (response.startsWith("#LIST#")) {
			if (listView != null) {
				int length = "#LIST#".length();
				if (response.length() > length) {
					String list = response.substring(length, response.length());
					String[] items = list.split("@");
					ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(
							activity, android.R.layout.simple_list_item_1,
							items);
					listView.setAdapter(itemsAdapter);
					itemsAdapter.notifyDataSetChanged();
				}
			}
		} else if (response.startsWith("#LISTALLREKAMAN#")) {
			if (listView != null) {
				int length = "#LISTALLREKAMAN#".length();
				if (response.length() > length) {
					String list = response.substring(length, response.length());
					String[] items = list.split("@");
					ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(
							activity, android.R.layout.simple_list_item_1,
							items);
					listView.setAdapter(itemsAdapter);
					itemsAdapter.notifyDataSetChanged();
				}
			}
		} else if (response.startsWith("#LISTDATEREKAMAN#")) {
			if (listView != null) {
				int length = "#LISTDATEREKAMAN#".length();
				if (response.length() > length) {
					String list = response.substring(length, response.length());
					String[] items = list.split("@");
					ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(
							activity, android.R.layout.simple_list_item_1,
							items);
					listView.setAdapter(itemsAdapter);
					itemsAdapter.notifyDataSetChanged();
				}
			}
		} else if (response.startsWith("#LISTTIMEREKAMAN#")) {
			if (listView != null) {
				int length = "#LISTTIMEREKAMAN#".length();
				if (response.length() > length) {
					String list = response.substring(length, response.length());
					String[] items = list.split("@");
					ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(
							activity, android.R.layout.simple_list_item_1,
							items);
					listView.setAdapter(itemsAdapter);
					itemsAdapter.notifyDataSetChanged();
				}
			}
		} else if (response.startsWith("#PLAYREKAMAN#")) {
			System.out.println("PLAYREKAMAN");
			int length = "#PLAYREKAMAN#".length();
			String list = response.substring(length, response.length());
			String[] items = list.split("@");
			String fileName = items[0];
			String fileSize = items[1];
			Intent intent = new Intent(activity, RekamanActivity.class);
			intent.putExtra("file_name", fileName);
			intent.putExtra("file_size", fileSize);
			activity.startActivity(intent);
		} else if (response.startsWith("#START#")) {
			int length = "#START#".length();
			String result = response.substring(length, response.length());
			String array[] = result.split("@");
			String camName = array[0];
			String port = array[1];
			Intent intent = new Intent(activity, VideoActivity.class);
			intent.putExtra("title", camName);
			intent.putExtra("port", port);
			activity.startActivity(intent);
		} else {
			System.out.println("RESPONSE : " + response);
		}
	}

	public TCPClient getTCPClient() {
		return mTcpClient;
	}
}
