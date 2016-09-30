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

public class ListDateRekamanActivity extends Activity {
	
	AlertDialogManager alert = new AlertDialogManager();
	TCPClient mTcpClient = MainActivity.conctTask.getTCPClient();
	ConnectTask cTask = MainActivity.getConnectTask();
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_date_rekaman);
		final Bundle bundle = getIntent().getExtras();
		getActionBar().setTitle(bundle.getString("ipcam"));
		listView = (ListView) findViewById(R.id.listViewDateRekaman);
		cTask.setContext(this);
		cTask.setListView(listView);
		mTcpClient = cTask.getTCPClient();
		String message = "#LISTDATEREKAMAN#"+bundle.getString("ipcam");
		if (mTcpClient != null) {
			if(!mTcpClient.sendMessage(message)){
				if(cTask.isCancelled()){
					cTask = new ConnectTask();
					cTask.setContext(ListDateRekamanActivity.this);
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
				Intent intent = new Intent(ListDateRekamanActivity.this,ListTimeRekamanActivity.class);
				intent.putExtra("ipcam", bundle.getString("ipcam"));
				intent.putExtra("date", listView.getAdapter().getItem(position).toString());
				
				startActivity(intent);
			}
		});
	}
}
