package com.leejw.mygroupmsg.main;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.leejw.mygroupmsg.R;
import com.leejw.mygroupmsg.contact.Contact;
import com.leejw.mygroupmsg.contact.SelectedContactActivity;
import com.leejw.mygroupmsg.group.GroupActivity;
import com.leejw.utils.StringUtil;

public class MainActivity extends Activity{
	
	ArrayList<Contact> contactList;
	EditText textView;
	
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.av_main);
		
		textView = (EditText)findViewById(R.id.editText1);
				
		setHeader();
		
		setFooter();

		
	}
	
	@SuppressWarnings("unchecked")
	protected void onActivityResult(int requestCode, int resultCode, Intent Data){
		super.onActivityResult(requestCode, resultCode, Data);
		
		if(requestCode == 2){
			Toast.makeText(getApplicationContext(), "수신자 선택 창 전환...", Toast.LENGTH_LONG).show();
			
			if(resultCode == 2){
				// ref.] http://blog.daum.net/haha25/5387851
				this.contactList = (ArrayList<Contact>)Data.getSerializableExtra("contactList");
				
				if(contactList != null){
					int contactListSize = contactList.size();
					
					if(contactListSize < 1){
						Toast.makeText(getApplicationContext(), "선택된 수신자가 없습니다.", Toast.LENGTH_LONG).show();
					}
				}
			}
		}else if(requestCode == 3){
			this.contactList = (ArrayList<Contact>)Data.getSerializableExtra("contactList");
			
			if(contactList != null){
				int contactListSize = contactList.size();
				
				if(contactListSize < 1){
					Toast.makeText(getApplicationContext(), "선택된 수신자가 없습니다.", Toast.LENGTH_LONG).show();
				}
			}
			
		}
	}
	/**
	 * 상단 버튼 영역 설정
	 */
	private void setHeader(){
		Button button01 = (Button)findViewById(R.id.button1);
		Button button02 = (Button)findViewById(R.id.button2);
		
		/**
		 * 초기화
		 */
		button01.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Initiate...", Toast.LENGTH_LONG).show();
				Intent intent = getIntent();
				finish();
				startActivity(intent);
			}
		});
		
		/**
		 * 수신자 선택
		 */
		button02.setOnClickListener(new OnClickListener() {			
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
//				Intent intent = new Intent(getBaseContext(), ContactActivity.class);
				Intent intent = new Intent(getBaseContext(), GroupActivity.class);
				startActivityForResult(intent, 2);
			}
		});
	}
	
	private void setFooter(){
		Button sendBtn = (Button)findViewById(R.id.sendBtn);
		Button modifyReceiverBtn = (Button)findViewById(R.id.modifyReceiverBtn);

		/**
		 *  전송
		 */
		sendBtn.setOnClickListener(new OnClickListener(){
			public void onClick(View v){
				Toast.makeText(getApplicationContext(), "Sending...", Toast.LENGTH_LONG).show();
				
				System.out.println("textView.getText().toString() : " + textView.getText().toString());
				
				String msgStr = textView.getText().toString();
				if(msgStr.isEmpty()){
					Toast.makeText(getApplicationContext(), "No inserted message.", Toast.LENGTH_LONG).show();
					return;
				}
				
				if(contactList == null || contactList.size() < 1){
					Toast.makeText(getApplicationContext(), "No selected receivers.", Toast.LENGTH_LONG).show();
					return;
				}
				
				this.sendSMS(contactList, msgStr);
			}
			/**
			 * 단체 메시지 발송 메서드
			 * @param contactList
			 * @param msgStr
			 */
			private void sendSMS(ArrayList<Contact> contactList, String msgStr) {
				// TODO Auto-generated method stub
				int contactListSize = contactList.size();
				
				if(StringUtil.isNotNull(msgStr)){
					if(contactListSize > 0){
						System.out.println("------------ [ start ] ------------");
						for(Contact contactInfo : contactList){
							sendSMS(contactInfo.getReceiverName(), contactInfo.getReceiverPhoneNo(), msgStr);
						}
						System.out.println("------------ [ finish ] -----------");
					}			
				}				
			}
			/**
			 * 단일 메시지 발송 메서드
			 * @param receiverName
			 * @param receiverPhoneNo
			 * @param msgStr
			 * 
			 * Ref.] 
			 * 1. http://blog.daum.net/seed/2628
			 * 2. http://www.androes.com/69
			 * 3. http://anditstory.tistory.com/entry/android-send-SMS
			 */
			private void sendSMS(String receiverName, String receiverPhoneNo,
					String msgStr) {
				// TODO Auto-generated method stub
				if(StringUtil.isNotNull(msgStr)){
					String convertedMsg = msgStr.replaceAll("##", receiverName);
					
					// 본인의 전화번호 가져오기
					TelephonyManager telMng = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
					String myPhoneNo = telMng.getLine1Number();
					
					SmsManager smsManager = SmsManager.getDefault();
					System.out.println(receiverName + ", " + receiverPhoneNo + ", " + convertedMsg + ", " + myPhoneNo);
					smsManager.sendTextMessage(receiverPhoneNo, myPhoneNo, convertedMsg, null, null);
					
					String sentMsg = "[" + receiverPhoneNo + "]" + receiverName + "";
					try{
						System.out.println(sentMsg + " normally sended message.");
					}catch(Exception ex){
						System.out.println(sentMsg + " will not give a message.");
						ex.printStackTrace();
					}			
				}
			}
		});
		/**
		 * 선택된 수신자 목록
		 */
		modifyReceiverBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ref.] ArrayList<T> 형 데이터를 다른 액티비티로 전달
				// http://www.androidside.com/bbs/board.php?bo_table=b49&wr_id=87165
				Intent intent = new Intent(getApplicationContext(), SelectedContactActivity.class);		
				intent.putExtra("contactList", contactList);
