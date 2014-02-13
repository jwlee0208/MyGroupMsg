package com.leejw.mygroupmsg.group;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.leejw.mygroup.adapter.GroupAdapter;
import com.leejw.mygroupmsg.R;
import com.leejw.mygroupmsg.contact.Contact;
import com.leejw.utils.StringUtil;

public class GroupListActivity extends Activity{
	
	TextView statusTextView;
	ListView groupListView;
	
	int groupCnt;
	int totalListCnt;
	ArrayList<Group> groupInfos;
	GroupAdapter adapter;
	LinearLayout linearLayout;
	TextView tempText;
	
	List<Group> groupList;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.av_group_list);
		
		statusTextView = (TextView) findViewById(R.id.status);
		
		groupInfos = new ArrayList<Group>();
		groupList = this.getGroupList(null);
		
		totalListCnt = (StringUtil.isNotNull(groupList)) ? groupList.size() : 0;
				
		if(totalListCnt > 0){
			for(int groupCnt = 0 ; groupCnt < 10; groupCnt++){
				groupInfos.add(groupList.get(groupCnt));				
			}
		}else{
			for(int groupCnt = 0 ; groupCnt < totalListCnt ; groupCnt++){
				groupInfos.add(groupList.get(groupCnt));
			}
		}
		/**
		 * 동적 listview (예} 앱스토어 목록 조회.)
		 * Ref.]
		 * 1. http://www.androidpub.com/1199511
		 * 
		 * listView와 ArrayAdapter의 사용.
		 * Ref.] 
		 * 1. https://github.com/thecodepath/android_guides/wiki/Using-an-ArrayAdapter-with-ListView
		 * 2. http://stackoverflow.com/questions/11492678/android-classcastexception-on-layoutparams-from-linearlayout-to-abslistview
		 * 
		 * IllegalStateException: ArrayAdapter requires the resource ID to be a TextView
		 * Ref.]
		 * 1. http://stackoverflow.com/questions/15940287/illegalstateexception-arrayadapter-requires-the-resource-id-to-be-a-textview
		 */
		adapter = new GroupAdapter(this, groupInfos);
		
		groupListView = (ListView)findViewById(R.id.groupList);
		linearLayout = (LinearLayout) View.inflate(this, R.layout.av_temptext, null);
		linearLayout.setVisibility(View.INVISIBLE);
		groupListView.addFooterView(linearLayout);
		groupListView.setAdapter(adapter);
		
		groupListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				
			}
			
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if((firstVisibleItem + visibleItemCount) == totalItemCount){
					
					if(totalListCnt > groupCnt){
						linearLayout.setVisibility(View.VISIBLE);
						new getMoreItems().execute(groupInfos);
					}else{
						linearLayout.setVisibility(View.INVISIBLE);
					}
					
				}
			}
		});

	}
	
	private class getMoreItems extends AsyncTask<ArrayList<Group>, Integer, Long> {

		@Override
		protected Long doInBackground(ArrayList<Group>... arg0) {
			// TODO Auto-generated method stub
			Long result = 0L;
			
			if(totalListCnt >= groupCnt + 10){
				int tmpEnd = groupCnt + 10;
				for(; groupCnt < tmpEnd ; groupCnt++){
					groupInfos.add(groupList.get(groupCnt));
				}
			}else{
				for(;groupCnt < totalListCnt ; groupCnt++){
					groupInfos.add(groupList.get(groupCnt));
				}
			}
			
			try{
				Thread.sleep(5000);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return null;
		}
		protected void onPostExecute(Long result){
			adapter.notifyDataSetChanged();
		}

	}
	/**
	 * 주소록 그룹 정보 목록 조회
	 * @param searchKeyword
	 * @return
	 */
	private List<Group> getGroupList(String searchKeyword){
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
        		getContentResolver().query(uri, projections,
                        selection, selectionArgs, sortOrder);
        
		List<Group> groupList = new ArrayList<Group>();
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
	
	/**
	 * 주소록 조회
	 * @param searchKeyword
	 * @param groupId
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<Contact> getContactList(String searchKeyword, String groupId){
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
        		getContentResolver().query(uri, projections,selection, selectionArgs, sortOrder);
        
		if(grpCursor.moveToFirst()){
			do{

				int nameColumnIndex = grpCursor.getColumnIndex(Phone.DISPLAY_NAME);
				
				String name = grpCursor.getString(nameColumnIndex);
				
				long contactId = grpCursor.getLong(grpCursor.getColumnIndex(android.provider.ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID));
				
				Cursor numberCursor = getContentResolver().query(Phone.CONTENT_URI, new String[]{Phone.NUMBER}, Phone.CONTACT_ID + " = " + contactId, null, null);
			
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
	
//	private class GroupAdapter extends ArrayAdapter<Group>{
//
//		public GroupAdapter(Context context, ArrayList<Group> groups) {
//			super(context, R.layout.av_group_row, groups);
//			// TODO Auto-generated constructor stub
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			// TODO Auto-generated method stub
//			GroupViewHolder holder;
//
//			Group groupInfo = getItem(position);
//			
//			if(convertView == null){
//				convertView = LayoutInflater.from(getContext()).inflate(R.layout.av_group_row, null);
//				holder = new GroupViewHolder();
//				holder.groupName = (TextView)convertView.findViewById(R.id.groupName);
//				holder.groupId = (TextView)convertView.findViewById(R.id.groupId);
//				convertView.setTag(holder);
//			}else{
//				holder = (GroupViewHolder)convertView.getTag();
//			}
//			
//			String groupIdStr = groupInfo.getGroupId();
//			
//			holder.groupName.setText(groupInfo.getGroupTitle());
//			holder.groupId.setText(groupIdStr);
//			
//			return convertView;
//		}
//	}
//	
//	static class GroupViewHolder{
//		TextView groupName;
//		TextView groupId;
//		boolean loaded;
//	}

}
