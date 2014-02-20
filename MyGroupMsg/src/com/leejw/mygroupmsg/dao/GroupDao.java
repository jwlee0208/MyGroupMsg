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
				ContactsContract.Groups.GROUP_VISIBLE,
				ContactsContract.Groups.ACCOUNT_TYPE
		};
		
        String selection = ""; 
        selection += ContactsContract.Groups.DELETED + " = 0 ";
        selection += " AND " + ContactsContract.Groups.GROUP_VISIBLE + " = 1 ";
        selection += " AND " + ContactsContract.Groups.ACCOUNT_TYPE + " = 'vnd.sec.contact.phone'" ;
        selection += (StringUtil.isNotNull(searchKeyword)) ? " AND " + ContactsContract.Groups.TITLE + " LIKE '%" + searchKeyword + "%'" : "";
                
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Groups._ID + " COLLATE LOCALIZED ASC";
        // 그룹 정보 조회 커서
        Cursor groupCursor = 
        		context.getContentResolver().query(uri, projections, selection, selectionArgs, sortOrder);
        
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
			groupCursor.close();
		}
		return groupList;
	}	
}
