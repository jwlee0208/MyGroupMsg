package com.leejw.mygroupmsg.group;

import java.util.ArrayList;

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
		// 상단 버튼 영역 설정
		setHeader();

		groupList = new GroupDao().getGroupList(null, this);

		if (StringUtil.isNotNull(groupList)) {
			groupListCnt = groupList.size();

			Group groupObj = null;
			String groupId = null;

			groupInfos = new ArrayList<Group>();
			childInfos = new ArrayList<ArrayList<Contact>>();

			if (groupListCnt > 15) {
				for (groupCnt = 0; groupCnt < 15; groupCnt++) {

					groupObj = (Group) groupList.get(groupCnt);
					groupId = groupObj.getGroupId();

					contactList = new ContactDao().getContactList(null, groupId, this);

					if (StringUtil.isNotNull(contactList)) {
						childInfos.add(contactList);
						groupObj.setContactList(contactList);
					}
					groupInfos.add(groupObj);

				}
				
				
				for(int j = 0 ; j < groupInfos.size() ; j++){
					Group temp = (Group)groupInfos.get(j);
//					System.out.println("groupInfo : " + temp.getGroupTitle() + ", " + temp.getGroupId());
				}
			} else {
System.out.println("22222");				
				for (groupCnt = 0; groupCnt < groupListCnt; groupCnt++) {
					groupObj = (Group) groupList.get(groupCnt);

					groupId = groupObj.getGroupId();

					contactList = new ContactDao().getContactList(null, groupId, this);

					if (StringUtil.isNotNull(contactList)) {
						childInfos.add(contactList);
						groupObj.setContactList(contactList);
					}
					groupInfos.add(groupObj);
				}
			}


			// }
		}

		adapter = new BaseExpandableAdapter(this, groupInfos, childInfos);

		groupListView = (ExpandableListView) findViewById(R.id.ex_group_list);
		// groupListView.setChoiceMode(ExpandableListView.CHOICE_MODE_MULTIPLE);

		linearLayout = (LinearLayout) View.inflate(this, R.layout.av_temptext, null);
		linearLayout.setVisibility(View.INVISIBLE);
		groupListView.addFooterView(linearLayout);
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
				
//				 System.out.println(firstVisibleItem + ", " + visibleItemCount + ", " + totalItemCount);
//				
				if ((firstVisibleItem + visibleItemCount) == totalItemCount) {

					if (groupListCnt > groupCnt) {
						linearLayout.setVisibility(View.VISIBLE);
						new getMoreItems().execute(groupInfos);
					} else {
						linearLayout.setVisibility(View.INVISIBLE);
					}
				}
			}
		});		

	}

	private class getMoreItems extends
			AsyncTask<ArrayList<Group>, Integer, Long> {

		@Override
		protected Long doInBackground(ArrayList<Group>... arg0) {
			// TODO Auto-generated method stub
			Long result = 0L;

			Group groupObj = null;
			String groupId = null;

			// childInfos = new ArrayList<ArrayList<Contact>>();

			if (groupListCnt >= groupCnt + 15) {

				int tmpEnd = groupCnt + 15;
				for (; groupCnt < tmpEnd; groupCnt++) {

					groupObj = (Group) groupList.get(groupCnt);

					groupId = groupObj.getGroupId();

					contactList = new ContactDao().getContactList(null, groupId, getBaseContext());

					if (StringUtil.isNotNull(contactList) && contactList.size() > 0) {

						childInfos.add(contactList);
						groupObj.setContactList(contactList);
					}
					groupInfos.add(groupObj);
				}
			} else {

				for (; groupCnt < groupListCnt; groupCnt++) {
					groupObj = (Group) groupList.get(groupCnt);
					groupInfos.add(groupObj);

					groupId = groupObj.getGroupId();

					contactList = new ContactDao().getContactList(null, groupId, getBaseContext());

					if (StringUtil.isNotNull(contactList) && contactList.size() > 0) {
						childInfos.add(contactList);
					}
				}
			}

			try {
				Thread.sleep(3000);
			} catch (Exception e) {
				e.printStackTrace();
			}

			return null;
		}

		protected void onPostExecute(Long result) {
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
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v = convertView;

			if (v == null) {
				viewHolder = new ViewHolder();
				v = inflater.inflate(R.layout.av_contact_row, null);
				viewHolder.contact_nm = (TextView) v
						.findViewById(R.id.receiverChkbox);
				v.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) v.getTag();
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
					CheckBox chkbox = (CheckBox) v.findViewById(R.id.receiverChkbox);

					// Toast.makeText(context, "선택값 : "+
					// chkbox.getText().toString() + ", 선택 여부 : " +
					// chkbox.isChecked(), Toast.LENGTH_SHORT).show();

					updateData(chkbox.getText().toString(), chkbox.isChecked());
				}
			});

			return v;
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
					contactListSize = (StringUtil.isNotNull(contactArrList)) ? contactArrList
							.size() : 0;

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
				viewHolder.group_nm = (TextView) v.findViewById(R.id.groupName);
				viewHolder.group_id = (TextView) v.findViewById(R.id.groupId);
				v.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) v.getTag();
			}

			// 그룹 열고 닫을 때
			if (isExpanded) {

			} else {

			}

			viewHolder.group_nm
					.setText(getGroup(groupPosition).getGroupTitle());
			viewHolder.group_id.setText(getGroup(groupPosition).getGroupId());

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
			public TextView group_nm;
			public TextView group_id;
			public TextView contact_nm;
		}
	}

	/**
	 * 연락처 객체에 정보 setting.
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
	 * 반환할 리스트 객체에 데이터 추가/제거
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

				// 행 추가
				selectedContactList.add(contactObj);

			} else {
				// 체크박스 체크해제시 오류 해결 요망
				if (selectedContactListSize > 0) {
					// System.out.println("IS_UNCHECKED");
					// 왜 삭제가 안될까???
					// ref.]
					// http://stackoverflow.com/questions/16460258/java-lang-indexoutofboundsexception-invalid-index-2-size-is-2
					// selectedContactList.remove(contactObj);

					for (int i = 0; i < selectedContactListSize; i++) {
						Contact contact = (Contact) selectedContactList.get(i);

						if (contact.getReceiverName().equals(receiverName)
								&& contact.getReceiverPhoneNo().equals(
										cellPhoneNo)) {
							selectedContactList.remove(contact); // java.lang.IndexOutOfBoundsException:
																	// Invalid
																	// index 2,
																	// size is 2
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
	 * 상단 버튼 영역 설정
	 */
	private void setHeader() {
		// 상단 버튼
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
				setResult(2, intent);
				finish();
			}
		});
	}
}
