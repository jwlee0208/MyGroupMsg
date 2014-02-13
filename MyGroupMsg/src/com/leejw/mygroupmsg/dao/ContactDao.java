package com.leejw.mygroupmsg.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;

import com.leejw.mygroupmsg.contact.Contact;
import com.leejw.utils.StringUtil;

public class ContactDao {
	/**
	 * 주소록 조회
	 * @param searchKeyword
	 * @param groupId
	 * @return
	 */
	public List<Contact> getContactList(String searchKeyword, String groupId, Context context){
		List<Contact> contactList = new ArrayList<Contact>();
		Contact contactInfo = null;
		
		Uri uri = Data.CONTENT_URI;

		String[] projections = new String[] {
				Contacts.DISPLAY_NAME
			  , android.provider.ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID
		};
		
        String selection = ""; 
        selection += CommonDataKinds.GroupMembership.GROUP_ROW_ID + " =  ? " + " AND ";
        selection += ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'" ;
        selection += (StringUtil.isNotNull(searchKeyword)) ? " AND " + Contacts.DISPLAY_NAME + " LIKE '%" + searchKeyword + "%'" : "";
        String[] selectionArgs = {String.valueOf(groupId)};
        String sortOrder = 
        		Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        Cursor grpCursor = 
        		context.getContentResolver().query(uri, projections,selection, selectionArgs, sortOrder);
        
		if(grpCursor.moveToFirst()){
			do{

				int nameColumnIndex = grpCursor.getColumnIndex(Phone.DISPLAY_NAME);
				
				String name = grpCursor.getString(nameColumnIndex);
				
				long contactId = grpCursor.getLong(grpCursor.getColumnIndex(android.provider.ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID));
				
				Cursor numberCursor = context.getContentResolver().query(Phone.CONTENT_URI, new String[]{Phone.NUMBER}, Phone.CONTACT_ID + " = " + contactId, null, null);
			
				if(numberCursor.moveToFirst()){
					int numberColumnIndex = numberCursor.getColumnIndex(Phone.NUMBER);
					do{
						String phoneNumber = numberCursor.getString(numberColumnIndex);
						
						contactInfo = new Contact();
                        contactInfo.setReceiverPhoneNo(phoneNumber);
                        contactInfo.setReceiverName(name);
                        contactInfo.setGroupId(groupId);
                        contactList.add(contactInfo);
						
					}while(numberCursor.moveToNext());
					numberCursor.close();	
				}
			}while(grpCursor.moveToNext());
			grpCursor.close();
		}	
        return contactList;
	}
}
