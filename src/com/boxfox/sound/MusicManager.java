package com.boxfox.sound;

import java.io.File;
import java.util.ArrayList;

import com.boxfox.controller.MainFormController;
import com.boxfox.dto.Event;
import com.boxfox.dto.YoutubeMusic;
import com.onemb.planb.manager.BaseManager;

import application.MainApplication;
import javafx.concurrent.Worker.State;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.web.WebEngine;

public class MusicManager extends BaseManager {
	private static final String youtubeUrl = "https://www.youtube.com/results?search_query=";
	public static final String MUSIC_PATH = "music/";

	private static ArrayList<String> musicList;

	private static MediaPlayer mediaPlayer;
	private static int music_offset = 0;
	private static boolean plaing = false;

	public MusicManager() {
		musicList = new ArrayList<String>();
	}

	public void getYoutube(String name) {
		update(new Event(MusicManager.class, "노래를 검색중입니다..."));
		javafx.application.Platform.runLater(() -> {
			MainFormController mc = (MainFormController) MainFormController.getController(MainFormController.class);
			WebEngine engine = mc.useWebView();
			engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
				if (newState == State.SUCCEEDED) {
					int i = 0;
					while (true) {
						String title = (String) engine.executeScript(
								"document.getElementsByClassName(\"yt-lockup-title\")[" + i + "].childNodes[0].text;");
						String href = (String) engine.executeScript(
								"document.getElementsByClassName(\"yt-lockup-title\")[" + i + "].childNodes[0].href;");
						if (!href.contains("googleads")) {
							title = title.replaceAll("MV", "");
							title = title.replaceAll("M/V", "");
							while (title.contains("[") && title.contains("]"))
								title = title.replace(title.substring(title.indexOf("["), title.indexOf("]") + 1), "");
							while (title.contains("(") && title.contains(")"))
								title = title.replace(title.substring(title.indexOf("("), title.indexOf(")") + 1), "");
							YoutubeMusic music = new YoutubeMusic(name, title, href);
							getMp3(music);
							return;
						}
						i++;
					}
				}
			});
			engine.load(youtubeUrl + name);
		});
	}

	private void getMp3(YoutubeMusic music) {
		if (!music.getUrl().contains("https://www.youtube.com/watch?v="))
			return;
		MainFormController mc = (MainFormController) MainFormController.getController(MainFormController.class);
		WebEngine engine = mc.useWebView();
		engine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
			if (newState == State.SUCCEEDED) {
				EngineTask task = new EngineTask(music, engine);
				task.setDaemon(true);
				task.start();
			}
		});
		engine.load("http://www.youtube-mp3.org/");
	}

	public void loadMusic() {
		File dir = new File(MUSIC_PATH);
		if (!dir.exists()) {
			dir.mkdirs();
		} else {
			File[] files = dir.listFiles();
			for (File f : files) {
				String name = f.getName();
				if (name.substring(name.lastIndexOf("."), name.length()).contains("mp3")) {
					musicList.add(f.toURI().toString());
				}
			}
		}
	}

	public void playMusic(YoutubeMusic yMusic) {
		update(new Event(MusicManager.class, "노래를 다운로드중입니다..."));
		if (mediaPlayer != null)
			mediaPlayer.stop();
		musicList.add(yMusic.getFileUrl());
		music_offset = musicList.size() - 1;
		play();
	}

	public void playMusic() {
		if (plaing)
			return;
		if (mediaPlayer != null) {
			mediaPlayer.play();
			return;
		}
		play();
	}

	private void play() {
		javafx.application.Platform.runLater(() -> {
			if (mediaPlayer != null)
				mediaPlayer.stop();
			File music = new File(musicList.get(music_offset));
			String title = music.getName();
			Media media = new Media(music.toURI().toString());
			mediaPlayer = new MediaPlayer(media);
			mediaPlayer.play();
			plaing = true;
			update(new Event(MusicManager.class, title));
		});
	}

	public synchronized void nextMusic() {
		stopMusic();
		music_offset++;
		if (music_offset >= musicList.size()) {
			music_offset = 0;
		}
		play();
	}

	public synchronized void stopMusic() {
		plaing = false;
		if (mediaPlayer != null)
			javafx.application.Platform.runLater(() -> {
				mediaPlayer.pause();
			});
	}

	private class EngineTask extends Thread {
		private int count = 0;
		private boolean check = true;
		private WebEngine engine;
		private YoutubeMusic music;

		public EngineTask(YoutubeMusic music, WebEngine engine) {
			this.engine = engine;
			this.music = music;
		}

		@Override
		public void run() {
			while (check) {
				try {
					Thread.sleep(500);
					javafx.application.Platform.runLater(() -> {
						try {
							if(count==30){
								update(new Event(MusicManager.class,"음악 검색에 실패했습니다."));
								check = false;
								return;
							}
							count++;
							for (int i = 0; i < 5; i++) {
								String str = (String) engine.executeScript(
										"document.getElementById('dl_link').childNodes[" + i + "].href;");
								if (str.contains("ts_create")) {
									music.setUrl(str);
									if (music.getSimilarity() > 70) {
										playMusic(music);
									} else {
										MainApplication.response(music);
									}
									check = false;
									return;
								}
							}
						} catch (Exception e) {
							javafx.application.Platform.runLater(() -> {
								engine.executeScript("document.getElementById(\"youtube-url\").value=\"" + music.getUrl() + "\";");
								engine.executeScript("document.getElementById(\"submit\").click();");
							});
						}
					});
				} catch (Exception e) {
				}
			}
		}
	}
}
