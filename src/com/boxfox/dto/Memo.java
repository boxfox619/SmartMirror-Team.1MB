package com.boxfox.dto;

public class Memo {
	private int num;
	private String content, datetime;

	public Memo(int num, String content, String datetime) {
		this.num = num;
		this.content = content;
		this.datetime = datetime;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

}
