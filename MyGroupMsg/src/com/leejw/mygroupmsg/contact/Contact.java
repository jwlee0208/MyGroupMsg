package com.leejw.mygroupmsg.contact;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Contact implements Serializable{
	private String receiverId;
	private String receiverName;
	private String receiverPhoneNo;
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
	
	
}
