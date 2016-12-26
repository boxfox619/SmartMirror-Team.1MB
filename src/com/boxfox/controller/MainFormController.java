package com.boxfox.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.boxfox.dto.Date;
import com.boxfox.dto.Event;
import com.boxfox.dto.Memo;
import com.boxfox.sound.MusicManager;
import com.boxfox.weather.Weather;
import com.boxfox.weather.WeatherManager;
import com.onemb.planb.manager.DateManager;
import com.onemb.planb.manager.MemoManager;

import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;

public class MainFormController extends BaseController {

	@FXML
	private Pane p_main;
	@FXML
	private ImageView iv_weather, iv_background;
	@FXML
	private Label lb_weather, lb_music, lb_date, lb_time;
	@FXML
	private Pane weather_pane;

	public MainFormController() {
		super(MainFormController.class);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Screen screen = Screen.getPrimary();
		Rectangle2D bounds = screen.getVisualBounds();
		weather_pane.setTranslateX(bounds.getWidth() - 395.0);
		updateWeather();
	}

	public WebEngine useWebView() {
		WebView view = new WebView();
		p_main.getChildren().add(view);
		view.setVisible(false);
		WebEngine engine = view.getEngine();
		engine.setJavaScriptEnabled(true);
		return engine;
	}

	public void updateWeather() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				lb_weather.setText("날씨정보를 불러오는중 입니다..");
				Weather weather = WeatherManager.getWeather();
				javafx.application.Platform.runLater(() -> {
					// iv_background.setImage(weather.getBackground());
					iv_weather.setImage(weather.getIcon());
					lb_weather.setText("대전 - " + weather.getSky());
				});
			}
		}).start();
	}

	@Override
	public void eventPerformed(Event e) {
		javafx.application.Platform.runLater(() -> {
			if (e.getSender().equals(DateManager.class.getName())) {
				Date date = (Date) e.getArg();
				lb_date.setText(date.getDate());
				lb_time.setText(date.getTime());
			} else if (e.getSender().equals(MusicManager.class.getName())) {
				String title = (String) e.getArg();
				lb_music.setText(title);
			}
		});
	}

}