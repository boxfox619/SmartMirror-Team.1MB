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
			builder.append("대전 기준으로 오늘의 날씨는, ");
			builder.append(weather.getSky().replaceAll("ºC", "도")+", ");
			builder.append("강수확률은 ");
			builder.append(weather.getPop().replaceAll("ºC", "도")+", ");
			builder.append("습도는 ");
			builder.append(weather.getReh().replaceAll("ºC", "도")+", ");
			builder.append("온도는 ");
			builder.append(weather.getTemperature().replaceAll("ºC", "도")+", ");
			builder.append("최고기온 ");
			builder.append(weather.getTmax().replaceAll("ºC", "도")+", ");
			builder.append("최저기온 ");
			builder.append(weather.getTmin().replaceAll("ºC", "도")+", ");
			builder.append("입니다.");
		}
		play(builder.toString());
	}

    private synchronized static void play(String text){
        String clientId = "APIKEY";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "PY43k4UHM5";//애플리케이션 클라이언트 시크릿값";
        try {
            text = URLEncoder.encode(text, "UTF-8"); // 13자
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
            if(responseCode==200) { // 정상 호출
                InputStream is = con.getInputStream();
                int read = 0;
                byte[] bytes = new byte[1024];
                // 랜덤한 이름으로 mp3 파일 생성
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
            } else {  // 에러 발생
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