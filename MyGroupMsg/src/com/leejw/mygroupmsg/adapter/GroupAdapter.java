package com.leejw.mygroupmsg.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.leejw.mygroupmsg.R;
import com.leejw.mygroupmsg.contact.Contact;
import com.leejw.mygroupmsg.dao.ContactDao;
import com.leejw.mygroupmsg.group.Group;
import com.leejw.utils.StringUtil;

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
		
		final String groupIdStr = groupInfo.getGroupId();
		
		holder.groupName.setText(groupInfo.getGroupTitle());
		holder.groupId.setText(groupIdStr);
		

//		holder.groupName.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				ContactAdapter contactAdapter;
//				ContactDao contactDao = new ContactDao();
//				List<Contact> contactList = contactDao.getContactList(null, groupIdStr, getContext());
//				ArrayList<Contact> contactInfos = new ArrayList<Contact>();
//				int totalContactListCnt;
//				
//				totalContactListCnt = (StringUtil.isNotNull(contactList)) ? contactList.size() : 0;
//			
//				if(totalContactListCnt > 0){
//					for(int contactCnt = 0 ; contactCnt < 10 ; contactCnt++){
//						contactInfos.add(contactList.get(contactCnt));
//					}
//				}else{
//					for(int contactCnt = 0 ; contactCnt < totalContactListCnt ; contactCnt++){
//						contactInfos.add(contactList.get(contactCnt));
//					}					
//				}
//				
//				contactAdapter = new ContactAdapter(getContext(), contactInfos);
//				
//
//				
//				Toast.makeText(getContext(), "test."+groupIdStr, Toast.LENGTH_LONG).show();
//				return;
//			}
//		});
		
		return convertView;
	}
	/**
	 * ViewHolder의 사용
	 * Ref.] http://www.kmshack.kr/346
	 * @author leejinwon
	 *
	 */
	static class GroupViewHolder{
		TextView groupName;
		TextView groupId;
		boolean loaded;
	}
	

}


