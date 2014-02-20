package com.leejw.mygroupmsg.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.leejw.mygroupmsg.R;
import com.leejw.mygroupmsg.contact.Contact;


public class ContactAdapter extends ArrayAdapter<Contact> {
	public ContactAdapter(Context context, ArrayList<Contact> contacts) {
		super(context, R.layout.av_contact_row, contacts);
		// TODO Auto-generated constructor stub
	}

//	Context context;
//	List<Contact> contactList;
//	private static LayoutInflater inflater = null;
	
//	public ContactAdapter(Context context, List<Contact> contactList) {
//		this.context = context;
//		this.contactList = contactList;
//		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//	}
//
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return contactList.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		// TODO Auto-generated method stub
//		return contactList.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		// TODO Auto-generated method stub
//		return position;
//	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ContactViewHolder holder;

		Contact contactInfo = getItem(position);
		
		if(convertView == null){
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.av_contact_row, null);
			holder = new ContactViewHolder();
			holder.chkBox = (CheckBox)convertView.findViewById(R.id.receiverChkbox);
			holder.receiverNm = (TextView)convertView.findViewById(R.id.receiverName);
			holder.receiverNo = (TextView)convertView.findViewById(R.id.receiverPhoneNo);
			
			convertView.setTag(holder);
		}else{
			holder = (ContactViewHolder)convertView.getTag();
		}
		
		String receiverName = contactInfo.getReceiverName();
		String receiverNumber = contactInfo.getReceiverPhoneNo();
		String receiverInfo = receiverName + ";" + receiverNumber;
		
//		System.out.println("receiverInfo : " + receiverInfo);
		
		holder.chkBox.setText(receiverInfo);
		holder.chkBox.setId(position);
		holder.receiverNm.setText(receiverName);
		holder.receiverNo.setText(receiverNumber);
		
//		View vi = convertView;
//		
//		CheckBox receiverChkBox = null;
//		
//		if(vi == null){
//			vi = inflater.inflate(R.layout.av_contact_row, null);
//			receiverChkBox = (CheckBox)vi.findViewById(R.id.receiverChkbox);
//		}
////		else{
////			ContactViewHolder viewHolder = (ContactViewHolder)vi.getTag();
////			receiverChkBox = viewHolder.chkBox;
////		}
//
//		Contact contactInfo = (Contact)contactList.get(position);
//		
//		String receiverNm = contactInfo.getReceiverName();
//		String phoneNo = contactInfo.getReceiverPhoneNo();
//
//		if(StringUtil.isNotNull(receiverNm) && StringUtil.isNotNull(phoneNo)){
//			
//			try{
//				String receiverId = receiverNm + ";" + phoneNo;
//				receiverChkBox.setText(receiverId);
//				receiverChkBox.setId(position);				
//			}catch(Exception e){
//				e.printStackTrace();
//			}
//			
//		}
		
		return convertView;
	}
	
	// ViewHolder
	// Ref.] http://www.kmshack.kr/346
	static class ContactViewHolder{
		CheckBox chkBox;
		TextView receiverNm;
		TextView receiverNo;
	}
}
