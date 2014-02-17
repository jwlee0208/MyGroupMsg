package com.leejw.mygroupmsg.group;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
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
	
	private ExpandableListAdapter adapter;
	
	private int groupCnt;
	private int groupListCnt;
	private LinearLayout linearLayout;
//	private TextView statusTextView;

	private Context context;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.av_group);
		
		context = this.getApplicationContext();
//      statusTextView = (TextView) findViewById(R.id.status);
//      statusTextView.setText("idle");
		linearLayout = (LinearLayout) View.inflate(this, R.layout.av_temptext, null);
		groupListView = (ExpandableListView)findViewById(R.id.ex_group_list);
		groupListView.setChoiceMode(ExpandableListView.CHOICE_MODE_MULTIPLE);

		
		// 상단 버튼
		Button cancelBtn = (Button)findViewById(R.id.cancel);
		Button okBtn = (Button)findViewById(R.id.ok);
		/**
		 * case to click cancel button.
		 */
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		/**
		 * case to click ok button.
		 */
		okBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				groupListView.get
			}
		});
		
		
		
		groupList = new GroupDao().getGroupList(null, this);
		
		if(StringUtil.isNotNull(groupList)){
			groupListCnt = groupList.size();
			
			if(groupListCnt > 0){
				Group groupObj = null;
				String groupId = null;
				
				groupInfos = new ArrayList<Group>();

				if(groupListCnt > 15){
					for(int groupCnt = 0 ; groupCnt < 15; groupCnt++){
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
						
//						System.out.println("contactList 사이즈 : " + contactList.size());
						
						if(StringUtil.isNotNull(contactList)){
							childList.add(contactList);						
						}
					}
				}
				
				// scrol event
				groupListView.setOnScrollListener(new OnScrollListener() {
					
					@Override
					public void onScrollStateChanged(AbsListView view, int scrollState) {
						// TODO Auto-generated method stub
						
					}
					
					@SuppressWarnings("unchecked")
					@Override
					public void onScroll(AbsListView view, int firstVisibleItem,
							int visibleItemCount, int totalItemCount) {
						// TODO Auto-generated method stub
						if((firstVisibleItem + visibleItemCount) == totalItemCount){
							
							if(groupListCnt > groupCnt){
								linearLayout.setVisibility(View.VISIBLE);
								new getMoreItems().execute(groupInfos);
							}else{
								linearLayout.setVisibility(View.INVISIBLE);
							}
							
						}
					}
				});
//				adapter = new BaseExpandableAdapter(this, groupInfos, childList);				
			}
//			adapter = new BaseExpandableAdapter(this, groupInfos, childList);
		}

		linearLayout.setVisibility(View.INVISIBLE);

		adapter = new BaseExpandableAdapter(this, groupInfos, childList);
		groupListView.addFooterView(linearLayout);
		groupListView.setAdapter(adapter);
		
	}

	private class getMoreItems extends AsyncTask<ArrayList<Group>, Integer, Long> {

		@Override
		protected Long doInBackground(ArrayList<Group>... groupArr) {
			// TODO Auto-generated method stub
			Long result = 0L;
//			Group groupObj = null;
//			String groupId;
			
			if(groupListCnt >= groupCnt + 15){
				int tmpEnd = groupCnt + 15;
				for(; groupCnt < tmpEnd ; groupCnt++){
					groupInfos.add(groupList.get(groupCnt));
//					groupObj = (Group)groupArr[groupCnt].get(groupCnt);
//					groupInfos.add(groupObj);
				}
			}else{
				for(;groupCnt < groupListCnt ; groupCnt++){
					groupInfos.add(groupList.get(groupCnt));
				}
			}

			try{
				Thread.sleep(3000);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return null;
		}
		protected void onPostExecute(Long result){
			((BaseExpandableListAdapter) adapter).notifyDataSetChanged();
		}

	}
}
