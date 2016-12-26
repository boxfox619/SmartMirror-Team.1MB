package com.boxfox.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.boxfox.dto.Event;
import com.boxfox.dto.Memo;
import com.onemb.planb.manager.MemoManager;

import javafx.fxml.FXML;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class MemoController extends BaseController {
	@FXML
	private WebView memo_view;
	private WebEngine engine;

	private String head = "<head><style>ul{list-style-type: none;}</style></head>";

	public MemoController() {
		super(MemoController.class);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		engine = memo_view.getEngine();
	}

	@Override
	public void eventPerformed(Event e) {
		if (e.getSender().equals(MemoManager.class.getName())) {
			StringBuilder htmlBuilder = new StringBuilder();
			ArrayList<Memo> list = (ArrayList) e.getArg();
			htmlBuilder.append("<html>");
			htmlBuilder.append(head);
			htmlBuilder.append("<body style=\"background: #123456; color:#ffffff;\"><ul>");
			for (Memo m : list) {
				htmlBuilder.append("<li>");
				htmlBuilder.append(m.getNum());
				htmlBuilder.append(".");
				htmlBuilder.append(m.getContent());
				htmlBuilder.append("</li>");
			}
			htmlBuilder.append("</ul></body></html>");
			javafx.application.Platform.runLater(() -> {
				engine.loadContent(htmlBuilder.toString());
			});
		}
	}

}
