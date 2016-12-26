package com.boxfox.weather;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class WeatherManager {
	private final static String rssFeed = "http://www.kma.go.kr/wid/queryDFS.jsp?gridx=%s&gridy=%s"; // 기상청
	public final static String X = "36.3504119", Y = "127.3845475";

	public static Weather getWeather() {
		return getTownForecast(X, Y);
	}

	private static Weather getTownForecast(String x, String y) {
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);

		try {
			SAXBuilder parser = new SAXBuilder();
			parser.setIgnoringElementContentWhitespace(true);

			String url = String.format(rssFeed, x, y);
			Document doc = parser.build(url);
			Element root = doc.getRootElement();

			Element channel = root.getChild("body");
			List<Element> list = channel.getChildren("data");

			Element el = (Element) list.get(hour / 3);
			Weather weather = new Weather();
			weather.setTemperature(el.getChildTextTrim("temp") + "ºC");
			weather.setTmax(el.getChildTextTrim("tmx") + "ºC");
			weather.setTmin(el.getChildTextTrim("tmn") + "ºC");
			weather.setSky(el.getChildTextTrim("wfKor"));
			weather.setPop(el.getChildTextTrim("ws") + "%");
			weather.setWd(el.getChildTextTrim("wdKor") + "풍 " + el.getChildTextTrim("ws") + "m/s");
			weather.setReh(el.getChildTextTrim("reh") + "%");
			return weather;
			// data.put("seq", el.getAttributeValue("seq")); // 48시간중 몇번째 인지
			// 가르킴, 3시간 단위가 한 시퀀스
			// data.put("hour", el.getChildTextTrim("hour")); // 동네예보 3시간 단위
			// data.put("day", el.getChildTextTrim("day")); // 1번째날 (0: 오늘/1:
			// 내일/2: 모레)
			// data.put("temp", el.getChildTextTrim("temp")); // 현재 시간온도
			// data.put("tmx", el.getChildTextTrim("tmx")); // 최고 온도
			// data.put("tmn", el.getChildTextTrim("tmn")); // 최저 온도
			// data.put("sky", el.getChildTextTrim("sky")); // 하늘 상태코드 (1: 맑음,
			// 2: 구름조금, 3:구름많음, 4:흐림)
			// data.put("pty", el.getChildTextTrim("pty")); // 강수 상태코드 (0: 없음,
			// 1: 비, 2: 비/눈, 3: 눈/비, 4: 눈)
			// data.put("wfkor", el.getChildTextTrim("wfKor")); // 날씨 한국어
			// data.put("wfEn", el.getChildTextTrim("wfEn")); // 날씨 영어
			// data.put("pop", el.getChildTextTrim("pop")); // 강수 확률%
			// data.put("r12", el.getChildTextTrim("r12")); // 12시간 예상 강수량
			// data.put("s12", el.getChildTextTrim("s12")); // 12시간 예상 적설량
			// data.put("ws", el.getChildTextTrim("ws")); // 풍속(m/s)
			// data.put("wd", el.getChildTextTrim("wd")); // 풍향 (0~7:북, 북동, 동,
			// 남동, 남, 남서, 서, 북서)
			// data.put("wdKor", el.getChildTextTrim("wdKor")); // 풍향 한국어
			// data.put("wdEn", el.getChildTextTrim("wdEn")); // 풍향 영어
			// data.put("reh", el.getChildTextTrim("reh")); // 습도 %
			// data.put("r06", el.getChildTextTrim("r06")); // 6시간 예상 강수량
			// data.put("s06", el.getChildTextTrim("s06")); // 6시간 예상 적설량

		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
