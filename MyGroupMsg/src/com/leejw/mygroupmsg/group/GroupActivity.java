package com.leejw.mygroupmsg.group;

import java.util.ArrayList;
import java.util.HashSet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
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

import com.leejw.mygroupmsg.BaseConstants;
import com.leejw.mygroupmsg.R;
import com.leejw.mygroupmsg.contact.Contact;
import com.leejw.mygroupmsg.dao.ContactDao;
import com.leejw.mygroupmsg.dao.GroupDao;
import com.leejw.mygroupmsg.dao.PhoneDao;
import com.leejw.mygroupmsg.main.MainActivity;
import com.leejw.utils.StringUtil;

public class GroupActivity extends Activity {

	private ExpandableListView groupListView;

	private ArrayList<Group> groupList;
	private ArrayList<ArrayList<Contact>> childInfos;
	private ArrayList<Contact> contactList;

	private ArrayList<Group> groupInfos;

	// ExpandableListView contents
	private ExpandableListAdapter adapter;

	private int groupCnt;
	private int groupListCnt;
	private LinearLayout linearLayout;
	// private TextView statusTextView;

	private Context context;

	private ArrayList<Contact> selectedContactList;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.av_group);

		context = this.getApplicationContext();

		selectedContactList = new ArrayList<Contact>();
		// ������ ������ ������ ������
		setHeader();

		groupList = new GroupDao().getGroupList(null, this);

		if (StringUtil.isNotNull(groupList)) {
			groupListCnt = groupList.size();

			Group groupObj = null;
			String groupId = null;

			groupInfos = new ArrayList<Group>();
			childInfos = new ArrayList<ArrayList<Contact>>();

			if (groupListCnt > 15) {
				this.forLoopGroupInfos(0, 15, groupList);
//				for (groupCnt = 0; groupCnt < 15; groupCnt++) {
//
//					groupObj = (Group) groupList.get(groupCnt);
//					groupId = groupObj.getGroupId();
//
//					contactList = new ContactDao().getContactList(null, groupId, this);
//
//					if (StringUtil.isNotNull(contactList)) {
//						childInfos.add(contactList);
//						groupObj.setContactList(contactList);
//						
//						if(contactList.size() > 0){
//							groupInfos.add(groupObj);
//						}
//					}
//				}				
			} else {
				this.forLoopGroupInfos(0, groupListCnt, groupList);
				
//				for (groupCnt = 0; groupCnt < groupListCnt; groupCnt++) {
//					groupObj = (Group) groupList.get(groupCnt);
//
//					groupId = groupObj.getGroupId();
//
//					contactList = new ContactDao().getContactList(null, groupId, this);
//
//					if (StringUtil.isNotNull(contactList)) {
//						childInfos.add(contactList);
//						groupObj.setContactList(contactList);
//						
//						if(contactList.size() > 0){
//							groupInfos.add(groupObj);
//						}
//					}
//				}
			}
		}

		adapter = new BaseExpandableAdapter(this, groupInfos, childInfos);

		groupListView = (ExpandableListView) findViewById(R.id.ex_group_list);
		groupListView.setChoiceMode(ExpandableListView.CHOICE_MODE_MULTIPLE);

