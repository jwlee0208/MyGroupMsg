package com.leejw.mygroupmsg.group;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.leejw.mygroupmsg.R;
import com.leejw.mygroupmsg.contact.Contact;
import com.leejw.mygroupmsg.dao.ContactDao;
import com.leejw.mygroupmsg.dao.GroupDao;
import com.leejw.mygroupmsg.main.MainActivity;
import com.leejw.utils.StringUtil;

public class GroupActivity extends Activity{
	
	private ExpandableListView groupListView;
	
	private ArrayList<Group> groupList;
	private ArrayList<ArrayList<Contact>> childList;
	private ArrayList<Contact> contactList;
	
	private ArrayList<Group> groupInfos;
	
	// ExpandableListView contents
	private ExpandableListAdapter adapter;
	
	private int groupCnt;
	private int groupListCnt;
	private LinearLayout linearLayout;
//	private TextView statusTextView;

	private Context context;
	
	private ArrayList<Contact> selectedContactList;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.av_group);
		
		context = this.getApplicationContext();
//      statusTextView = (TextView) findViewById(R.id.status);
//      statusTextView.setText("idle");
		linearLayout = (LinearLayout) View.inflate(this, R.layout.av_temptext, null);
		groupListView = (ExpandableListView)findViewById(R.id.ex_group_list);
		groupListView.setChoiceMode(ExpandableListView.CHOICE_MODE_MULTIPLE);

		selectedContactList = new ArrayList<Contact>();
		
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
			
			@SuppressLint("NewApi")
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				if(selectedContactList.size() > 0){
//					for(Contact contact : selectedContactList){
//						System.out.println(contact.getReceiverName() + ", " + contact.getReceiverPhoneNo());
//						System.out.println("---------------------------");
//					}
//				}
				Intent intent = new Intent(getBaseContext(), MainActivity.class);
				intent.putExtra("contactList", selectedContactList);
				setResult(2, intent);
				finish();
			}
		});
		
		groupList = new GroupDao().getGroupList(null, this);
		
		if(StringUtil.isNotNull(groupList)){
			groupListCnt = groupList.size();
			
			if(groupListCnt > 0){
				Group groupObj = null;
				String groupId = null;

				groupInfos = new ArrayList<Group>();

				if(groupListCnt > 0){
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
						
						System.out.println("contactList 사이즈 : " + contactList.size());
						
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
				adapter = new BaseExpandableAdapter(this, groupInfos, childList);		
			}
		}

		linearLayout.setVisibility(View.INVISIBLE);

		groupListView.addFooterView(linearLayout);
		groupListView.setAdapter(adapter);
		
		
//		groupListView.setOnChildClickListener(new OnChildClickListener() {
//			
//			@Override
//			public boolean onChildClick(ExpandableListView parent, View v,
//					int groupPosition, int childPosition, long id) {
//				// TODO Auto-generated method stub
//				CheckBox chkbox = (CheckBox)v.findViewById(R.id.receiverChkbox);
//				Toast.makeText(context,chkbox.getText(), Toast.LENGTH_SHORT).show();
//				return false;
//			}
//		});
		
	}

	private class getMoreItems extends AsyncTask<ArrayList<Group>, Integer, Long> {

		@Override
		protected Long doInBackground(ArrayList<Group>... arg0) {
			// TODO Auto-generated method stub
			Long result = 0L;
			Group groupObj = null;
			String groupId;
			
			if(groupListCnt >= groupCnt + 15){
				int tmpEnd = groupCnt + 15;
				for(; groupCnt < tmpEnd ; groupCnt++){
					groupInfos.add(groupList.get(groupCnt));
				}
			}else{
				for(;groupCnt < groupListCnt ; groupCnt++){
					groupInfos.add(groupList.get(groupCnt));
				}
			}

//			int groupInfosCnt = (StringUtil.isNotNull(groupInfos)) ? groupInfos.size() : 0;
//			
//			if(groupInfosCnt > 0){
//				
//				childList = new ArrayList<ArrayList<Contact>>();
//				
//				for(int groupInfoCnt = 0 ; groupInfoCnt < groupInfosCnt ; groupInfoCnt++){
//						
//					groupObj = (Group)groupInfos.get(groupInfoCnt);
//					groupId = groupObj.getGroupId();
//					
//					contactList = new ContactDao().getContactList(null, groupId, context);
//					
//					System.out.println("contactList 사이즈 : " + contactList.size());
//					
//					if(StringUtil.isNotNull(contactList)){
//						childList.add(contactList);						
//					}
//				}
//			}
			
			try{
				Thread.sleep(5000);
			}catch(Exception e){
				e.printStackTrace();
			}
			
			return null;
		}
		protected void onPostExecute(Long result){
			((BaseExpandableListAdapter) adapter).notifyDataSetChanged();
		}
	}
	/**
	 * BaseExpandableAdapter
	 * @author jwlee0208
	 *
	 */
	private class BaseExpandableAdapter extends BaseExpandableListAdapter{

		private ArrayList<Group> groupList;
		private ArrayList<ArrayList<Contact>> childList;
		
		private LayoutInflater inflater;
		
		private ViewHolder viewHolder;
//		private ArrayList<Contact> contactList;
		
		public BaseExpandableAdapter(Context context, ArrayList<Group> groupList,
				ArrayList<ArrayList<Contact>> childList) {
			super();
			this.groupList = groupList;
			this.childList = childList;
			this.inflater = LayoutInflater.from(context);
		}
		
		@Override
		public Contact getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childList.get(groupPosition).get(childPosition);
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = convertView;
			
			if(v == null){
				viewHolder = new ViewHolder();
				v = inflater.inflate(R.layout.av_contact_row, null);
//				v = inflater.inflate(R.layout.av_list_row, null);
//				viewHolder.contact_nm = (TextView)v.findViewById(R.id.tv_contact);
				viewHolder.contact_nm = (TextView)v.findViewById(R.id.receiverChkbox);
				v.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder)v.getTag();
			}
			
			Contact contactInfo = getChild(groupPosition, childPosition);
			String receiverName = contactInfo.getReceiverName();
			String receiverPhoneNo = contactInfo.getReceiverPhoneNo();
			
			String receiverInfo = receiverName + ";" + receiverPhoneNo;
			
			viewHolder.contact_nm.setText(receiverInfo);
			
			
			viewHolder.contact_nm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					CheckBox chkbox = (CheckBox)v.findViewById(R.id.receiverChkbox);
					
