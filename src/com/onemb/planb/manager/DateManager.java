package com.onemb.planb.manager;

import java.util.Calendar;

import com.boxfox.controller.BaseController;
import com.boxfox.dto.Date;
import com.boxfox.dto.Event;

public class DateManager extends BaseManager {

	private Date getDate() {
		StringBuilder date = new StringBuilder();
		StringBuilder time = new StringBuilder();
		Calendar today = Calendar.getInstance();

		date.append(today.get(Calendar.YEAR)).append("³â ");
		date.append(today.get(Calendar.MONTH) + 1).append("¿ù ");
		date.append(today.get(Calendar.DATE)).append("ÀÏ");

		if (today.get(Calendar.AM_PM) == 0) { // AM
			time.append("AM ");
		} else {
			time.append("PM ");
		}
		if (today.get(Calendar.HOUR) == 0) {
			time.append("12").append(" : ");
		} else {
			time.append(today.get(Calendar.HOUR)).append(" : ");
		}
		time.append(today.get(Calendar.MINUTE)).append(" : ");
		time.append(today.get(Calendar.SECOND));
		Date dateObj = new Date(date.toString(), time.toString());
		return dateObj;
	}

	public void run() {
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					update(new Event(DateManager.class, getDate()));
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					}
				}
			}
		});
		t.setDaemon(true);
		t.start();
	}

}
