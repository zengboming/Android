package com.example.softwaretest;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends Activity {

	Button savePhone;
	Button saveMessage;
	Button savePhoto;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        savePhone=(Button)findViewById(R.id.save_phone);
        saveMessage=(Button)findViewById(R.id.save_message);
        savePhoto=(Button)findViewById(R.id.save_photo);
        savePhone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,SavePhoneActivity.class);
				startActivity(intent);
			}
		});
        saveMessage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,SaveMessageActivity.class);
				startActivity(intent);
			}
		});
        savePhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,SaveCallActivity.class);
				startActivity(intent);
			}
		});
		
    }
    
}
