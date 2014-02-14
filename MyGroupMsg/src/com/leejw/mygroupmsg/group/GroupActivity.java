package com.leejw.mygroupmsg.group;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;

import com.leejw.mygroupmsg.R;
import com.leejw.mygroupmsg.adapter.BaseExpandableAdapter;
import com.leejw.mygroupmsg.contact.Contact;
import com.leejw.mygroupmsg.dao.ContactDao;
import com.leejw.mygroupmsg.dao.GroupDao;
import com.leejw.utils.StringUtil;

public class GroupActivity extends Activity{
	
	private ExpandableListView groupListView;
	
	private ArrayList<Group> groupList;
	private ArrayList<ArrayList<Contact>> childList;
	private ArrayList<Contact> contactList;
	
	private ArrayList<Group> groupInfos;
	
	int groupCnt;
	int groupListCnt;
	LinearLayout linearLayout;

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.av_group);

		groupListView = (ExpandableListView)findViewById(R.id.ex_group_list);
//		linearLayout = (LinearLayout) View.inflate(this, R.layout.av_temptext, null);
//		linearLayout.setVisibility(View.INVISIBLE);

		groupList = new GroupDao().getGroupList(null, this);
		
		if(StringUtil.isNotNull(groupList)){
			groupListCnt = groupList.size();
			
			if(groupListCnt > 0){
				Group groupObj = null;
				String groupId = null;
				
				groupInfos = new ArrayList<Group>();

				if(groupListCnt > 0){
					for(int groupCnt = 0 ; groupCnt < 10; groupCnt++){
						groupInfos.add(groupList.get(groupCnt));				
					}
				}else{
					for(int groupCnt = 0 ; groupCnt < groupListCnt ; groupCnt++){
						groupInfos.add(groupList.get(groupCnt));
					}
				}
				
				int groupInfosCnt = (StringUtil.isNotNull(groupInfos)) ? groupInfos.size() : 0;
				
				if(groupInfosCnt > 0){
					
					childList = new ArrayList<ArrayList<Contact>>();
					
					for(int groupInfoCnt = 0 ; groupInfoCnt < groupInfosCnt ; groupInfoCnt++){
							
						groupObj = (Group)groupList.get(groupInfoCnt);
						groupId = groupObj.getGroupId();
						
						contactList = new ContactDao().getContactList(null, groupId, this);
						
						System.out.println("contactList 사이즈 : " + contactList.size());
						
						if(StringUtil.isNotNull(contactList)){
							childList.add(contactList);						
						}
					}
				}
				
//				// scrol event
//				groupListView.setOnScrollListener(new OnScrollListener() {
//					
//					@Override
//					public void onScrollStateChanged(AbsListView view, int scrollState) {
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void onScroll(AbsListView view, int firstVisibleItem,
//							int visibleItemCount, int totalItemCount) {
//						// TODO Auto-generated method stub
//						if((firstVisibleItem + visibleItemCount) == totalItemCount){
//							
//							if(groupListCnt > groupCnt){
//								linearLayout.setVisibility(View.VISIBLE);
//								new getMoreItems().execute(groupInfos);
//							}else{
//								linearLayout.setVisibility(View.INVISIBLE);
//							}
//							
//						}
//					}
//				});
				groupListView.setAdapter(new BaseExpandableAdapter(this, groupInfos, childList));
			}
		}
		
		
	}
//	private class getMoreItems extends AsyncTask<ArrayList<Group>, Integer, Long> {
//
//		@Override
//		protected Long doInBackground(ArrayList<Group>... arg0) {
//			// TODO Auto-generated method stub
//			Long result = 0L;
//			
//			if(groupListCnt >= groupCnt + 10){
//				int tmpEnd = groupCnt + 10;
//				for(; groupCnt < tmpEnd ; groupCnt++){
//					groupInfos.add(groupList.get(groupCnt));
//				}
//			}else{
//				for(;groupCnt < groupListCnt ; groupCnt++){
//					groupInfos.add(groupList.get(groupCnt));
//				}
//			}
//			
//			try{
//				Thread.sleep(5000);
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//			
//			return null;
//		}
//		protected void onPostExecute(Long result){
//			adapter.notifyDataSetChanged();
//		}
//
//	}
}
