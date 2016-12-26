package com.boxfox.dto;

public abstract class CommandJob {
	public Object obj;
	
	public abstract void work();
	public abstract void cancel();
	
}
