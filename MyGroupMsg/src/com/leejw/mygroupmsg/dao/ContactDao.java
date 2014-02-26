package com.leejw.mygroupmsg.dao;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;

import com.leejw.mygroupmsg.contact.Contact;

public class ContactDao {
	/**
	 * ��������� ������
	 * @param searchKeyword
	 * @param groupId
	 * @return
	 */
	public ArrayList<Contact> getContactList(String searchKeyword, String groupId, Context context){
		
		ArrayList<Contact> contactList = new ArrayList<Contact>();
		Contact contactInfo = null;
		
		Uri uri = 
//				Contacts.CONTENT_URI;
		Data.CONTENT_URI;

		String[] projections = new String[] {
				Contacts.DISPLAY_NAME
			  , android.provider.ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID
			  , Phone.NUMBER
		};
		
        String selection = ""; 
        
        selection += Data.DATA1 + " = ? "; 
//        		CommonDataKinds.GroupMembership.GROUP_ROW_ID + " =  ? ";
        selection += " AND ";
        selection += CommonDataKinds.GroupMembership.HAS_PHONE_NUMBER + " = 1 ";
        selection += " AND ";
        selection += Data.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'";
//        selection += (StringUtil.isNotNull(searchKeyword)) ? " AND " + Contacts.DISPLAY_NAME + " LIKE '%" + searchKeyword + "%'" : "";
        String[] selectionArgs = { groupId };	
        String sortOrder = null;
//        		Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        Cursor grpCursor = 
        		context.getContentResolver().query(uri, projections, selection, selectionArgs, sortOrder);
        
        long startGrpTime = System.currentTimeMillis();
		
        if(grpCursor.moveToFirst()){
			do{

//				int nameColumnIndex = grpCursor.getColumnIndex(Phone.DISPLAY_NAME);
				
				String name = grpCursor.getString(0);
				long contactId = grpCursor.getLong(1);
				
//				long contactId = grpCursor.getLong(grpCursor.getColumnIndex(GroupMembership.CONTACT_ID));
//				String phoneNo = grpCursor.getString(grpCursor.getColumnIndex(Phone.NUMBER));
				
//				System.out.println("groupId : " + groupId + ", contactId : " + contactId + ", name : " + name);
				
				contactInfo = new Contact();

                contactInfo.setReceiverName(name);
                contactInfo.setGroupId(groupId);
				contactInfo.setContactId(contactId);
                contactList.add(contactInfo);

			}while(grpCursor.moveToNext());
			grpCursor.close();
			
			long endGrpTime = System.currentTimeMillis();
			
			System.out.println("row Time : " + (endGrpTime-startGrpTime)/1000 +"초");
		}	
		
        return contactList;
	}
}
