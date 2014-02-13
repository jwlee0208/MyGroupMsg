package com.leejw.mygroup.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.leejw.mygroupmsg.R;
import com.leejw.mygroupmsg.group.Group;

public class GroupAdapter extends ArrayAdapter<Group>{

	public GroupAdapter(Context context, ArrayList<Group> groups) {
		super(context, R.layout.av_group_row, groups);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		GroupViewHolder holder;

		Group groupInfo = getItem(position);
		
		if(convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.av_group_row, null);
			holder = new GroupViewHolder();
			holder.groupName = (TextView)convertView.findViewById(R.id.groupName);
			holder.groupId = (TextView)convertView.findViewById(R.id.groupId);
			convertView.setTag(holder);
		}else{
			holder = (GroupViewHolder)convertView.getTag();
		}
		
		String groupIdStr = groupInfo.getGroupId();
		
		holder.groupName.setText(groupInfo.getGroupTitle());
		holder.groupId.setText(groupIdStr);
		
		return convertView;
	}
	static class GroupViewHolder{
		TextView groupName;
		TextView groupId;
		boolean loaded;
	}
}


