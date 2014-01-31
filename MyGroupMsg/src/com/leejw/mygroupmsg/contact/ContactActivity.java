package com.leejw.mygroupmsg.contact;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import com.leejw.mygroupmsg.R;

public class ContactActivity extends Activity{
	
	private ListView listView;

	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.contact_linear);
		ScrollView scrollView = (ScrollView) findViewById(R.id.contact_scroll);
		
		
		List<Contact> contactList = this.getContactList();
		
		int contactListSize = contactList.size();
		
		if(contactListSize > 0){
//			adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
//			
//			listView = (ListView)findViewById(R.id.listView);
//			listView.setAdapter(adapter);
//			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

//			listView.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> parent, View view,
//						int position, long id) {
//					// TODO Auto-generated method stub
//					String str = (String)adapter.getItem(position);
//					
//					Toast.makeText(getBaseContext(), str, Toast.LENGTH_SHORT).show();
//				}
//				
//			});
			for(Contact contact : contactList){
				
				String receiverId = contact.getReceiverName() + ";" + contact.getReceiverPhoneNo();
				String receiverText = contact.getReceiverName() + "[" + contact.getReceiverPhoneNo() + "]";
				
//				System.out.println("receiverId ; " + receiverId);
				
				CheckBox checkBox = new CheckBox(this);
				checkBox.setText(receiverId.toString());
				
//				checkBox.setId(receiverId);
				linearLayout.addView(checkBox);
				
			}
			scrollView.addView(linearLayout);
		}else{
			
		}
		setContentView(R.layout.av_contact);
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
