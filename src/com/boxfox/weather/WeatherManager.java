package com.boxfox.weather;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class WeatherManager {
	private final static String rssFeed = "http://www.kma.go.kr/wid/queryDFS.jsp?gridx=%s&gridy=%s"; // ���û
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
			weather.setTemperature(el.getChildTextTrim("temp") + "��C");
			weather.setTmax(el.getChildTextTrim("tmx") + "��C");
			weather.setTmin(el.getChildTextTrim("tmn") + "��C");
			weather.setSky(el.getChildTextTrim("wfKor"));
			weather.setPop(el.getChildTextTrim("ws") + "%");
			weather.setWd(el.getChildTextTrim("wdKor") + "ǳ " + el.getChildTextTrim("ws") + "m/s");
			weather.setReh(el.getChildTextTrim("reh") + "%");
			return weather;
			// data.put("seq", el.getAttributeValue("seq")); // 48�ð��� ���° ����
			// ����Ŵ, 3�ð� ������ �� ������
			// data.put("hour", el.getChildTextTrim("hour")); // ���׿��� 3�ð� ����
			// data.put("day", el.getChildTextTrim("day")); // 1��°�� (0: ����/1:
			// ����/2: ��)
			// data.put("temp", el.getChildTextTrim("temp")); // ���� �ð��µ�
			// data.put("tmx", el.getChildTextTrim("tmx")); // �ְ� �µ�
			// data.put("tmn", el.getChildTextTrim("tmn")); // ���� �µ�
			// data.put("sky", el.getChildTextTrim("sky")); // �ϴ� �����ڵ� (1: ����,
			// 2: ��������, 3:��������, 4:�帲)
			// data.put("pty", el.getChildTextTrim("pty")); // ���� �����ڵ� (0: ����,
			// 1: ��, 2: ��/��, 3: ��/��, 4: ��)
			// data.put("wfkor", el.getChildTextTrim("wfKor")); // ���� �ѱ���
			// data.put("wfEn", el.getChildTextTrim("wfEn")); // ���� ����
			// data.put("pop", el.getChildTextTrim("pop")); // ���� Ȯ��%
			// data.put("r12", el.getChildTextTrim("r12")); // 12�ð� ���� ������
			// data.put("s12", el.getChildTextTrim("s12")); // 12�ð� ���� ������
			// data.put("ws", el.getChildTextTrim("ws")); // ǳ��(m/s)
			// data.put("wd", el.getChildTextTrim("wd")); // ǳ�� (0~7:��, �ϵ�, ��,
			// ����, ��, ����, ��, �ϼ�)
			// data.put("wdKor", el.getChildTextTrim("wdKor")); // ǳ�� �ѱ���
			// data.put("wdEn", el.getChildTextTrim("wdEn")); // ǳ�� ����
			// data.put("reh", el.getChildTextTrim("reh")); // ���� %
			// data.put("r06", el.getChildTextTrim("r06")); // 6�ð� ���� ������
			// data.put("s06", el.getChildTextTrim("s06")); // 6�ð� ���� ������

		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
