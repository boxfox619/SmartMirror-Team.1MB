package com.onemb.planb.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.boxfox.controller.BaseController;
import com.boxfox.dto.Event;
import com.boxfox.dto.Memo;

public class MemoManager extends BaseManager{
	private Connection connection = null;
	private Statement statement = null;

	public MemoManager(){
		try {
			Class.forName("org.sqlite.JDBC");
			try {
				connection = DriverManager.getConnection("jdbc:sqlite:memo.db");
				statement = connection.createStatement();
				String str = "CREATE TABLE IF NOT EXISTS memo(number INTEGER PRIMARY KEY AUTOINCREMENT, date DATETIME NOT NULL, content TEXT NOT NULL);";
				statement.execute(str);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	

	public void addMemo(String text) {
		String sql = "INSERT INTO memo (date, content) VALUES(DATETIME('NOW', 'LOCALTIME'), '" + text + "');";
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		update(new Event(MemoManager.class, getMemoList()));
	}

	public void deleteMemo(int num) {
		String sql = "delete * FROM memo;";
		try {
			statement.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		update(new Event(MemoManager.class, getMemoList()));
	}

	public ArrayList<Memo> getMemoList() {
		ArrayList<Memo> list = new ArrayList<Memo>();
		String sql = "SELECT * FROM memo;";
		try {
			ResultSet resultSet = statement.executeQuery(sql);
			while (resultSet.next()) {
				int num = resultSet.getInt("number");
				String date = resultSet.getString("date");
				String content = resultSet.getString("content");
				Memo memo = new Memo(num, content, date);
				list.add(memo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	

}
