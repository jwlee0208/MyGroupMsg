package com.leejw.mygroup.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.leejw.mygroupmsg.R;
import com.leejw.mygroupmsg.contact.Contact;
import com.leejw.utils.StringUtil;



public class ContactAdapter extends BaseAdapter {
	Context context;
	List<Contact> contactList;
	private static LayoutInflater inflater = null;
	
	public ContactAdapter(Context context, List<Contact> contactList) {
		this.context = context;
		this.contactList = contactList;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contactList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return contactList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View vi = convertView;
		
		CheckBox receiverChkBox = null;
		
		if(vi == null){
			vi = inflater.inflate(R.layout.av_contact_row, null);
			receiverChkBox = (CheckBox)vi.findViewById(R.id.receiverChkbox);
		}
//		else{
//			ContactViewHolder viewHolder = (ContactViewHolder)vi.getTag();
//			receiverChkBox = viewHolder.chkBox;
//		}

		Contact contactInfo = (Contact)contactList.get(position);
		
		String receiverNm = contactInfo.getReceiverName();
		String phoneNo = contactInfo.getReceiverPhoneNo();

		if(StringUtil.isNotNull(receiverNm) && StringUtil.isNotNull(phoneNo)){
			
			try{
				String receiverId = receiverNm + ";" + phoneNo;
				receiverChkBox.setText(receiverId);
				receiverChkBox.setId(position);				
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		
		
		return vi;
	}
	
	// ViewHolder의 사용
	// Ref.] http://www.kmshack.kr/346
	private static class ContactViewHolder{
		public final CheckBox chkBox;

		@SuppressWarnings("unused")
		public ContactViewHolder(CheckBox chkBox) {
			super();
			this.chkBox = chkBox;
		}
		
	}
}
