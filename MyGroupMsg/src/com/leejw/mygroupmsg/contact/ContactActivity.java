package com.leejw.mygroupmsg.contact;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.leejw.mygroupmsg.R;
import com.leejw.mygroupmsg.main.MainActivity;

public class ContactActivity extends Activity{
	
	ArrayList<Contact> returnList;
	
//	public ContactActivity() {
//		super();
		// TODO Auto-generated constructor stub
//		this.returnList = new ArrayList<Contact>();
//	}

	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.av_contact);
		
		this.returnList = new ArrayList<Contact>();
		
		final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.contact_linear);
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		// 상단 문구 설정
		TextView headText = new TextView(this);
		headText.setText("수신자를 선택해 주세요.");
		linearLayout.addView(headText);


		// 하단의 버튼 추가
		LinearLayout llbottom = new LinearLayout(this);
        llbottom.setOrientation(LinearLayout.HORIZONTAL);
        Button okBtn = new Button(this);
        okBtn.setText("확인");
        
        Button cancelBtn = new Button(this);
        cancelBtn.setText("취소");

        llbottom.addView(okBtn);
        llbottom.addView(cancelBtn);
        linearLayout.addView(llbottom);
      
		// 수신자 선택 목록 스크롤 생성
		ScrollView scrollView = new ScrollView(this);

		scrollView.setLayoutParams(params);
		// 수신자 선택 목록 리니어레이아웃 생성
		LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        
		scrollView.addView(ll);
		linearLayout.addView(scrollView);

		List<Contact> contactList = this.getContactList();
		
		int contactListSize = contactList.size();
		
		if(contactListSize > 0){			
			int index = 0;
			// Create LinearLayout

			for(Contact contact : contactList){
	             
				String receiverId = contact.getReceiverName() + ";" + contact.getReceiverPhoneNo();
//				String receiverText = contact.getReceiverName() + "[" + contact.getReceiverPhoneNo() + "]";
				
				final CheckBox checkBox = new CheckBox(this);
				checkBox.setText(receiverId.toString());
				checkBox.setId(index);
				
			
				checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						
						updateData(buttonView.getText().toString(), checkBox.isChecked());
					}
				});

				ll.addView(checkBox);
				
				index++;
			}
		}else{
			
		}
		// 확인 버튼 클릭시
		okBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ref.] 
				// 1. http://blog.naver.com/PostView.nhn?blogId=0677haha&logNo=60141449535
				// 2. http://rlqks132.tistory.com/44
				Intent intent = new Intent(getBaseContext(), MainActivity.class);
				intent.putExtra("contactList", returnList);
				setResult(2, intent);
				finish();					
			}
		});
		// 취소 버튼 클릭시
		cancelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}
	/**
	 * 반환할 리스트 객체에 데이터 추가/제거
	 * @param receiverInfo
	 * @param isChecked
	 */
	private void updateData(String receiverInfo, boolean isChecked){
		String receiverInfos[]  = this.splitStr(receiverInfo.toString());
		Contact contactObj;
		
		String receiverName = receiverInfos[0];
		String cellPhonoNo = receiverInfos[1];
		
		int returnListSize = this.returnList.size();
		
		if(receiverInfos.length > 0){
			if(isChecked){
				contactObj = new Contact();
				
				contactObj.setReceiverName(receiverInfos[0]);
				contactObj.setReceiverPhoneNo(receiverInfos[1]);
				// 행 추가
				returnList.add(contactObj);
				
			}else{
				// 체크박스 체크해제시 오류 해결 요망
				if(returnListSize > 0){
					
					// 비교
//					if(returnList.contains(receiverInfos[1])){
						contactObj = new Contact();
						contactObj.setReceiverName(receiverName);
						contactObj.setReceiverPhoneNo(cellPhonoNo);
						
						for(int i = 0 ; i < returnListSize ; i++){
							Contact contact = (Contact)returnList.get(i);
//							System.out.println(contact.getReceiverName().equals(receiverName) + " , " + contact.getReceiverPhoneNo().equals(cellPhonoNo));
							
							if(contact.getReceiverName().equals(receiverName) && contact.getReceiverPhoneNo().equals(cellPhonoNo)){
								returnList.remove(contact);
							}
						}
				}else{
					// pass
				}
			}			
		}
		printList();
	}
	
	public String[] splitStr(String value){
		Pattern p = Pattern.compile("[;]+");
		return p.split(value);
	}
	
	private List<Contact> getContactList() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projections = new String[] {
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME };
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                        + " COLLATE LOCALIZED ASC";

        Cursor contactCursor = getContentResolver().query(uri, projections,
                        null, selectionArgs, sortOrder);
        List<Contact> contactList = new ArrayList<Contact>();
        Contact contactInfo = null;
        if (contactCursor.moveToFirst()) {

                do {
                        contactInfo = new Contact();

                        String phonenumber = contactCursor.getString(1).replaceAll("-",
                                        "");
                        
                        String headPhoneNo = phonenumber.substring(0, 3);
                        
                        if (phonenumber.length() == 10) {
                                phonenumber = headPhoneNo + "-"
                                                + phonenumber.substring(3, 6) + "-"
                                                + phonenumber.substring(6);
                        } else if (phonenumber.length() > 8) {
                                phonenumber = headPhoneNo + "-"
                                                + phonenumber.substring(3, 7) + "-"
                                                + phonenumber.substring(7);
                        }
                        // contactInfo.setPhotoId(contactCursor.getLong(0));
                        
                        if(headPhoneNo.equals("010") || headPhoneNo.equals("011") || headPhoneNo.indexOf("82") > 0){
                                contactInfo.setReceiverPhoneNo(phonenumber);
                                contactInfo.setReceiverName(contactCursor.getString(2));
                                contactList.add(contactInfo);                                        
                        }
                        
                } while (contactCursor.moveToNext());
        }
        return contactList;
	}
	
	private void printList(){
		int listSize = returnList.size();
		
		if(listSize > 0){
			System.out.println("-------------------------");
			for(Contact contact : returnList){
				System.out.println(contact.getReceiverName() + ", " + contact.getReceiverPhoneNo());
			}				
			System.out.println("-------------------------");
		}	
	}	
}
