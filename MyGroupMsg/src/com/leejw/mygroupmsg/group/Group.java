package com.leejw.mygroupmsg.group;

import java.io.Serializable;
import java.util.List;

import com.leejw.mygroupmsg.contact.Contact;

public class Group implements Serializable{
	private String groupId;
	private String groupCount;
	private String groupTitle;
	private String groupAccountNm;
	private String groupAccountTp;
	private String groupDelete;
	private String groupVisible;
	private int contactId;
	
	private List<Contact> contactList;
	
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupCount() {
		return groupCount;
	}
	public void setGroupCount(String groupCount) {
		this.groupCount = groupCount;
	}
	public String getGroupTitle() {
		return groupTitle;
	}
	public void setGroupTitle(String groupTitle) {
		this.groupTitle = groupTitle;
	}
	public String getGroupAccountNm() {
		return groupAccountNm;
	}
	public void setGroupAccountNm(String groupAccountNm) {
		this.groupAccountNm = groupAccountNm;
	}
	public String getGroupAccountTp() {
		return groupAccountTp;
	}
	public void setGroupAccountTp(String groupAccountTp) {
		this.groupAccountTp = groupAccountTp;
	}
	public String getGroupDelete() {
		return groupDelete;
	}
	public void setGroupDelete(String groupDelete) {
		this.groupDelete = groupDelete;
	}
	public String getGroupVisible() {
		return groupVisible;
	}
	public void setGroupVisible(String groupVisible) {
		this.groupVisible = groupVisible;
	}
	public int getContactId() {
		return contactId;
	}
	public void setContactId(int contactId) {
		this.contactId = contactId;
	}
	public List<Contact> getContactList() {
		return contactList;
	}
	public void setContactList(List<Contact> contactList) {
		this.contactList = contactList;
	}

}