//					Toast.makeText(context, "선택값 : "+ chkbox.getText().toString() + ", 선택 여부 : " + chkbox.isChecked(), Toast.LENGTH_SHORT).show();

					updateData(chkbox.getText().toString(), chkbox.isChecked());
//					String[] receiverInfoStrArr = StringUtil.splitStr(chkbox.getText().toString());
					
//					if(receiverInfoStrArr != null){
//						Contact contactObj = new Contact();
//						contactObj.setReceiverName(receiverInfoStrArr[0]);
//						contactObj.setReceiverPhoneNo(receiverInfoStrArr[1]);
//						
//						if(chkbox.isChecked()){
//							selectedContactList.add(contactObj);	
//						}else{
//							selectedContactList.remove(contactObj);
//						}	
//					}
					
//					for(Contact contact : selectedContactList){
//						System.out.println(contact.getReceiverName() + ", " + contact.getReceiverPhoneNo());
//					}
					
				}
			});
			
			return v;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			return childList.get(groupPosition).size();
		}

		@Override
		public Group getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return groupList.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return groupList.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = convertView;
				
			if(v == null){
				viewHolder = new ViewHolder();
//				v = inflater.inflate(R.layout.av_list_row, parent, false);
				v = inflater.inflate(R.layout.av_group_row, parent, false);
				viewHolder.group_nm = (TextView) v.findViewById(R.id.groupName);
				viewHolder.group_id = (TextView) v.findViewById(R.id.groupId);
				v.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder)v.getTag();
			}
			
			// 그룹 열고 닫을 때
			if(isExpanded){
				
			}else{
				
			}
			
			viewHolder.group_nm.setText(getGroup(groupPosition).getGroupTitle());
//			viewHolder.group_nm.setId(groupPosition);
			viewHolder.group_id.setText(getGroup(groupPosition).getGroupId());
//			viewHolder.group_id.setId(groupPosition);
			
			return v;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return true;
		}

		class ViewHolder{
			public TextView group_nm;
			public TextView group_id;
			public TextView contact_nm;
		}
	}
	/**
	 * 연락처 객체에 정보 setting.
	 * @param receiverName
	 * @param receiverPhoneNo
	 * @return
	 */
	@SuppressWarnings("unused")
	private Contact setContact(String receiverName, String receiverPhoneNo){
		Contact contactObj = new Contact();
		contactObj.setReceiverName(receiverName);
		contactObj.setReceiverPhoneNo(receiverPhoneNo);
		
		return contactObj;
	}
	
	/**
	 * 반환할 리스트 객체에 데이터 추가/제거
	 * @param receiverInfo
	 * @param isChecked
	 */
	private void updateData(String receiverInfo, boolean isChecked){
		String receiverInfos[]  = StringUtil.splitStr(receiverInfo.toString());
		Contact contactObj;
		
		String receiverName = receiverInfos[0];
		String cellPhoneNo = receiverInfos[1];
//		String groupId = receiverInfos[2];
		
		int selectedContactListSize = this.selectedContactList.size();
		
		if(receiverInfos.length > 0){
			if(isChecked){
//				System.out.println("IS_CHECKED");
				contactObj = this.setContact(receiverName, cellPhoneNo);
				
				// 행 추가
				selectedContactList.add(contactObj);
				
			}else{
				// 체크박스 체크해제시 오류 해결 요망
				if(selectedContactListSize > 0){
//					System.out.println("IS_UNCHECKED");
					// 왜 삭제가 안될까???
					// ref.] http://stackoverflow.com/questions/16460258/java-lang-indexoutofboundsexception-invalid-index-2-size-is-2
//					selectedContactList.remove(contactObj);
					
					for(int i = 0 ; i < selectedContactListSize ; i++){
						Contact contact = (Contact)selectedContactList.get(i);
							
						if(contact.getReceiverName().equals(receiverName) 
						&& contact.getReceiverPhoneNo().equals(cellPhoneNo)){
							selectedContactList.remove(contact);  	// java.lang.IndexOutOfBoundsException: Invalid index 2, size is 2
							selectedContactListSize--;
						}
					}
				}else{
					// pass
				}
			}			
		}
//		printList();
	}

}
