package com.example.softwaretest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SavePhoneActivity extends Activity {
	
	ListView contactsView;
	ArrayAdapter<String> adapter;
	List<String> contactsList=new ArrayList<String>();
	List<String> numberList=new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.savephone_layout);
		contactsView=(ListView)findViewById(R.id.phone_view);
		readContacts();
		adapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
				contactsList);
		contactsView.setAdapter(adapter);
		save(contactsList);
		
		//点击ListView拨打电话
		contactsView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view, int positon,
					long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Intent.ACTION_CALL,
						Uri.parse("tel:"+numberList.get(positon)));
				startActivity(intent);
			}
		});
	}

	private void save(List<String> contactsList2) {
		// TODO Auto-generated method stub
		FileOutputStream out=null;
		BufferedWriter writer=null;
		try{
			out = new FileOutputStream(new File(
					Environment.getExternalStorageDirectory(), "PhoneList.txt"));
			//out=openFileOutput("PhoneList", Context.MODE_PRIVATE);
			writer=new BufferedWriter(new OutputStreamWriter(out));
			for (String e : contactsList2) {
				writer.write(e);
			}
			Toast.makeText(this, "通讯录信息成功储存，请查询SDK根目录下的PhoneList.txt文件", Toast.LENGTH_SHORT).show();
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
		try{
			cursor=getContentResolver().query(
					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
					null, null, null, null);
			if(cursor!=null){
				while(cursor.moveToNext()){
					//电话
					String number=cursor.getString(cursor.getColumnIndex(
							ContactsContract.CommonDataKinds.Phone.NUMBER));
					if(TextUtils.isEmpty(number))
					{
						continue;
					}
					numberList.add(number);
					//姓名
					String diaplayName=cursor.getString(cursor.getColumnIndex(
							ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
					//联系人ID
					Long id=cursor.getLong(cursor.getColumnIndex(
							ContactsContract.CommonDataKinds.Phone._ID));
					//最近联系时间
					//Long lastTime=cursor.getLong(cursor.getColumnIndex(
					//		ContactsContract.CommonDataKinds.Phone.LAST_TIME_CONTACTED));
					contactsList.add("姓名："+diaplayName+"\n电话号码："+number+"\nID："
							+id.toString());
				}
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
