package com.boxfox.weather;

import java.io.File;

import javafx.scene.image.Image;

public class Weather {
    private final static Image image_sun = new Image(new File("resource/sun.png").toURI().toString());
    private final static Image image_cloud = new Image(new File("resource/cloud.png").toURI().toString());
    private final static Image image_rain = new Image(new File("resource/rainy.png").toURI().toString());

	private String temperature, tmax, sky, pop, wd, reh, tmin;
    private Image icon = image_sun;
	
	public String getTemperature() {
		return temperature;
	}
	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}
	public String getTmax() {
		return tmax;
	}
	public void setTmax(String tmax) {
		this.tmax = tmax;
	}
	public String getSky() {
		return sky;
	}
	public String getPop() {
		return pop;
	}
	public void setPop(String pop) {
		this.pop = pop;
	}
	public String getWd() {
		return wd;
	}
	public void setWd(String wd) {
		this.wd = wd;
	}
	public String getReh() {
		return reh;
	}
	public void setReh(String reh) {
		this.reh = reh;
	}
	public void setTmin(String string) {
		this.tmin = string;
	}
	public String getTmin(){
		return tmin;
	}
	

    public void setSky(String sky) {
    	if(sky.contains("±¸¸§")||sky.equals("Èå¸²")){
            icon = image_cloud;
    	}else if(sky.contains("ºñ")||sky.contains("´«")){
            icon = image_rain;
    	}else{
            icon = image_sun;
    	}
 		this.sky = sky;
    }

    public Image getIcon() {
        return icon;
    }
}
