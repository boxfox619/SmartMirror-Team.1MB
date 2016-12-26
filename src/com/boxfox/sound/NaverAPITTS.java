package com.boxfox.sound;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import com.boxfox.weather.Weather;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class NaverAPITTS {
	
	public static void play(Object obj){
		StringBuilder builder = new StringBuilder();
		if(obj instanceof String){
			builder.append((String)obj);
		}else if(obj instanceof Weather){
			Weather weather = (Weather)obj;
			builder.append("���� �������� ������ ������, ");
			builder.append(weather.getSky().replaceAll("��C", "��")+", ");
			builder.append("����Ȯ���� ");
			builder.append(weather.getPop().replaceAll("��C", "��")+", ");
			builder.append("������ ");
			builder.append(weather.getReh().replaceAll("��C", "��")+", ");
			builder.append("�µ��� ");
			builder.append(weather.getTemperature().replaceAll("��C", "��")+", ");
			builder.append("�ְ��� ");
			builder.append(weather.getTmax().replaceAll("��C", "��")+", ");
			builder.append("������� ");
			builder.append(weather.getTmin().replaceAll("��C", "��")+", ");
			builder.append("�Դϴ�.");
		}
		play(builder.toString());
	}

    private synchronized static void play(String text){
        String clientId = "APIKEY";//���ø����̼� Ŭ���̾�Ʈ ���̵�";
        String clientSecret = "PY43k4UHM5";//���ø����̼� Ŭ���̾�Ʈ ��ũ����";
        try {
            text = URLEncoder.encode(text, "UTF-8"); // 13��
            String apiURL = "https://openapi.naver.com/v1/voice/tts.bin";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            // post request
            String postParams = "speaker=jinho&speed=0&text=" + text;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // ���� ȣ��
                InputStream is = con.getInputStream();
                int read = 0;
                byte[] bytes = new byte[1024];
                // ������ �̸����� mp3 ���� ����
                String tempname = Long.valueOf(new Date().getTime()).toString();
                File f = new File(tempname + ".mp3");
                f.createNewFile();
                OutputStream outputStream = new FileOutputStream(f);
                while ((read =is.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }
                is.close();
                MediaPlayer mp = new MediaPlayer(new Media(f.toURI().toString()));
                mp.play();
                f.delete();
            } else {  // ���� �߻�
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
            }
        } catch (Exception e) {
        }
    }
}