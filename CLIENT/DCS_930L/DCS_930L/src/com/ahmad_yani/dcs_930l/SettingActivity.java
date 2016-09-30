package com.ahmad_yani.dcs_930l;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ahmad_yani.dcs_930l.database.ServerHandler;
import com.ahmad_yani.dcs_930l.entity.Server;
import com.ahmad_yani.dcs_930l.pengaturan.IPAddressValidator;
import com.ahmad_yani.dcs_930l.sessions.AlertDialogManager;

public class SettingActivity extends Activity {

	EditText txtServer, txtPort;
	Button btnSimpan;
	AlertDialogManager alert = new AlertDialogManager();
	IPAddressValidator ip_validator;
	ServerHandler serverHandler;
	List<Server> listServer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		serverHandler = new ServerHandler(this);
		listServer = serverHandler.getAllServers();
		ip_validator = new IPAddressValidator();
		txtServer = (EditText) findViewById(R.id.txtServer);
		txtPort = (EditText) findViewById(R.id.txtPort);
		btnSimpan = (Button) findViewById(R.id.btnLogin);
		btnSimpan.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String ipaddress = txtServer.getText().toString();
				String port = txtPort.getText().toString();
				if (ipaddress.trim().length() > 0 && port.trim().length() > 0) {
					if (ip_validator.validate(ipaddress)) {
						try {
							int port_valid = Integer.parseInt(port);
							if(listServer.size()>0){
								Server _server = listServer.get(0);	
								_server.setIpAddress(ipaddress);
								_server.setPort(""+port_valid);
								serverHandler.updateServer(_server);
							}else{
								Server _server = new Server();	
								_server.setIpAddress(ipaddress);
								_server.setPort(""+port_valid);
								serverHandler.addServer(_server);
							}
							Toast.makeText(SettingActivity.this, "Pengaturan berhasil di simpan..", Toast.LENGTH_LONG).show();
							finish();
						} catch (Exception e) {
							alert.showAlertDialog(SettingActivity.this,
									"Gagal Menyimpan..", "Port harus angka",
									false);
						}
					} else {
						alert.showAlertDialog(SettingActivity.this,
								"Gagal Menyimpan..",
								"IP Address Tidak di Kenal", false);
					}
				} else {
					alert.showAlertDialog(SettingActivity.this,
							"Gagal Menyimpan..",
							"IP Server dan Port harus di isi", false);
				}
			}
		});

		if (listServer.size() > 0) {
			Server _server = listServer.get(0);
			txtServer.setText(_server.getIpAddress());
			txtPort.setText(_server.getPort());
		}

	}
}
