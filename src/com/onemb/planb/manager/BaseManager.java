package com.onemb.planb.manager;

import java.util.ArrayList;

import com.boxfox.controller.BaseController;
import com.boxfox.dto.Event;

public abstract class BaseManager {
	protected ArrayList<BaseController> controllList;

	public BaseManager() {
		controllList = new ArrayList<BaseController>();
	}

	public void registerController(Class c) {
		controllList.add(BaseController.getController(c));
	}

	public void registerController(BaseController c) {
		controllList.add(c);
	}

	public void update(Event e){
		for(BaseController c : controllList){
			c.eventPerformed(e);
		}
	}

}
