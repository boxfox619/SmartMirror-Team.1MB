package com.boxfox.controller;

import java.awt.AWTException;
import java.awt.Robot;
import java.net.URL;
import java.util.ResourceBundle;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.boxfox.dto.Event;

import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class BrowserController extends BaseController {
	private static final String url = "https://www.google.co.kr/?gws_rd=ssl#q=";
	private static final String youtubeUrl = "https://www.youtube.com/results?search_query=";
	
	@FXML
	private Pane p_main;
	

	public BrowserController() {
		super(BrowserController.class);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	public void scrollUp() {
		try {
			Robot robot = new Robot();
			robot.mouseWheel(-5);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public void scrollDown() {
		try {
			Robot robot = new Robot();
			robot.mouseWheel(5);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

	public void searchYoutube(String text) {
		javafx.application.Platform.runLater(() -> {
			WebView view = new WebView();
			view.getStyleClass().add("root-background");
			WebEngine engine = view.getEngine();
			engine.load(youtubeUrl + text);
			p_main.getChildren().clear();
			p_main.getChildren().add(view);
		});
	}

	public void search(String text) {
		javafx.application.Platform.runLater(() -> {
			WebView view = new WebView();
			view.getStyleClass().add("root-background");
			WebEngine engine = view.getEngine();
			engine.load(url + text);
			Pane pane = new Pane();
			pane.getStyleClass().add("root-background");
			pane.getChildren().add(view);
			p_main.getChildren().clear();
			p_main.getChildren().add(pane);
			engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
		            if (newState == State.SUCCEEDED) {
		            	Document doc = engine.getDocument() ;
		                Element styleNode = doc.createElement("style");
		                Text styleContent = doc.createTextNode(
		                        "body, div {"
		                                + "    background-color: #000000; "
		                                + "    color: #ffffff; "
		                                + "}");
		                styleNode.appendChild(styleContent);
		                doc.getDocumentElement().getElementsByTagName("head").item(0).appendChild(styleNode);
		            }
	        });
			
		});
	}

	@Override
	public void eventPerformed(Event e) {
		
	}
}
