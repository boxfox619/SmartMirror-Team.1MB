package com.boxfox.controller;

import java.util.HashMap;

import com.boxfox.dto.Event;

import javafx.fxml.Initializable;

public abstract class BaseController implements Initializable{
	private static HashMap<String,BaseController> controllers;
	
	static{
		controllers = new HashMap<String,BaseController>();
	}
	
	public static BaseController getController(Class c){
		return controllers.get(c.getName());
	}
	
	public BaseController(Class c){
		controllers.put(c.getName(),this);
	}
	
	public abstract void eventPerformed(Event e);

}
