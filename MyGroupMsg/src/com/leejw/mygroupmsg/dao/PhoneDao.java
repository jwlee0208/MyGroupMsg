package com.leejw.mygroupmsg.dao;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.leejw.mygroupmsg.contact.Contact;

public class PhoneDao {
	Contact contactInfo;
	Uri uri;
	String[] projections;
	String selection;
	Cursor phoneCursor;
    String[] selectionArgs;	
    String sortOrder;

    private void setCursorInfos(long contactId, Context context){
		uri = Phone.CONTENT_URI;
		projections = new String[] {
				  Phone.NUMBER
				, Phone.TYPE
		};
		selection = Phone.CONTACT_ID + " = " + contactId;
		selectionArgs = null;	
		sortOrder = null;
		phoneCursor = context.getContentResolver().query(uri, projections, selection, selectionArgs, sortOrder);

    }
    
	public Contact getPhoneInfo(long contactId, Context context){
		this.setCursorInfos(contactId, context);
		
		if(phoneCursor.moveToFirst()){
			do{
				String phoneNo = phoneCursor.getString(0);
				int phoneNoType = phoneCursor.getInt(1);
				
				contactInfo = new Contact();
				contactInfo.setPhoneNoType(phoneNoType);
				contactInfo.setReceiverPhoneNo(phoneNo);
				
			}while(phoneCursor.moveToNext());
			phoneCursor.close();
		}
		
		return contactInfo;
	}
}