//				Bundle b = new Bundle();
//				b.putParcelableArrayList("contactList", (ArrayList<? extends Parcelable>)contactList);
//				intent.putExtras(b);
				startActivityForResult(intent, 3);
			}
		});
	}
//	/**
//	 * 단체 메시지 발송 메서드
//	 * @param contactList
//	 * @param msgStr
//	 */
//	@SuppressWarnings("unused")
//	private void sendSMS(ArrayList<Contact> contactList, String msgStr){
//		
//		int contactListSize = contactList.size();
//		
//		if(StringUtil.isNotNull(msgStr)){
//			if(contactListSize > 0){
//				for(Contact contactInfo : contactList){
//					this.sendSMS(contactInfo.getReceiverName(), contactInfo.getReceiverPhoneNo(), msgStr);
//				}
//			}			
//		}
//	}
//	/**
//	 * 단일 메시지 발송 메서드
//	 * @param receiverName
//	 * @param receiverPhoneNo
//	 * @param msgStr
//	 * 
//	 * Ref.] 
//	 * 1. http://blog.daum.net/seed/2628
//	 * 2. http://www.androes.com/69
//	 * 3. http://anditstory.tistory.com/entry/android-send-SMS
//	 */
//	private void sendSMS(String receiverName, String receiverPhoneNo, String msgStr){
//		
//		if(StringUtil.isNotNull(msgStr)){
//			String convertedMsg = msgStr.replaceAll("##", receiverName);
//			
//			SmsManager smsManager = SmsManager.getDefault();
//			smsManager.sendTextMessage(receiverPhoneNo, null, convertedMsg, null, null);
//			
//			String sentMsg = "[" + receiverPhoneNo + "]" + receiverName + "에게 전송한 메시지가 ";
//			try{
//				System.out.println(sentMsg + "정상 발송되었습니다.");
//			}catch(Exception ex){
//				System.out.println(sentMsg + "발송되지 않았습니다.");
//				ex.printStackTrace();
//			}			
//		}
//	}

}
