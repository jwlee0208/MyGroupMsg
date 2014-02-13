package com.leejw.mygroupmsg.contact;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.provider.ContactsContract.Data;
import android.util.Log;
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
import com.leejw.mygroupmsg.group.Group;
import com.leejw.mygroupmsg.main.MainActivity;
import com.leejw.utils.StringUtil;

@SuppressLint("NewApi")
public class ContactActivity extends Activity{
	
	ArrayList<Contact> returnList;
	
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

		LayoutParams scrollViewParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		scrollView.setLayoutParams(scrollViewParams);
		// 수신자 선택 목록 리니어레이아웃 생성
		LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        
		scrollView.addView(ll);
		linearLayout.addView(scrollView);
		
		List<Group> groupList = this.getGroupList(null);
		
//		this.printList(groupList);
		
		// 중복 데이터 제거
		groupList = new ArrayList<Group>(new HashSet<Group>(groupList));
		
		int groupListSize = groupList.size();
		
		if(groupListSize > 0){
			int groupIndex = 0;
			
			for(Group group : groupList){
				
//				if(groupIndex < 5){// 임시 조건
				
				String groupId = group.getGroupId();
				
				TextView groupRow = new TextView(this);
				groupRow.setText(group.getGroupTitle());
				groupRow.setId(Integer.parseInt(groupId));
				
				ll.addView(groupRow);
				
				List<Contact> contactList = this.getContactList(null, groupId);
				
				// 중복 데이터 제거
				contactList = new ArrayList<Contact>(new HashSet<Contact>(contactList));
				// ------------ [ contactList 조회 ] --------------
				int contactListSize = contactList.size();
				
				if(contactListSize > 0){		
					
//					this.printList(contactList);
					
					int index = 0;
					// Create LinearLayout

					for(Contact contact : contactList){
			             
						String receiverId = contact.getReceiverName() + ";" + contact.getReceiverPhoneNo() + ";" + contact.getGroupId();
						
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
				// ------------ [ contactList 조회 ] --------------
				
//				}//임시조건 끝
				
				groupIndex++;
			}
			
		}else{
			// pass
		}
        
        
	

	}
	/**
	 * 반환할 리스트 객체에 데이터 추가/제거
	 * @param receiverInfo
	 * @param isChecked
	 */
	private void updateData(String receiverInfo, boolean isChecked){
		String receiverInfos[]  = StringUtil.splitStr(receiverInfo.toString());
		Contact contactObj;
		
		String receiverName = receiverInfos[0];
		String cellPhoneNo = receiverInfos[1];
		String groupId = receiverInfos[2];
		
		int returnListSize = this.returnList.size();
		
		if(receiverInfos.length > 0){
			if(isChecked){
//				System.out.println("IS_CHECKED");
				contactObj = this.setContact(receiverName, cellPhoneNo, groupId);
				
				// 행 추가
//				returnList.add(contactObj);
				
			}else{
				// 체크박스 체크해제시 오류 해결 요망
				if(returnListSize > 0){
//					System.out.println("IS_UNCHECKED");
					// 왜 삭제가 안될까???
					// ref.] http://stackoverflow.com/questions/16460258/java-lang-indexoutofboundsexception-invalid-index-2-size-is-2
//					returnList.remove(contactObj);
					
					for(int i = 0 ; i < returnListSize ; i++){
						Contact contact = (Contact)returnList.get(i);
							
						if(contact.getReceiverName().equals(receiverName) 
						&& contact.getReceiverPhoneNo().equals(cellPhoneNo)){
							returnList.remove(contact);  	// java.lang.IndexOutOfBoundsException: Invalid index 2, size is 2
							returnListSize--;
						}
					}
				}else{
					// pass
				}
			}			
		}
//		printList();
	}
	
	/**
	 * 연락처 객체에 정보 setting.
	 * @param receiverName
	 * @param receiverPhoneNo
	 * @return
	 */
	@SuppressWarnings("unused")
	private Contact setContact(String receiverName, String receiverPhoneNo, String groupId){
		Contact contactObj = new Contact();
		contactObj.setReceiverName(receiverName);
		contactObj.setReceiverPhoneNo(receiverPhoneNo);
		contactObj.setGroupId(groupId);
		
		return contactObj;
	}
	/**
	 * 주소록 그룹 정보 목록 조회
	 * @param searchKeyword
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<Group> getGroupList(String searchKeyword){
		Uri uri = ContactsContract.Groups.CONTENT_URI;
//		String[] projections = new String[] {
//				ContactsContract.Groups._ID,
//				ContactsContract.Groups._COUNT,
//				ContactsContract.Groups.TITLE,
//				ContactsContract.Groups.ACCOUNT_NAME,
//				ContactsContract.Groups.ACCOUNT_TYPE,
//				ContactsContract.Groups.DELETED,
//				ContactsContract.Groups.GROUP_VISIBLE
//		};
		String[] projections = new String[] {
				ContactsContract.Groups._ID,
				ContactsContract.Groups.TITLE,
				ContactsContract.Groups.DELETED,
				ContactsContract.Groups.GROUP_VISIBLE
		};

		
        String selection = ""; 
        selection += ContactsContract.Groups.DELETED + " = 0 ";
        selection += (StringUtil.isNotNull(searchKeyword)) ? " AND " + ContactsContract.Groups.TITLE + " LIKE '%" + searchKeyword + "%'" : "";
        
        
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.Groups.TITLE + " COLLATE LOCALIZED ASC";

        Cursor groupCursor = 
        		getContentResolver().query(uri, projections,
                        selection, selectionArgs, sortOrder);
        
		List<Group> groupList = new ArrayList<Group>();
		Group groupObj = null;
		
		if(groupCursor.moveToFirst()){
			do{
				groupObj = new Group();
//				groupObj.setGroupId(groupCursor.getString(0));
//				groupObj.setGroupCount(groupCursor.getString(1));
//				groupObj.setGroupTitle(groupCursor.getString(2));
//				groupObj.setGroupAccountNm(groupCursor.getString(3));
//				groupObj.setGroupAccountTp(groupCursor.getString(4));
//				groupObj.setGroupDelete(groupCursor.getString(5));
//				groupObj.setGroupVisible(groupCursor.getString(6));

				groupObj.setGroupId(groupCursor.getString(0));
				groupObj.setGroupTitle(groupCursor.getString(1));
				groupObj.setContactId(groupCursor.getColumnIndex(android.provider.ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID));
				
				groupList.add(groupObj);
			}while(groupCursor.moveToNext());
		}
        
        
		return groupList;
	}
	/**
	 * 주소록 조회
	 * @return
	 */
	@SuppressWarnings("unused")
	private List<Contact> getContactList() {
		return getContactList(null, null);
	}
	/**
	 * 주소록 조회
	 * @param searchKeyword
	 * @param groupId
	 * @return
	 */
	private List<Contact> getContactList(String searchKeyword, String groupId){
		List<Contact> contactList = new ArrayList<Contact>();
		Contact contactInfo = null;
		
		Uri uri = Data.CONTENT_URI;

		String[] projections = new String[] {
				Contacts.DISPLAY_NAME
			  , android.provider.ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID
		};
		
        String selection = ""; 
        selection += CommonDataKinds.GroupMembership.GROUP_ROW_ID + " =  ? " + " AND ";
        selection += ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE + " = '" + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE + "'" ;
        selection += (StringUtil.isNotNull(searchKeyword)) ? " AND " + Contacts.DISPLAY_NAME + " LIKE '%" + searchKeyword + "%'" : "";
        String[] selectionArgs = {String.valueOf(groupId)};
        String sortOrder = 
        		Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

        Cursor groupCursor = 
        		getContentResolver().query(uri, projections,selection, selectionArgs, sortOrder);
        
		if(groupCursor.moveToFirst()){
			do{

				int nameColumnIndex = groupCursor.getColumnIndex(Phone.DISPLAY_NAME);
				
				String name = groupCursor.getString(nameColumnIndex);
				
				long contactId = groupCursor.getLong(groupCursor.getColumnIndex(android.provider.ContactsContract.CommonDataKinds.GroupMembership.CONTACT_ID));
				
				Cursor numberCursor = getContentResolver().query(Phone.CONTENT_URI, new String[]{Phone.NUMBER}, Phone.CONTACT_ID + " = " + contactId, null, null);
			
				if(numberCursor.moveToFirst()){
					int numberColumnIndex = numberCursor.getColumnIndex(Phone.NUMBER);
					do{
						String phoneNumber = numberCursor.getString(numberColumnIndex);
						Log.d("your tag", "contact " + name + ":" + phoneNumber);
						
						contactInfo = new Contact();
                        contactInfo.setReceiverPhoneNo(phoneNumber);
                        contactInfo.setReceiverName(name);
                        contactInfo.setGroupId(groupId);
                        contactList.add(contactInfo);
						
					}while(numberCursor.moveToNext());
					numberCursor.close();	
				}
			}while(groupCursor.moveToNext());
			groupCursor.close();
		}	
//        Uri uri = 
////        		ContactsContract.Data.CONTENT_URI;
//        		ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
//        String[] projections = //null;
//        		new String[] {
////        		ContactsContract.Data.CONTACT_ID,
//        		ContactsContract.CommonDataKinds.Phone.NUMBER,
//        		ContactsContract.Data.DISPLAY_NAME
//        		};
////        		new String[] {
////                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
////                        ContactsContract.CommonDataKinds.Phone.NUMBER,
////                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
////                        ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID};
//        // where 절 조건 정의
//        // ref.] 
//        //       1. http://withwani.tistory.com/153 
//        //       2. http://www.tutorialspoint.com/sqlite/sqlite_like_clause.htm
//        //       3. http://chunic.blogspot.kr/2013/10/blog-post.html
//        String selection = "";
//        selection += (contactId > -1) ? Phone.CONTACT_ID + " = " + contactId : "";
////        selection += (StringUtil.isNotNull(groupId)) 
////        				?  ContactsContract.CommonDataKinds.GroupMembership.GROUP_ROW_ID + " = " + Long.parseLong(groupId) + " AND "
////        				 + ContactsContract.CommonDataKinds.GroupMembership.MIMETYPE 	 + " = '" + ContactsContract.CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE +"'"
////            			 + ContactsContract.Data.MIMETYPE 	 + " = '" + ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE +"'"
////        				 : "" ;
//        selection += (StringUtil.isNotNull(groupId) && StringUtil.isNotNull(searchKeyword)) ? " AND " : "";
//        selection += (StringUtil.isNotNull(searchKeyword)) ? ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " LIKE '%" + searchKeyword + "%' " : "";
//        
//        
//        String[] selectionArgs = null;
//        String sortOrder = 
//        		ContactsContract.Data.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
//
//        Cursor contactCursor = getContentResolver().query(uri, projections,
//                        selection, selectionArgs, sortOrder);
//        List<Contact> contactList = new ArrayList<Contact>();
//        Contact contactInfo = null;
//        int contactCursorCnt = contactCursor.getCount();
//        
//        if(contactCursorCnt > 0){        	
//        
//	        if (contactCursor.moveToFirst()) {
//	
//	                do {
//	                	
////	                	for(int i = 0 ; i < contactCursor.getColumnCount() ; i++){
////	                		System.out.println(i + " : " + contactCursor.getColumnName(i) + ", " + contactCursor.getString(i) + ", " + contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
////	                	}
//	                	
//	                        contactInfo = new Contact();
//	                        contactInfo.setReceiverPhoneNo(contactCursor.getString(0));
//	                        contactInfo.setReceiverName(contactCursor.getString(1));
//	                        contactInfo.setGroupId(groupId);
//	                        contactList.add(contactInfo);
////	                        String phonenumber = contactCursor.getString(1).replaceAll("-",
////	                                        "");
////	                        
////	                        String headPhoneNo = phonenumber.substring(0, 3);
////	                        
////	                        if (phonenumber.length() == 10) {
////	                                phonenumber = headPhoneNo + "-"
////	                                                + phonenumber.substring(3, 6) + "-"
////	                                                + phonenumber.substring(6);
////	                        } else if (phonenumber.length() > 8) {
////	                                phonenumber = headPhoneNo + "-"
////	                                                + phonenumber.substring(3, 7) + "-"
////	                                                + phonenumber.substring(7);
////	                        }
////	//                         contactInfo.setPhotoId(contactCursor.getLong(0));
////	                        
////	                        if(headPhoneNo.equals("010") || headPhoneNo.equals("011") || headPhoneNo.indexOf("82") > 0){
////	                                contactInfo.setReceiverPhoneNo(phonenumber);
////	                                contactInfo.setReceiverName(contactCursor.getString(2));
////	                                contactInfo.setGroupId(contactCursor.getString(3));
////	                                contactList.add(contactInfo);                                        
////	                        }
//	                        
//	                } while (contactCursor.moveToNext());
//	        }
//        }
        return contactList;
	}
	
	private void printList(){
		int listSize = returnList.size();
		
		if(listSize > 0){
			System.out.println("-------------------------");
			for(Contact contact : returnList){
				System.out.println(contact.getReceiverName() + ", " + contact.getReceiverPhoneNo() + ", " + contact.getGroupId());
			}				
			System.out.println("-------------------------");
		}	
	}	
	
	// ref.] http://stackoverflow.com/questions/13400075/reflection-generic-get-field-value
	private void printList(List<?> list){
		
		int listSize = list.size();
		
		if(listSize > 0){
			System.out.println("-------------------------");
			for(Object obj : list){
				Class classVal = obj.getClass();
				
				Method[] methods = classVal.getMethods();
				
				for(Field targetField : classVal.getDeclaredFields()){
					targetField.setAccessible(true);
					
					try {
						System.out.println(targetField.getName() + ", " + targetField.get(obj));
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}				
			System.out.println("-------------------------");

		}
	}
	

	
}
