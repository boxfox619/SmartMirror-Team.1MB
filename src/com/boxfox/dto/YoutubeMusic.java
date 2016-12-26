package com.boxfox.dto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.boxfox.sound.MusicManager;

import javafx.scene.media.Media;

public class YoutubeMusic {
	private String title, url;
	private double similarity;

	private double similarity(String a, String b) {
		double count = 0;
		char[] words = a.toCharArray();
		for (char word : words) {
			if (b.indexOf(word) != -1) {
				count++;
			}
		}
		return count / words.length * 100;
	}

	public YoutubeMusic(String search, String title, String url) {
		this.title = URLDecoder.decode(title.replaceAll("%20", ""));
		this.url = url;
		this.similarity = similarity(search, title);
	}

	public Media getMedia() {
		return new Media(getFileUrl());
	}

	public String getFileUrl() {
		String uri;
		File file = new File(MusicManager.MUSIC_PATH + title + ".mp3");
		if (file.exists()) {
			uri = file.getPath();
		} else {
			uri = getFile();
		}
		return uri;
	}

	private String getFile() {
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
			InputStream is = con.getInputStream();
			File f = new File(MusicManager.MUSIC_PATH + title + ".mp3");
			f.getParentFile().mkdirs();
			f.createNewFile();
			int read = 0;
			byte[] bytes = new byte[1024];
			OutputStream os = new FileOutputStream(f);
			while ((read = is.read(bytes)) != -1) {
				os.write(bytes, 0, read);
			}
			is.close();
			os.close();
			return f.getPath();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}