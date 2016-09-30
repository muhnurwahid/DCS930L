package com.ahmad_yani.dcs_930l.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.ahmad_yani.dcs_930l.entity.Server;

public class ServerHandler extends DatabaseHandler{
	// SERVER table IPSERVER
    private static final String TABLE_SERVER = "server";
 
    // SERVER Table Columns IPSERVERs
    private static final String KEY_ID = "id";
    private static final String KEY_IPSERVER = "ipserver";
    private static final String KEY_PORT = "port";
    
	public ServerHandler(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SERVER_TABLE = "CREATE TABLE " + TABLE_SERVER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_IPSERVER + " TEXT,"
                + KEY_PORT + " TEXT" + ")";
        db.execSQL(CREATE_SERVER_TABLE);
    }
 
    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SERVER);
 
        // Create tables again
        onCreate(db);        
    }
	
	// Adding new server
    public void addServer(Server server) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_IPSERVER, server.getIpAddress()); // Server Name
        values.put(KEY_PORT, server.getPort()); // Server Phone
 
        // Inserting Row
        db.insert(TABLE_SERVER, null, values);
        db.close(); // Closing database connection
    }
 
    // Getting single server
    Server getServer(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
 
        Cursor cursor = db.query(TABLE_SERVER, new String[] { KEY_ID,
                KEY_IPSERVER, KEY_PORT }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
 
        Server server = new Server();
        server.setId(Integer.parseInt(cursor.getString(0)));
        server.setIpAddress(cursor.getString(1));
        server.setPort(cursor.getString(2));
        // return server
        return server;
    }
     
    // Getting All Contacts
    public List<Server> getAllServers() {
        List<Server> serverList = new ArrayList<Server>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_SERVER;
 
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
 
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Server server = new Server();
                server.setId(Integer.parseInt(cursor.getString(0)));
                server.setIpAddress(cursor.getString(1));
                server.setPort(cursor.getString(2));
                // Adding server to list
                serverList.add(server);
            } while (cursor.moveToNext());
        }
 
        // return server list
        return serverList;
    }
 
    // Updating single server
    public int updateServer(Server server) {
        SQLiteDatabase db = this.getWritableDatabase();
 
        ContentValues values = new ContentValues();
        values.put(KEY_IPSERVER, server.getIpAddress());
        values.put(KEY_PORT, server.getPort());
 
        // updating row
        return db.update(TABLE_SERVER, values, KEY_ID + " = ?",
                new String[] { String.valueOf(server.getId()) });
    }
 
    // Deleting single server
    public void deleteServer(Server server) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SERVER, KEY_ID + " = ?",
                new String[] { String.valueOf(server.getId()) });
        db.close();
    }
 
 
    // Getting contacts Count
    public int getServersCount() {
        String countQuery = "SELECT  * FROM " + TABLE_SERVER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
 
        // return count
        return cursor.getCount();
    }

}
