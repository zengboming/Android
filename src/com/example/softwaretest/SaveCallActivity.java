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
import android.os.Bundle;
import android.os.Environment;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SaveCallActivity extends Activity {

	ListView contactsView;
	ArrayAdapter<String> adapter;
	List<String> contactsList = new ArrayList<String>();
	private SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.savecall_layout);
		contactsView = (ListView) findViewById(R.id.call_view);
		readContacts();
		adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, contactsList);
		contactsView.setAdapter(adapter);
		save(contactsList);
	}

	private void save(List<String> contactsList2) {
		// TODO Auto-generated method stub
		FileOutputStream out = null;
		BufferedWriter writer = null;
		try {
			out = new FileOutputStream(new File(
					Environment.getExternalStorageDirectory(), "CallList.txt"));
			// out=openFileOutput("CallList", Context.MODE_PRIVATE);
			writer = new BufferedWriter(new OutputStreamWriter(out));
			for (String e : contactsList2) {
				writer.write(e);
			}
			Toast.makeText(this, "通话记录成功储存，请查询SDK根目录下的CallList.txt文件", Toast.LENGTH_SHORT).show();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void readContacts() {
		// TODO Auto-generated method stub
		Cursor cursor = null;
		try {
			cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI,
					null, null, null, null);
			cursor.moveToPrevious();
			while (cursor.moveToNext()) {// 千万别用do while反正不知道为什么。。用了就错！！！还没找出原因

				String number = "号码: "
						+ cursor.getString(cursor
								.getColumnIndex(CallLog.Calls.NUMBER));

				String name = "姓名: "
						+ cursor.getString(cursor
								.getColumnIndex(CallLog.Calls.CACHED_NAME));

				String type;
				switch (Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(CallLog.Calls.TYPE)))) {
				case CallLog.Calls.INCOMING_TYPE: // 1
					type = "呼入";
					break;
				case CallLog.Calls.OUTGOING_TYPE: // 2
					type = "呼出";
					break;
				case CallLog.Calls.MISSED_TYPE: // 3
					type = "未接";
					break;
				default:
					type = "挂断";
					break;
				}

				Long ldate = cursor.getLong(cursor
						.getColumnIndex(CallLog.Calls.DATE));
				Date d = new Date(ldate);
				String date = dateFormat.format(d);

				String duration = cursor.getString(cursor
						.getColumnIndex(CallLog.Calls.DURATION));

				contactsList.add(number + "\n" + name + "\n类型: " + type
						+ "\n时间: " + date);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

}
