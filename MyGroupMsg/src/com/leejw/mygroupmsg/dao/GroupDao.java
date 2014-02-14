package com.leejw.mygroupmsg.dao;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import com.leejw.mygroupmsg.group.Group;
import com.leejw.utils.StringUtil;

public class GroupDao {
	public ArrayList<Group> getGroupList(String searchKeyword, Context context){
		Uri uri = ContactsContract.Groups.CONTENT_URI;

		String[] projections = new String[] {
				ContactsContract.Groups._ID,
				ContactsContract.Groups.TITLE,
				ContactsContract.Groups.DELETED,
				ContactsContract.Groups.GROUP_VISIBLE
		};

		
        String selection = ""; 
        selection += ContactsContract.Groups.DELETED + " = 0 ";
        selection += (StringUtil.isNotNull(searchKeyword)) ? " AND " + ContactsContract.Groups.TITLE + " LIKE '%" + searchKeyword + "%'" : "";
        
        
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Groups.TITLE + " COLLATE LOCALIZED ASC";

        Cursor groupCursor = 
        		context.getContentResolver().query(uri, projections,
                        selection, selectionArgs, sortOrder);
        
		ArrayList<Group> groupList = new ArrayList<Group>();
		Group groupObj = null;
		
		if(groupCursor.moveToFirst()){
			do{
				String groupId = groupCursor.getString(0);
				
				groupObj = new Group();
				groupObj.setGroupId(groupId);
				groupObj.setGroupTitle(groupCursor.getString(1));
				groupObj.setContactId(groupCursor.getColumnIndex(android.provider.ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID));
				
				groupList.add(groupObj);	
			}while(groupCursor.moveToNext());
		}
		return groupList;
	}	
}
