package com.leejw.mygroupmsg.contact;

import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.leejw.mygroupmsg.R;

public class ContactActivity extends Activity{
	
	private ListView listView;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.av_contact);
		
		final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.contact_linear);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// 상단 문구 설정
		TextView headText = new TextView(this);
		headText.setText("수신자를 선택해 주세요.");
		linearLayout.addView(headText);


		// 하단의 버튼 추가
		LinearLayout llbottom = new LinearLayout(this);
        llbottom.setOrientation(LinearLayout.HORIZONTAL);
        Button okBtn = new Button(this);
        okBtn.setText("확인");
        
        Button cancelBtn = new Button(this);
        cancelBtn.setText("취소");

        llbottom.addView(okBtn);
        llbottom.addView(cancelBtn);
        linearLayout.addView(llbottom);
      
		// 수신자 선택 목록 스크롤 생성
		ScrollView scrollView = new ScrollView(this);

		scrollView.setLayoutParams(params);
		// 수신자 선택 목록 리니어레이아웃 생성
		LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        
		scrollView.addView(ll);
		linearLayout.addView(scrollView);

		List<Contact> contactList = this.getContactList();
		
		int contactListSize = contactList.size();
		
		if(contactListSize > 0){			
			int index = 0;
			// Create LinearLayout

			for(Contact contact : contactList){
	             
				String receiverId = contact.getReceiverName() + ";" + contact.getReceiverPhoneNo();
				String receiverText = contact.getReceiverName() + "[" + contact.getReceiverPhoneNo() + "]";
				
				CheckBox checkBox = new CheckBox(this);
				checkBox.setText(receiverId.toString());
				checkBox.setId(index);
				
				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						// TODO Auto-generated method stub
						if(buttonView.isChecked()){
							System.out.println(buttonView.getText());							
						}else{
							
						}
					}
				});

				ll.addView(checkBox);
				
				index++;
			}
		}else{
			
		}
		// 확인 버튼 클릭시
		okBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		// 취소 버튼 클릭시
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}
	
	private List<Contact> getContactList() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projections = new String[] {
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                        + " COLLATE LOCALIZED ASC";

        Cursor contactCursor = getContentResolver().query(uri, projections,
                        null, selectionArgs, sortOrder);
        List<Contact> contactList = new ArrayList<Contact>();
        Contact contactInfo = null;
        if (contactCursor.moveToFirst()) {

                do {
                        contactInfo = new Contact();

                        String phonenumber = contactCursor.getString(1).replaceAll("-",
                                        "");
                        
                        String headPhoneNo = phonenumber.substring(0, 3);
                        
                        if (phonenumber.length() == 10) {
                                phonenumber = headPhoneNo + "-"
                                                + phonenumber.substring(3, 6) + "-"
                                                + phonenumber.substring(6);
                        } else if (phonenumber.length() > 8) {
                                phonenumber = headPhoneNo + "-"
                                                + phonenumber.substring(3, 7) + "-"
                                                + phonenumber.substring(7);
                        }
                        // contactInfo.setPhotoId(contactCursor.getLong(0));
                        
                        if(headPhoneNo.equals("010") || headPhoneNo.equals("011") || headPhoneNo.indexOf("82") > 0){
                                contactInfo.setReceiverPhoneNo(phonenumber);
                                contactInfo.setReceiverName(contactCursor.getString(2));
                                contactList.add(contactInfo);                                        
                        }
                        
                } while (contactCursor.moveToNext());
        }
        return contactList;
	}	
}
