package com.leejw.mygroupmsg.contact;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Contact implements Serializable{
	private String receiverId;
	private String receiverName;
	private String receiverPhoneNo;
	private String groupId;
	private long contactId;		// unique key for searching someons's detail contact info.
	private int phoneNoType;		// cell phone, home phone, fax no, etc,.
	
	public String getReceiverId() {
		return receiverId;
	}
	public void setReceiverId(String receiverId) {
		this.receiverId = receiverId;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverPhoneNo() {
		return receiverPhoneNo;
	}
	public void setReceiverPhoneNo(String receiverPhoneNo) {
		this.receiverPhoneNo = receiverPhoneNo;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public long getContactId() {
		return contactId;
	}
	public void setContactId(long contactId) {
		this.contactId = contactId;
	}
	public int getPhoneNoType() {
		return phoneNoType;
	}
	public void setPhoneNoType(int phoneNoType) {
		this.phoneNoType = phoneNoType;
	}
	
	
}
