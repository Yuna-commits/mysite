package com.bit2025.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bit2025.mysite.vo.GuestbookVo;

public class GuestbookDao {
	public int insert(GuestbookVo vo) {
		int result = 0;
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn
					.prepareStatement("insert into guestbook values(null, ?, password(?), ?, now())");
		) {
			pstmt.setString(1, vo.getName());
			pstmt.setString(2, vo.getPassword());
			pstmt.setString(3, vo.getMessage());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("DB 연결에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}
		
		return result;
	}
	
	public int deleteByIdAndPassword(Long id, String password) {
		int result = 0;
		
		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement("delete from guestbook where id=? and password=password(?)");
		) {
			pstmt.setLong(1, id);
			pstmt.setString(2, password);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println("DB 연결에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}
		
		return result;
	}
	
	public List<GuestbookVo> findAll() {
		List<GuestbookVo> result = new ArrayList<GuestbookVo>();

		try (
			Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(
					"select id, name, message, date_format(reg_date, '%Y-%m-%d %h:%i:%s') from guestbook order by reg_date desc");
			ResultSet rs = pstmt.executeQuery();
		) {
			while (rs.next()) {
				Long id = rs.getLong(1);
				String name = rs.getString(2);
				String message = rs.getString(3);
				String regDate = rs.getString(4);

				GuestbookVo vo = new GuestbookVo();
				vo.setId(id);
				vo.setName(name);
				vo.setMessage(message);
				vo.setRegDate(regDate);

				result.add(vo);
			}
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
			System.err.println("드라이버 로딩에 실패했습니다.");
			System.err.println("오류: " + e.getMessage());
		}

		return conn;
	}
}
