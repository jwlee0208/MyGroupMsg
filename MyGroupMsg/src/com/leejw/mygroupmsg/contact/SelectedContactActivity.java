package com.leejw.mygroupmsg.contact;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.leejw.mygroupmsg.R;
import com.leejw.utils.StringUtil;

public class SelectedContactActivity extends Activity{

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.av_selected_contact);
		
		Intent intent = getIntent();
		
		ArrayList<Contact> contactList = (ArrayList<Contact>) intent.getSerializableExtra("contactList");
		
		if(StringUtil.isNotNull(contactList)){
			int contactListSize = contactList.size();
			if(contactListSize > 0){
				for(Contact contactObj : contactList){
					System.out.println("contactObj : " + contactObj.getReceiverName() +", " + contactObj.getReceiverPhoneNo());
				}
			}
		}
	}
}
