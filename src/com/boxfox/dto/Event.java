package com.boxfox.dto;

public class Event {
	private String sender;
	private Object arg;
	
	public Event(Class sender, Object arg){
		this.sender = sender.getName();
		this.arg = arg;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(Class sender) {
		this.sender = sender.getName();
	}

	public Object getArg() {
		return arg;
	}

	public void setArg(Object arg) {
		this.arg = arg;
	}

}
