package com.leejw.mygroupmsg.contact;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.leejw.mygroupmsg.BaseConstants;
import com.leejw.mygroupmsg.R;
import com.leejw.mygroupmsg.adapter.ContactAdapter;
import com.leejw.mygroupmsg.main.MainActivity;
import com.leejw.utils.StringUtil;

public class SelectedContactActivity extends Activity{
	
	ArrayList<Contact> contactList; 
	ArrayList<Contact> returnList;
	ListView selectedContactListView;
	selectedContactAdapter contactAdapter;
			
	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.av_contact_list);
		
		setHeader();
		
		Intent intent = getIntent();
		this.contactList = (ArrayList<Contact>) intent.getSerializableExtra("contactList");
		
		contactAdapter = new selectedContactAdapter(this, contactList);
		
		selectedContactListView = (ListView)findViewById(R.id.contactList);
		selectedContactListView.setAdapter(contactAdapter);		
	}
	
	private void setHeader() {
		// 버튼 설정
		Button cancelBtn = (Button) findViewById(R.id.select_cancel);
		Button okBtn = (Button) findViewById(R.id.select_ok);
		/**
		 * case to click cancel button.
		 */
		cancelBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getBaseContext(), MainActivity.class);
				intent.putExtra("contactList", contactList);
				setResult(3, intent);
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
				intent.putExtra("contactList", contactList);
				setResult(BaseConstants.PAGE_GO_TO_SELECTED_LIST, intent);
				finish();
			}
		});
	}
	/**
	 * 어댑터 설정
	 * @author leejinwon
	 *
	 */
	class selectedContactAdapter extends ArrayAdapter<Contact>{

		public selectedContactAdapter(Context context, ArrayList<Contact> contacts) {
			super(context, R.layout.av_selected_contact_row, contacts);
			// TODO Auto-generated constructor stub
		}
		
		public View getView(int position, View convertView, ViewGroup parent){
			
			final SelectedContactViewHolder holder;
			
			final Contact contactInfo = getItem(position);
			
			if(convertView == null){
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.av_selected_contact_row, null);
				holder = new SelectedContactViewHolder();
				holder.receiverNm = (TextView)convertView.findViewById(R.id.selectedReceiverName);
				holder.receiverNo = (TextView)convertView.findViewById(R.id.selectedReceiverPhoneNo);
				holder.deleteBtn = (Button)convertView.findViewById(R.id.deleteBtn);
				
				convertView.setTag(holder);
			}else{
				holder = (SelectedContactViewHolder)convertView.getTag();
			}
			
			String receiverName = contactInfo.getReceiverName();
			String receiverNumber = contactInfo.getReceiverPhoneNo();

			holder.receiverNm.setText(receiverName);
			holder.receiverNo.setText(receiverNumber);	
			
			holder.deleteBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					String deleteName = holder.receiverNm.getText().toString();
					String deleteNumber = holder.receiverNo.getText().toString();
					
					deleteRow(deleteName, deleteNumber);
					
					// ref.] 안드로이드 리스트 뷰 버튼 클릭시 ROW INVISIBLE
					// http://blog.naver.com/PostView.nhn?blogId=buddy213&logNo=10114576169
					contactAdapter.remove(contactInfo);
					contactAdapter.notifyDataSetChanged();					
				}
			});
			return convertView;
		}
	}
	
	// ref.] ViewHolder
	// http://www.ruinnel.net/post/improve-listview-adapter-performance/
	static class SelectedContactViewHolder{
		TextView receiverNm;
		TextView receiverNo;
		Button deleteBtn;
		
	}
	
	private void deleteRow(String receiverName, String receiverNumber){
		if(StringUtil.isNotNull(contactList)){
			int contactListSize = contactList.size();
			
			if(contactListSize > 0){
				Contact contactObj;
				for(int i = 0 ; i < contactListSize ; i++){
					contactObj = (Contact)contactList.get(i);
					
					String cReceiverNm = contactObj.getReceiverName();
					String cReceiverNo = contactObj.getReceiverPhoneNo();
					
					if(receiverName.equals(cReceiverNm) && receiverNumber.equals(cReceiverNo)){
						this.contactList.remove(i);	
						contactListSize--;
					}
				}
			}
		}
	}
}
