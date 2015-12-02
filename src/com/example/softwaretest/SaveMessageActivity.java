package com.example.softwaretest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SaveMessageActivity extends Activity {
	
	private static final String SMS_ALL="content://sms/" ;
	//private static final String Lookup="content://com.android.contacts/phone_lookup/";
	
	ListView contactsView;
	ArrayAdapter<String> adapter;
	List<String> contactsList=new ArrayList<String>();
	
	private SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.savemessage_layout);
		contactsView=(ListView)findViewById(R.id.message_view);
		readContacts();
		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				contactsList);
		contactsView.setAdapter(adapter);
		save(contactsList);
	}
	
	private void save(List<String> contactsList2) {
		// TODO Auto-generated method stub
		FileOutputStream out=null;
		BufferedWriter writer=null;
		try{
			out = new FileOutputStream(new File(
					Environment.getExternalStorageDirectory(), "MessageList.txt"));
			//out=openFileOutput("MessageList", Context.MODE_PRIVATE);
			writer=new BufferedWriter(new OutputStreamWriter(out));
			for (String e : contactsList2) {
				writer.write(e);
			}
			Toast.makeText(this, "短信记录成功储存，请查询SDK根目录下的MessageList.txt文件", Toast.LENGTH_SHORT).show();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				if(writer!=null){
					writer.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	private void readContacts() {
		// TODO Auto-generated method stub
		Cursor cursor=null;
		Uri uri=Uri.parse(SMS_ALL);
		try{
			cursor=getContentResolver().query(uri, null, null, null, "date DESC");
			/*
			Cursor cur=new CursorWrapper(cursor){
				@Override
				public String getString(int columnIndex) {
					// TODO Auto-generated method stub
					if(super.getColumnIndex("address")==columnIndex){
						String contacts=super.getString(columnIndex);
						Uri uri=Uri.parse(Lookup+contacts);
						Cursor c=getContentResolver().query(uri, null, null, null, null);
						if(c.moveToFirst()){
							String contactname=c.getString(c.getColumnIndex("display_name"));
							return contactname;
						}
						return contacts;
					}
					return super.getString(columnIndex);
					
				}
			};
			*/
			while(cursor.moveToNext()){
				String groupId="序号: "+cursor.getString(cursor.getColumnIndex("thread_id"));
				//Log.d("MainActivity", groupId);
				String number="号码: "+cursor.getString(cursor.getColumnIndex("address"));
				//Log.d("MainActivity", number);
				//String name="姓名: "+cursor.getString(cursor.getColumnIndex("person"));
				String msgContent="内容: "+cursor.getString(cursor.getColumnIndex("body"));
				//Log.d("MainActivity", msgContent);
				//String date="时间: "+dateFormat.format(cursor.getColumnIndex("date"));
				//Log.d("MainActivity", date);
				//时间
				Long ldate=cursor.getLong(cursor.getColumnIndex("date"));
				Date d=new Date(ldate);
				String date=dateFormat.format(d);
				//类型
				int type=cursor.getInt(cursor.getColumnIndex("type"));
				String strType="";
				if(type==1){
					strType="接收";
				}else if(type==2){
					strType="发送";
				}else{
					strType=null;
				}
				//Log.d("MainActivity", strType);
				contactsList.add(groupId+"\n"+number+"\n"+msgContent+"\n时间: "+date+"\n类型: "+strType);
				/*
				for (String e : contactsList) {
					Log.d("MainActivity", e.toString());
				}
				*/
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(cursor!=null){
				cursor.close();
			}
		}
	}

}
