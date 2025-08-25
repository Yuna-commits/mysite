package com.bit2025.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.bit2025.mysite.vo.UserVo;

public class UserDao {

	public int insert(UserVo vo) {
		int result = 0;

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(
					"insert into user(name, email, password, gender, join_date) "
					+ "values (?, ?, password(?), ?, current_date())");
		) {
			// Parameter Binding
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getEmail());
			pstmt.setString(3, vo.getPassword());
			pstmt.setString(4, vo.getGender());

			result = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.err.println("DB 연결에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}

		return result;
	}

	private Connection getConnection() throws SQLException {
		Connection conn = null;

		try {
			// 1. JDBC Driver 로드 -> 실패시 ClassNotFoundException
			Class.forName("org.mariadb.jdbc.Driver");

			// 2. Connection 연결 -> 호출 위치로 반환, Exception도 호출 위치로
			String url = "jdbc:mariadb://192.168.0.181:3306/webdb";
			conn = DriverManager.getConnection(url, "webdb", "webdb");
		} catch (ClassNotFoundException e) {
			System.out.println("Driver Class Not Found");
		}

		return conn;
	}

}
