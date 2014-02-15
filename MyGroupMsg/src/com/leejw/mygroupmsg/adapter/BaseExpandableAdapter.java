package com.leejw.mygroupmsg.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.leejw.mygroupmsg.R;
import com.leejw.mygroupmsg.contact.Contact;
import com.leejw.mygroupmsg.group.Group;

//public class BaseExpandableAdapter extends BaseExpandableListAdapter{
//
//	private ArrayList<Group> groupList;
//	private ArrayList<ArrayList<Contact>> childList;
//	
//	private LayoutInflater inflater;
//	
//	private ViewHolder viewHolder;
////	private ArrayList<Contact> contactList;
//	
//	public BaseExpandableAdapter(Context context, ArrayList<Group> groupList,
//			ArrayList<ArrayList<Contact>> childList) {
//		super();
//		this.groupList = groupList;
//		this.childList = childList;
//		this.inflater = LayoutInflater.from(context);
//	}
//	
//	@Override
//	public Contact getChild(int groupPosition, int childPosition) {
//		// TODO Auto-generated method stub
//		return childList.get(groupPosition).get(childPosition);
//	}
//
//	@Override
//	public long getChildId(int groupPosition, int childPosition) {
//		// TODO Auto-generated method stub
//		return childPosition;
//	}
//
//	@Override
//	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
//			ViewGroup parent) {
//		// TODO Auto-generated method stub
//		View v = convertView;
//		
//		if(v == null){
//			viewHolder = new ViewHolder();
//			v = inflater.inflate(R.layout.av_contact_row, null);
////			v = inflater.inflate(R.layout.av_list_row, null);
////			viewHolder.contact_nm = (TextView)v.findViewById(R.id.tv_contact);
//			viewHolder.contact_nm = (TextView)v.findViewById(R.id.receiverChkbox);
//			v.setTag(viewHolder);
//		}else{
//			viewHolder = (ViewHolder)v.getTag();
//		}
//		
//		Contact contactInfo = getChild(groupPosition, childPosition);
//		String receiverName = contactInfo.getReceiverName();
//		String receiverPhoneNo = contactInfo.getReceiverPhoneNo();
//		
//		String receiverInfo = receiverName + ";" + receiverPhoneNo;
//		
//		viewHolder.contact_nm.setText(receiverInfo);
//		
//		return v;
//	}
//
//	@Override
//	public int getChildrenCount(int groupPosition) {
//		// TODO Auto-generated method stub
//		return childList.get(groupPosition).size();
//	}
//
//	@Override
//	public Group getGroup(int groupPosition) {
//		// TODO Auto-generated method stub
//		return groupList.get(groupPosition);
//	}
//
//	@Override
//	public int getGroupCount() {
//		// TODO Auto-generated method stub
//		return groupList.size();
//	}
//
//	@Override
//	public long getGroupId(int groupPosition) {
//		// TODO Auto-generated method stub
//		return groupPosition;
//	}
//
//	@Override
//	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//		View v = convertView;
//			
//		if(v == null){
//			viewHolder = new ViewHolder();
////			v = inflater.inflate(R.layout.av_list_row, parent, false);
//			v = inflater.inflate(R.layout.av_group_row, parent, false);
//			viewHolder.group_nm = (TextView) v.findViewById(R.id.groupName);
//			viewHolder.group_id = (TextView) v.findViewById(R.id.groupId);
//			v.setTag(viewHolder);
//		}else{
//			viewHolder = (ViewHolder)v.getTag();
//		}
//		
//		// 그룹 열고 닫을 때
//		if(isExpanded){
//			
//		}else{
//			
//		}
//		
//		viewHolder.group_nm.setText(getGroup(groupPosition).getGroupTitle());
////		viewHolder.group_nm.setId(groupPosition);
//		viewHolder.group_id.setText(getGroup(groupPosition).getGroupId());
////		viewHolder.group_id.setId(groupPosition);
//		
//		return v;
//	}
//
//	@Override
//	public boolean hasStableIds() {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	@Override
//	public boolean isChildSelectable(int groupPosition, int childPosition) {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	class ViewHolder{
//		public TextView group_nm;
//		public TextView group_id;
//		public TextView contact_nm;
//	}
//}
