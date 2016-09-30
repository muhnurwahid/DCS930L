package com.ahmad_yani.dcs_930l;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.ahmad_yani.dcs_930l.sessions.AlertDialogManager;

public class ListTimeRekamanActivity extends Activity {

	AlertDialogManager alert = new AlertDialogManager();
	TCPClient mTcpClient = MainActivity.conctTask.getTCPClient();
	ConnectTask cTask = MainActivity.getConnectTask();
	ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_time_rekaman);		
		final Bundle bundle = getIntent().getExtras();
		getActionBar().setTitle(bundle.getString("ipcam")+"/"+bundle.getString("date"));
		listView = (ListView) findViewById(R.id.listViewTimeRekaman);
		cTask.setContext(this);
		cTask.setListView(listView);
		mTcpClient = cTask.getTCPClient();
		String message = "#LISTTIMEREKAMAN#"+bundle.getString("ipcam")+"@"+bundle.getString("date");
		if (mTcpClient != null) {
			if(!mTcpClient.sendMessage(message)){
				if(cTask.isCancelled()){
					cTask = new ConnectTask();
					cTask.setContext(ListTimeRekamanActivity.this);
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
				String file_path = "video/"+bundle.getString("ipcam")+"/"+bundle.getString("date")+"/"+listView.getAdapter().getItem(position).toString();
				if (mTcpClient != null) {
					if(!mTcpClient.sendMessage("#PLAYREKAMAN#"+file_path)){
						if(cTask.isCancelled()){
							cTask = new ConnectTask();
							cTask.setContext(ListTimeRekamanActivity.this);
							cTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);								
							MainActivity.conctTask = cTask;
							Toast.makeText(getApplicationContext(), "Try Again!!!", Toast.LENGTH_LONG).show();
						}else{
							cTask.cancel(true);
							mTcpClient.stopClient();
						}
					}
				}
			}
		});
	}
}
