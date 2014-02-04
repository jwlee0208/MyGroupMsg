package com.leejw.mygroupmsg.main;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony.Sms;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.leejw.mygroupmsg.R;
import com.leejw.mygroupmsg.contact.Contact;
import com.leejw.mygroupmsg.contact.ContactActivity;

public class MainActivity extends Activity{
	
	ArrayList<Contact> contactList;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.av_main);
		
		final EditText textView = (EditText)findViewById(R.id.editText1);
		
		Button button00 = (Button)findViewById(R.id.button0);
		Button button01 = (Button)findViewById(R.id.button1);
		Button button02 = (Button)findViewById(R.id.button2);
		/**
		 *  전송
		 */
		button00.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Toast.makeText(getApplicationContext(), "메시지 전송...", Toast.LENGTH_LONG).show();
				
				System.out.println("textView.getText().toString() : " + textView.getText().toString());
				
				String msgStr = textView.getText().toString();
				if(msgStr.isEmpty()){
					Toast.makeText(getApplicationContext(), "입력된 메시지가 없습니다.", Toast.LENGTH_LONG).show();
					return;
				}
				
				if(contactList == null || contactList.size() < 1){
					Toast.makeText(getApplicationContext(), "선택된 수신자가 없습니다.", Toast.LENGTH_LONG).show();
				}
				
			}
		});
		/**
		 * 초기화
		 */
		button01.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "초기화...", Toast.LENGTH_LONG).show();
			}
		});
		
		/**
		 * 수신자 선택
		 */
		button02.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent intent = new Intent(getBaseContext(), ContactActivity.class);
				startActivityForResult(intent, 2);
			}
		});
		
	}
	
	@SuppressWarnings("unchecked")
	protected void onActivityResult(int requestCode, int resultCode, Intent Data){
		super.onActivityResult(requestCode, resultCode, Data);
		
		if(requestCode == 2){
			Toast.makeText(getApplicationContext(), "수신자 선택 창 전환...", Toast.LENGTH_LONG).show();
			
			if(resultCode == 2){
				// ref.] http://blog.daum.net/haha25/5387851
				this.contactList = (ArrayList<Contact>)Data.getSerializableExtra("contactList");
				
				if(contactList != null){
					int contactListSize = contactList.size();
					
					if(contactListSize < 1){
						Toast.makeText(getApplicationContext(), "선택된 수신자가 없습니다.", Toast.LENGTH_LONG).show();
					}
				}
			}
		}
	}
	
	
}
