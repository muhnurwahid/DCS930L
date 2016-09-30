package com.ahmad_yani.dcs_930l;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ahmad_yani.dcs_930l.sessions.AlertDialogManager;

public class ListAllRekamanActivity extends Activity {
	
	AlertDialogManager alert = new AlertDialogManager();	
	TCPClient mTcpClient = MainActivity.conctTask.getTCPClient();
	ConnectTask cTask = MainActivity.getConnectTask();
	ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_all_rekaman);
		listView = (ListView) findViewById(R.id.listViewAllRekaman);
		cTask.setContext(this);
		cTask.setListView(listView);
		mTcpClient = cTask.getTCPClient();
		String message = "#LISTALLREKAMAN#";
		if (mTcpClient != null) {
			if(!mTcpClient.sendMessage(message)){
				if(cTask.isCancelled()){
					cTask = new ConnectTask();
					cTask.setContext(ListAllRekamanActivity.this);
					cTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);								
					MainActivity.conctTask = cTask;
					Toast.makeText(getApplicationContext(), "Try Again!!!", Toast.LENGTH_LONG).show();
				}else{
					cTask.cancel(true);
					mTcpClient.stopClient();
				}
			}
		}
		listView
		.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ListAllRekamanActivity.this,ListDateRekamanActivity.class);
				intent.putExtra("ipcam", listView.getAdapter().getItem(position).toString());
				startActivity(intent);
			}
		});
	}
}