//		linearLayout = (LinearLayout) View.inflate(this, R.layout.av_temptext, null);
//		linearLayout.setVisibility(View.INVISIBLE);
//		
//		groupListView.addFooterView(linearLayout);
		groupListView.setAdapter(adapter);

		// scroll event
		groupListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@SuppressWarnings("unchecked")
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
								
				if ((firstVisibleItem + visibleItemCount) == totalItemCount) {

					if (groupListCnt > groupCnt) {
//						linearLayout.setVisibility(View.VISIBLE);
						
						new getMoreItems().execute(groupInfos);
					} else {
//						linearLayout.setVisibility(View.INVISIBLE);
					}
				}
			}
		});		

	}
	/**
	 * forLoop
	 * @param grpCnt
	 * @param grpListCnt
	 * @param grpList
	 */
	public void forLoopGroupInfos(int grpCnt, int grpListCnt, ArrayList<Group> grpList){
		Group groupObj;
		String groupId;
		
		for (groupCnt = grpCnt; groupCnt < grpListCnt; groupCnt++) {
			int duplChk = 0;
			
			groupObj = (Group) groupList.get(groupCnt);
			groupId = groupObj.getGroupId();

			if(StringUtil.isNotNull(groupInfos)){
				
				if(groupInfos.contains(groupObj))
					duplChk++;
				
//				int groupInfosSize = groupInfos.size();
//				
//				if(groupInfosSize > 0 ){
//					for(int ii = 0 ; ii < groupInfosSize ; ii++){
//						Group compareGroupObj = (Group)groupInfos.get(ii);
//						if(compareGroupObj.getGroupId() == groupId 
//						&& compareGroupObj.getGroupTitle() == groupObj.getGroupTitle()){
//							duplChk++;
//						}
//					}					
//				}

				if(duplChk > 0){
					// pass
				}else{
					contactList = new ContactDao().getContactList(null, groupId, this);

					if (StringUtil.isNotNull(contactList)) {
						childInfos.add(contactList);
						groupObj.setContactList(contactList);
						
						if(contactList.size() > 0){
							groupInfos.add(groupObj);
						}
					}					
				}
			}
			
//			HashSet<Group> removedDuplGroup = new HashSet<Group>(groupInfos);
//			groupInfos = new ArrayList<Group>(removedDuplGroup);
			
//			contactList = new ContactDao().getContactList(null, groupId, this);
//
//			if (StringUtil.isNotNull(contactList)) {
//				childInfos.add(contactList);
//				groupObj.setContactList(contactList);
//				
//				if(contactList.size() > 0){
//					groupInfos.add(groupObj);
//				}
//			}
		}				

	}
	
	private class getMoreItems extends
			AsyncTask<ArrayList<Group>, Integer, Long> {
		// ref.] http://mainia.tistory.com/709
		ProgressDialog progress;
		/**
		 * Excuted Method before Excution doInBackground() Method.
		 */
		protected void onPreExecute() {
			progress = new ProgressDialog(GroupActivity.this);
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.setTitle("");
			progress.setMessage("Loading...");
			progress.show();
			super.onPreExecute();
		}
		
		/**
		 * It excuted method where executed execute() method.
		 */
		@Override
		protected Long doInBackground(ArrayList<Group>... arg0) {
			
			final int taskCnt = 0;
//			publishProgress("max", Integer.toString(taskCnt));
			
			// Scroll 후 로딩 시 화면을 막고 로딩 스피너를 보여주도록.
			// ref.] http://www.bemga.com/05-20-2013/android-show-loading-spinner.html
//			ProgressDialog progress = new ProgressDialog(GroupActivity.this);
//			progress.setTitle("");
//			progress.setMessage("Loading...");
//			progress.show();
//			
//			progress.dismiss();
			
			// TODO Auto-generated method stub
			Long result = 0L;

			Group groupObj = null;
			String groupId = null;

			// childInfos = new ArrayList<ArrayList<Contact>>();

			if (groupListCnt >= groupCnt + 15) {

				int tmpEnd = groupCnt + 15;				
				for (; groupCnt < tmpEnd; groupCnt++) {
					int duplChk = 0 ;
					groupObj = (Group) groupList.get(groupCnt);
					groupId = groupObj.getGroupId();

					if(groupInfos.contains(groupObj))
						duplChk++;
					
						
//					int groupInfosSize = groupInfos.size();
//					
//					if(groupInfosSize > 0 ){
//						for(int ii = 0 ; ii < groupInfosSize ; ii++){
//							Group compareGroupObj = (Group)groupInfos.get(ii);
//							if(compareGroupObj.getGroupId() == groupId 
//							&& compareGroupObj.getGroupTitle() == groupObj.getGroupTitle()){
//								duplChk++;
//							}
//						}					
//					}

					if(duplChk > 0){
						// pass
					}else{
						contactList = new ContactDao().getContactList(null, groupId, getBaseContext());

						if (StringUtil.isNotNull(contactList) && contactList.size() > 0) {
							childInfos.add(contactList);
							groupObj.setContactList(contactList);
							groupInfos.add(groupObj);
						}
					}
//					contactList = new ContactDao().getContactList(null, groupId, getBaseContext());
//
//					if (StringUtil.isNotNull(contactList) && contactList.size() > 0) {
//						childInfos.add(contactList);
//						groupObj.setContactList(contactList);
//						groupInfos.add(groupObj);
//					}
				}
			} else {

				for (; groupCnt < groupListCnt; groupCnt++) {
					int duplChk = 0;
					groupObj = (Group) groupList.get(groupCnt);
					groupId = groupObj.getGroupId();

					if(groupInfos.contains(groupObj))
						duplChk++;
//					int groupInfosSize = groupInfos.size();
//					
//					if(groupInfosSize > 0 ){
//						for(int ii = 0 ; ii < groupInfosSize ; ii++){
//							Group compareGroupObj = (Group)groupInfos.get(ii);
//							if(compareGroupObj.getGroupId() == groupId 
//							&& compareGroupObj.getGroupTitle() == groupObj.getGroupTitle()){
//								duplChk++;
//							}
//						}					
//					}

					if(duplChk > 0){
						// pass
					}else{
						contactList = new ContactDao().getContactList(null, groupId, getBaseContext());

						if (StringUtil.isNotNull(contactList) && contactList.size() > 0) {
							childInfos.add(contactList);
							groupObj.setContactList(contactList);
							groupInfos.add(groupObj);
						}
					}
//					contactList = new ContactDao().getContactList(null, groupId, getBaseContext());
//
//					if (StringUtil.isNotNull(contactList) && contactList.size() > 0) {
//						childInfos.add(contactList);		
//						groupObj.setContactList(contactList);
//						groupInfos.add(groupObj);
//					}
				}
			}

//			HashSet<Group> removedDuplGroup = new HashSet<Group>(groupInfos);
//			groupInfos = new ArrayList<Group>(removedDuplGroup);
			
			
			try {				
				Thread.sleep(1000);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Long result) {
			progress.dismiss();
			((BaseExpandableAdapter) adapter).notifyDataSetChanged();
		}
	}

	/**
	 * BaseExpandableAdapter
	 * 
	 * @author jwlee0208
	 * 
	 */
	private class BaseExpandableAdapter extends BaseExpandableListAdapter {

		private ArrayList<Group> grpList;
		private ArrayList<ArrayList<Contact>> childList;
		private LayoutInflater inflater;
		private ViewHolder viewHolder;

		public BaseExpandableAdapter(Context context, ArrayList<Group> grpList, ArrayList<ArrayList<Contact>> ctList) {
			super();
			this.grpList = grpList;
			// this.childList = this.setChildList(grpList);
			this.childList = ctList;
			this.inflater = LayoutInflater.from(context);			
		}

		// private ArrayList<ArrayList<Contact>> setChildList(ArrayList<Group>
		// grpList){
		//
		// ArrayList<ArrayList<Contact>> tempList = new
		// ArrayList<ArrayList<Contact>>();
		//
		// if(StringUtil.isNotNull(grpList) && grpList.size() > 0){
		// for(Group group : grpList){
		// ArrayList<Contact> contactList = group.getContactList();
		// if(StringUtil.isNotNull(contactList)){
		//
		// tempList.add(contactList);
		// }
		// }
		// }
		// return tempList;
		// }

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
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = convertView;

//			if (v == null) {
				viewHolder = new ViewHolder();
				v = inflater.inflate(R.layout.av_contact_row, null);
				viewHolder.contact_nm  = (CheckBox) v.findViewById(R.id.receiverChkbox);
				viewHolder.receiver_nm = (TextView) v.findViewById(R.id.receiverName);
				viewHolder.receiver_no = (TextView) v.findViewById(R.id.receiverPhoneNo);
				
//				viewHolder.contact_nm.setChecked(false);
				
//				v.setTag(viewHolder);
//			} else {
//				viewHolder = (ViewHolder) v.getTag();
////				viewHolder.contact_nm.setChecked(viewHolder.contact_nm.isChecked());
//			}

//			viewHolder.contact_nm.setChecked(false);
			
			Contact contactInfo = getChild(groupPosition, childPosition);
			
			long contactId = contactInfo.getContactId();
			
			System.out.println("contactId : "+ contactId);
			
			// 전화번호 설정 위한 phoneDao 호출
			PhoneDao phoneDao = new PhoneDao();
			Contact additionalInfo = phoneDao.getPhoneInfo(contactId, context);
			
			if(additionalInfo != null){
				contactInfo.setPhoneNoType(additionalInfo.getPhoneNoType());
				contactInfo.setReceiverPhoneNo(additionalInfo.getReceiverPhoneNo());
				contactInfo.setPhoneNoType(additionalInfo.getPhoneNoType());
				System.out.println("additionalInfo : " + additionalInfo.getReceiverPhoneNo() + ", " + additionalInfo.getPhoneNoType());
			}
			
			String receiverName = contactInfo.getReceiverName();
			String receiverPhoneNo = contactInfo.getReceiverPhoneNo();
			String receiverInfo = receiverName + ";" + receiverPhoneNo;
			int phoneType = contactInfo.getPhoneNoType();
			// 전화번호가 mobile이 아닌 경우는 체크박스 비활성화
			if(!isMobileNo(phoneType)){
				viewHolder.contact_nm.setEnabled(false);
			}
			
			viewHolder.contact_nm.setText(receiverInfo);
			viewHolder.receiver_nm.setText(receiverName);
			viewHolder.receiver_no.setText(receiverPhoneNo);
			
			viewHolder.contact_nm.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

//					System.out.println("RR : " + viewHolder.contact_nm.getText().toString());
					
					CheckBox chkBox = (CheckBox)v;
					
					boolean isChecked = chkBox.isChecked();
//					chkBox.setChecked(isChecked);
					updateData(chkBox.getText().toString(), isChecked);
					
				}
			});
			
			return v;
		}

		private boolean isMobileNo(int type){
			boolean isMobile = false;
			if(type == 2)
				isMobile = true;
			return isMobile;
		}
		
		@Override
		public int getChildrenCount(int groupPosition) {
			// TODO Auto-generated method stub
			int contactListSize = 0;

			// ArrayList<ArrayList<Contact>> temp = this.childList;

			if (StringUtil.isNotNull(this.childList)) {

				int childListSize = this.childList.size();

				if (childListSize > 0) {
					ArrayList<Contact> contactArrList = this.childList.get(groupPosition);
					contactListSize = (StringUtil.isNotNull(contactArrList)) ? contactArrList.size() : 0;

					// System.out.println("1-1 contactListSIze : " +
					// contactListSize);
				} else {
					// System.out.println("1-2 contactListSIze : " +
					// contactListSize);
				}
			} else {
				// System.out.println("2 contactListSIze : " + contactListSize);
			}

			return contactListSize;
		}

		@Override
		public Group getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return grpList.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			// TODO Auto-generated method stub
			return grpList.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = convertView;
			
			if (v == null) {
				viewHolder = new ViewHolder();
				v = inflater.inflate(R.layout.av_group_row, parent, false);
				viewHolder.group_select = (TextView) v.findViewById(R.id.groupSelect);
				viewHolder.group_nm = (TextView) v.findViewById(R.id.groupName);
				viewHolder.group_id = (TextView) v.findViewById(R.id.groupId);
				viewHolder.group_child_cnt = (TextView) v.findViewById(R.id.groupChildCnt);
				viewHolder.group_id.setVisibility(View.INVISIBLE);
				v.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) v.getTag();
			}

			// ������ ������ ������ ���
			if (isExpanded) {
				viewHolder.group_select.setText("∧");
			} else {
				viewHolder.group_select.setText("∨");
			}

			Group groupObj = getGroup(groupPosition);
			ArrayList<Contact> tempContactList = (ArrayList<Contact>) groupObj.getContactList();
			viewHolder.group_nm.setText(groupObj.getGroupTitle());
			viewHolder.group_id.setText(groupObj.getGroupId());
			viewHolder.group_child_cnt.setText("(" + ((StringUtil.isNotNull(tempContactList)) ? tempContactList.size() : 0) + ")");

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

		class ViewHolder {
			public TextView group_select;
			public TextView group_nm;
			public TextView group_id;
			public TextView group_child_cnt;
			public CheckBox contact_nm;
			public TextView receiver_nm;
			public TextView receiver_no;
		}
	}

	/**
	 * ��������� ��������� ������ setting.
	 * 
	 * @param receiverName
	 * @param receiverPhoneNo
	 * @return
	 */
	@SuppressWarnings("unused")
	private Contact setContact(String receiverName, String receiverPhoneNo) {
		Contact contactObj = new Contact();
		contactObj.setReceiverName(receiverName);
		contactObj.setReceiverPhoneNo(receiverPhoneNo);

		return contactObj;
	}

	/**
	 * ��������� ��������� ��������� ��������� ������/������
	 * 
	 * @param receiverInfo
	 * @param isChecked
	 */
	private void updateData(String receiverInfo, boolean isChecked) {
		String receiverInfos[] = StringUtil.splitStr(receiverInfo.toString());
		Contact contactObj;

		String receiverName = receiverInfos[0];
		String cellPhoneNo = receiverInfos[1];
		// String groupId = receiverInfos[2];

		int selectedContactListSize = this.selectedContactList.size();

		if (receiverInfos.length > 0) {
			if (isChecked) {
				// System.out.println("IS_CHECKED");
				contactObj = this.setContact(receiverName, cellPhoneNo);

				// ��� ������
				selectedContactList.add(contactObj);

			} else {
				// ������������ ��������������� ������ ������ ������
				if (selectedContactListSize > 0) {
					// System.out.println("IS_UNCHECKED");
					// ��� ��������� ���������???
					// ref.]
					// http://stackoverflow.com/questions/16460258/java-lang-indexoutofboundsexception-invalid-index-2-size-is-2
					// selectedContactList.remove(contactObj);

					for (int i = 0; i < selectedContactListSize; i++) {
						Contact contact = (Contact) selectedContactList.get(i);

						if (contact.getReceiverName().equals(receiverName)
								&& contact.getReceiverPhoneNo().equals(
										cellPhoneNo)) {
							selectedContactList.remove(contact);
							selectedContactListSize--;
						}
					}
				} else {
					// pass
				}
			}
		}
	}

	/**
	 * ������ ������ ������ ������
	 */
	private void setHeader() {
		// ������ ������
		Button cancelBtn = (Button) findViewById(R.id.cancel);
		Button okBtn = (Button) findViewById(R.id.ok);
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
				Intent intent = new Intent(getBaseContext(), MainActivity.class);
				intent.putExtra("contactList", selectedContactList);
				setResult(BaseConstants.PAGE_GO_TO_GROUP, intent);
				finish();
			}
		});
	}
}
