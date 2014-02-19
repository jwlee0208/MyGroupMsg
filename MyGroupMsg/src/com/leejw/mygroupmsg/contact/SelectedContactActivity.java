package com.leejw.mygroupmsg.contact;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

import com.leejw.mygroupmsg.R;
import com.leejw.mygroupmsg.adapter.ContactAdapter;
import com.leejw.mygroupmsg.main.MainActivity;
import com.leejw.utils.StringUtil;

public class SelectedContactActivity extends Activity{
	
	ArrayList<Contact> contactList; 
	ListView selectedContactListView;
	ContactAdapter contactAdapter;
			
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.av_contact_list);
		
		setHeader();
		
		Intent intent = getIntent();
		
		contactList = (ArrayList<Contact>) intent.getSerializableExtra("contactList");
		
		selectedContactListView = (ListView)findViewById(R.id.contactList);
		
		contactAdapter = new ContactAdapter(this, contactList);
		
		selectedContactListView.setAdapter(contactAdapter);		
	}
	
	private void setHeader() {
		// ������ ������
		Button cancelBtn = (Button) findViewById(R.id.select_cancel);
		Button okBtn = (Button) findViewById(R.id.select_ok);
		/**
		 * case to click cancel button.
		 */
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		/**
		 * case to click ok button.
		 */
		okBtn.setOnClickListener(new OnClickListener() {

			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getBaseContext(), MainActivity.class);
				intent.putExtra("contactList", contactList);
				setResult(3, intent);
				finish();
			}
		});
	}
}
