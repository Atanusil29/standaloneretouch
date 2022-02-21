package com.dlw.standaloneretouch.controller;

public class RequestDTO {
	
	private String radTemplate;
	private String channel;
	private String emailAddress;
	private String emailSubject;
	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	private String otdsticket;
	private String docid;
	private String revision;
	private String jsessionId;
	private String messageid;

	public String getOtdsticket() {
		return otdsticket;
	}

	public void setOtdsticket(String otdsticket) {
		this.otdsticket = otdsticket;
	}

	public String getDocid() {
		return docid;
	}

	public void setDocid(String docid) {
		this.docid = docid;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public String getJsessionId() {
		return jsessionId;
	}

	public void setJsessionId(String jsessionId) {
		this.jsessionId = jsessionId;
	}

	public String getMessageid() {
		return messageid;
	}

	public void setMessageid(String messageid) {
		this.messageid = messageid;
	}

	public String getRadTemplate() {
		return radTemplate;
	}

	public void setRadTemplate(String radTemplate) {
		this.radTemplate = radTemplate;
	}
	
	
}
